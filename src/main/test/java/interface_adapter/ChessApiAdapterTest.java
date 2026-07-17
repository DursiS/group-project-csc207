package interface_adapter;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChessApiAdapterTest {

    private static final String START =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void adapterRequestReturnsNonEmptyJsonObject() throws Exception {
        JsonObject json = new ChessApiAdapter().request(START);

        System.out.println(json);
        assertTrue(json.has("eval"));
        assertTrue(json.has("move"));
        assertTrue(json.has("text"));
        assertInstanceOf(JsonObject.class, json);
    }
}
