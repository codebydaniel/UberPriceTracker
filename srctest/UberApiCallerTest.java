import com.sun.tools.javac.util.Pair;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UberApiCallerTest {

    UberApiCaller caller = new UberApiCaller();

    @Test
    public void checkInputStreamNotNull() {
        assertNotNull(caller.getResponseFromUber());
    }

    @Test
    public void getPricesFromJson() {
        Pair<String, Double> expected = caller.getUberPoolPrices(getSampleData());
        assertEquals("POOL", expected.fst);
        assertEquals(new Double(19.38), expected.snd);
    }

    private String getSampleData() {
        return "{\n" +
                "   \"prices\":[\n" +
                "      {\n" +
                "         \"localized_display_name\":\"POOL\",\n" +
                "         \"distance\":9.77,\n" +
                "         \"display_name\":\"POOL\",\n" +
                "         \"product_id\":\"26546650-e557-4a7b-86e7-6a3942445247\",\n" +
                "         \"high_estimate\":20,\n" +
                "         \"surge_multiplier\":1.0,\n" +
                "         \"minimum\":null,\n" +
                "         \"low_estimate\":19,\n" +
                "         \"duration\":900,\n" +
                "         \"estimate\":\"$19.38\",\n" +
                "         \"currency_code\":\"USD\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"localized_display_name\":\"uberX\",\n" +
                "         \"distance\":9.77,\n" +
                "         \"display_name\":\"uberX\",\n" +
                "         \"product_id\":\"a1111c8c-c720-46c3-8534-2fcdd730040d\",\n" +
                "         \"high_estimate\":26,\n" +
                "         \"surge_multiplier\":1.0,\n" +
                "         \"minimum\":7,\n" +
                "         \"low_estimate\":20,\n" +
                "         \"duration\":900,\n" +
                "         \"estimate\":\"$20-26\",\n" +
                "         \"currency_code\":\"USD\"\n" +
                "      },\n" +
                "      {\n" +
                "         \"localized_display_name\":\"uberXL\",\n" +
                "         \"distance\":9.77,\n" +
                "         \"display_name\":\"uberXL\",\n" +
                "         \"product_id\":\"821415d8-3bd5-4e27-9604-194e4359a449\",\n" +
                "         \"high_estimate\":38,\n" +
                "         \"surge_multiplier\":1.0,\n" +
                "         \"minimum\":9,\n" +
                "         \"low_estimate\":30,\n" +
                "         \"duration\":900,\n" +
                "         \"estimate\":\"$30-38\",\n" +
                "         \"currency_code\":\"USD\"\n" +
                "      }\n" +
                "   ]\n" +
                "}\n";
    }
}

