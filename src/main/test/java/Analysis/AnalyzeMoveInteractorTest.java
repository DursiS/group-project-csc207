package Analysis;

import com.google.gson.JsonObject;
import entity.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyzeMoveInteractorTest {

    private static final String START_FEN
            = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String BAD_FEN
            = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    private static final Board BOARD = new Board();
    private static Board BAD_BOARD;

    @BeforeAll
    static void setUp() {
        // entity.Board(squares, turn, verticalEdgeType, horizontalEdgeType);
        // turn is unused by the translator now (isWhiteTurn is passed in explicitly).
        BAD_BOARD = new Board(new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1, 0,-1,-1,-1,-1},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0,-2, 3, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 1, 1, 1, 1, 0, 1, 1, 1},
                { 4, 6, 7, 8, 9, 7, 6, 4}
        }, 0, 0, 0);
    }

    @Test
    void executeTurnAnalysisPresentsOutput() throws IOException {
        ChessApiInterface adapter = mock(ChessApiInterface.class);
        when(adapter.request(anyString())).thenReturn(mockRequest());
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = new AnalyzeMoveInteractor(adapter, presenter);

        interactor.executeTurnAnalysis();

        verify(presenter).addMessage(any(AnalyzeOutputData.class));
    }

    @Test
    void rejectedPositionThrowsIllegalState() throws IOException {
        ChessApiInterface rejectingApi = mock(ChessApiInterface.class);
        JsonObject error = new JsonObject();
        error.addProperty("type", "error");
        error.addProperty("text", "wrong FEN");
        when(rejectingApi.request(anyString())).thenReturn(error);
        AnalyzeMoveInteractor rejecting = new AnalyzeMoveInteractor(
                rejectingApi, mock(AnalyzeOutputBoundary.class));

        assertThrows(IllegalStateException.class, rejecting::executeTurnAnalysis);
    }

    @Test
    void networkFailureThrowsIOException() throws IOException {
        ChessApiInterface downApi = mock(ChessApiInterface.class);
        when(downApi.request(anyString())).thenThrow(new IOException("network down"));
        AnalyzeMoveInteractor offline = new AnalyzeMoveInteractor(
                downApi, mock(AnalyzeOutputBoundary.class));

        assertThrows(IOException.class, offline::executeTurnAnalysis);
    }

    @Test
    void convertsToFenCorrectly() {
        BoardToFenTranslator translator = new BoardToFenTranslator();
        assertEquals(START_FEN, translator.convertToFen(BOARD, true));
        assertEquals(BAD_FEN, translator.convertToFen(BAD_BOARD, false));
    }

    private static JsonObject mockRequest() {
        JsonObject response = new JsonObject();

        // fixed random (but still valid) info
        response.addProperty("eval", 0.42);
        response.addProperty("winChance", 45.14);
        response.addProperty("from", "g8");
        response.addProperty("to", "f6");
        response.addProperty("move", "g8f6");
        response.addProperty("text", "Move: Ng8 -> f6");
        return response;
    }
}
