package interface_adapter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

public class ChessApiAdapterTest {

    private static final String START =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void adapterRequestReturnsNonEmptyJsonObject() throws Exception {
        final JsonObject json = new ChessApiAdapter().request(START);

        System.out.println(json);
        Assertions.assertTrue(json.has("eval"));
        Assertions.assertTrue(json.has("move"));
        Assertions.assertTrue(json.has("text"));
        Assertions.assertInstanceOf(JsonObject.class, json);
    }
}
