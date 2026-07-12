package interface_adapter;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EngineGatewayTest {

    private static final String START =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Test
    void ping_returnsJsonWithExpectedFields() throws Exception {
        JsonObject json = new EngineGateway().ping(START);

        System.out.println(json);
        assertTrue(json.has("eval"));
        assertTrue(json.has("move"));
        assertTrue(json.has("text"));
    }

    @Test
    void bestMoveMessage_returnsNonEmptyText() throws Exception {
        String message = new EngineGateway().BestMoveMessage(START);

        System.out.println(message);
        assertNotNull(message);
        assertFalse(message.isBlank());
    }
}