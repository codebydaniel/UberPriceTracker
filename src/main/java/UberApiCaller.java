import com.google.common.annotations.VisibleForTesting;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UberApiCaller {

    private static final DateTimeFormatter FILE_FORMAT = DateTimeFormatter.ofPattern("MM-dd");
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
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
        LocalDateTime timeOfApiCall = LocalDateTime.now();
        InputStream inputStreamResponse = getResponseFromUber();
        String jsonResponse = translateToString(inputStreamResponse);
        Double uberPoolPrice = getUberPoolPrice(jsonResponse);
        writeToFile(timeOfApiCall, uberPoolPrice);
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
    static Double getUberPoolPrice(String jsonResponse) {
        JsonElement jsonElement = new JsonParser().parse(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject().getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("prices");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();
            String uberType = object.get("display_name").getAsString();
            if(uberType.equals("POOL")) {
                String priceAsString = object.get("estimate").getAsString().substring(1);
                return Double.valueOf(priceAsString);
            }
        }
        return Double.MAX_VALUE;
    }

    private static void writeToFile(LocalDateTime timeOfApiCall, Double uberPoolPrice) throws IOException {
        if(uberPoolPrice.equals(Double.MAX_VALUE)) {
            return;
        } else {
            String date = timeOfApiCall.toLocalDate().format(FILE_FORMAT);
            String dateTime = timeOfApiCall.format(DATA_FORMAT);
            try {
                String fileName = String.format("uber pool prices for %s", date);
                String toWrite = String.format("DateTime of API Call: %s\nPrice: %s\n\n", dateTime, uberPoolPrice);
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.write(toWrite);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
