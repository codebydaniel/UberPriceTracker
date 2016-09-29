import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UberApiCallerTest {

    @Test
    public void checkInputStreamNotNull() {
        UberApiCaller caller = new UberApiCaller();
        assertNotNull(caller.getResponseFromUber());
    }
}
