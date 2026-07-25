package interface_adapter;

import com.google.gson.JsonObject;
import entity.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.AnalyzeMoveInteractor;
import use_case.ChessApiInterface;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyzeMoveInteractorTest {

    private static String START_FEN;
    private static String OTHER_FEN;
    private static Board BOARD;
    private static Board OTHER_BOARD;
    private static AnalyzeMoveInteractor interactor;

    @BeforeAll
    static void setUp() {
        ChessApiAdapter adapter = new ChessApiAdapter();
        AnalyzePresenter presenter = new AnalyzePresenter();
        interactor = new AnalyzeMoveInteractor(adapter, presenter);

        START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        OTHER_FEN = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";

        BOARD = new Board();
        OTHER_BOARD = new Board(new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1, 0,-1,-1,-1,-1},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0,-2, 3, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 1, 1, 1, 1, 0, 1, 1, 1},
                { 4, 6, 7, 8, 9, 7, 6, 4}
        }, 5);
    }

    @Test
    void nonEmptyNewTurnAnalysis() throws Exception {
        String analysis = interactor.getAnalysisMessage(START_FEN);

        System.out.println(analysis);
        assertNotNull(analysis);
        assertFalse(analysis.isBlank());
    }

    @Test
    void convertFen() {
        String convertedStartFen = interactor.convertToFen(BOARD);
        assertEquals(START_FEN, convertedStartFen);

        String convertedRandomFen = interactor
                .convertToFen(OTHER_BOARD);
        assertEquals(OTHER_FEN, convertedRandomFen);
    }

    private static class MockChessApiInterface implements ChessApiInterface {

        @Override
        public JsonObject request(String fen){
            JsonObject response = new JsonObject();

            // random (but valid) info
            response.addProperty("eval", 0.42);
            response.addProperty("winChance", 45.14);
            response.addProperty("from", "g8");
            response.addProperty("to", "f6");
            response.addProperty("move", "g8f6");
            response.addProperty("text", "Move: Ng8 -> f6");
            return response;
        }
    }

    @Test
    void mockApiInterfaceNonEmptyAnalysis() throws Exception{
        MockChessApiInterface adapter = new MockChessApiInterface(); // Mock
        AnalyzePresenter presenter = new AnalyzePresenter();
        AnalyzeMoveInteractor mockInteractor = new AnalyzeMoveInteractor(adapter
                , presenter);

        String analysis = mockInteractor.getAnalysisMessage(START_FEN);

        System.out.println(analysis);
        assertNotNull(analysis);
        assertFalse(analysis.isBlank());
    }
}
