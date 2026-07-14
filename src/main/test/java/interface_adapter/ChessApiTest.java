package interface_adapter;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessApiEngineGatewayTest {

    private static final String START =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void ping_returnsJsonWithExpectedFields() throws Exception {
        JsonObject json = new ChessApiEngineGateway().ping(START);

        System.out.println(json);
        assertTrue(json.has("eval"));
        assertTrue(json.has("move"));
        assertTrue(json.has("text"));
    }

    @Test
    void bestMoveMessage_returnsNonEmptyText() throws Exception {
        String message = new ChessApiEngineGateway().bestMoveMessage(START);

        System.out.println(message);
        assertNotNull(message);
        assertFalse(message.isBlank());
    }
}
