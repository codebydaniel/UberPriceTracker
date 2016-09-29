import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class UberApiCaller {

    private static final double EMERYVILLE_LATITUDE = 37.846390;
    private static final double EMERYVILLE_LONGITUDE = -122.293033;
    private static final double SAN_FRANCISCO_LATITUDE = 37.796860;
    private static final double SAN_FRANCISCO_LONGITUDE = -122.398786;
    private static final String SERVER_TOKEN = "MfvqqHIflPDOx32PAuxajcNjhdkmBEbHL2gdG0uT";
    private static final String UBER_API_URL = String.format(
            "https://api.uber.com/v1/estimates/price?start_latitude=%s&start_longitude=%s&end_latitude=%s&end_longitude=%s&server_token=%s",
            SAN_FRANCISCO_LATITUDE,
            SAN_FRANCISCO_LONGITUDE,
            EMERYVILLE_LATITUDE,
            EMERYVILLE_LONGITUDE,
            SERVER_TOKEN);

    public static void main(String[] args) throws IOException {
        InputStream inputStreamResponse = getResponseFromUber();
        String jsonResponse = translateToString(inputStreamResponse);
        Map<String, Double> uberTypeToPrices = getPricesFromJSON(jsonResponse);
    }

    @VisibleForTesting
    static InputStream getResponseFromUber() {
        try {
            URL url = new URL(UBER_API_URL);
            URLConnection connection = url.openConnection();
            return connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String translateToString(InputStream inputStreamResponse) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStreamResponse)).readLine();
    }

    @VisibleForTesting
    static Map<String, Double> getPricesFromJSON(String jsonResponse) {
        Map<String, Double> typeToPrices = Maps.newHashMap();
        JsonElement jsonElement = new JsonParser().parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("prices");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            String uberType = object.get("display_name").getAsString();
            String priceAsString = object.get("estimate").getAsString();
            Double price;
            if (priceAsString.contains("-")) {
                price = Double.valueOf(priceAsString.substring(priceAsString.indexOf("-") + 1, priceAsString.length()));
            } else {
                price = Double.valueOf(priceAsString.substring(1));
            }
            typeToPrices.put(uberType, price);
        }
        return typeToPrices;
    }

}
