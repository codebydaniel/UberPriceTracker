import com.google.common.annotations.VisibleForTesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UberApiCaller {

    private static final double EMERYVILLE_LATITUDE = 0;
    private static final double EMERYVILLE_LONGITUDE = 0;
    private static final double SAN_FRANCISCO_LATITUDE = 0;
    private static final double SAN_FRANCISCO_LONGITUDE = 0;
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
        String xmlResponse = translateToString(inputStreamResponse);
    }

    @VisibleForTesting
    public static InputStream getResponseFromUber() {
        try {
            URL url = new URL(UBER_API_URL);
            URLConnection connection = url.openConnection();
            return connection.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @VisibleForTesting
    private static String translateToString(InputStream inputStreamResponse) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStreamResponse)).readLine();
    }


}
