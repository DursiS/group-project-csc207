package interface_adapter;

import com.google.gson.JsonObject;
import entity.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.AnalyzeMoveInteractor;
import use_case.ChessApiInterface;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void integratedNonEmptyNewTurnAnalysis() throws Exception {
        String analysis = interactor.getAnalysisMessage(START_FEN);
        // calls real api to get this message, crosses a boundary = integration

        System.out.println(analysis);
        assertNotNull(analysis);
        assertFalse(analysis.isBlank());
    }

    @Test
    void convertsToFen() {
        String convertedStartFen = interactor.convertToFen(BOARD);
        assertEquals(START_FEN, convertedStartFen);

        String convertedRandomFen = interactor
                .convertToFen(OTHER_BOARD);
        assertEquals(OTHER_FEN, convertedRandomFen);
    }

    private JsonObject mockRequest(String fen){
        JsonObject response = new JsonObject();

        // fixed random (but valid) info
        response.addProperty("eval", 0.42);
        response.addProperty("winChance", 45.14);
        response.addProperty("from", "g8");
        response.addProperty("to", "f6");
        response.addProperty("move", "g8f6");
        response.addProperty("text", "Move: Ng8 -> f6");
        return response;
    }


    @Test
    void mockExecuteTurnAnalysis() throws Exception {
        ChessApiInterface adapter = mock(ChessApiInterface.class);

        when(adapter.request(anyString())).thenReturn(mockRequest(START_FEN));

        AnalyzePresenter presenter = mock(AnalyzePresenter.class);
        AnalyzeMoveInteractor mockedInteractor = new AnalyzeMoveInteractor(adapter, presenter);

        String analysis = mockedInteractor.getAnalysisMessage(START_FEN);
        System.out.println(analysis);
        assertNotNull(analysis);
        assertFalse(analysis.isBlank());
    }
}
