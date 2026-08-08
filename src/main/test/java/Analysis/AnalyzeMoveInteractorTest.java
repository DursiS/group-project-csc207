package Analysis;

import com.google.gson.JsonObject;
import entity.Board;
import entity.BoardStateList;
import entity.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyzeMoveInteractorTest {

    private static final String START_FEN
            = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String NO_EN_PASSANT_FEN
            = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1";

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState(new Board(), 0, 0, new BoardStateList(), "test");
    }

    private AnalyzeMoveInteractor interactorWith(ChessApiInterface api, AnalyzeOutputBoundary out) {
        return new AnalyzeMoveInteractor(api, out, gameState);
    }

    @Test
    void executeTurnAnalysisPresentsOutput() throws IOException {
        ChessApiInterface adapter = mock(ChessApiInterface.class);
        when(adapter.request(anyString())).thenReturn(mockRequest());
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(adapter, presenter);

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
        AnalyzeMoveInteractor rejecting = interactorWith(rejectingApi, mock(AnalyzeOutputBoundary.class));

        assertThrows(IllegalStateException.class, rejecting::executeTurnAnalysis);
    }

    @Test
    void networkFailureThrowsIOException() throws IOException {
        ChessApiInterface downApi = mock(ChessApiInterface.class);
        when(downApi.request(anyString())).thenThrow(new IOException("network down"));
        AnalyzeMoveInteractor offline = interactorWith(downApi, mock(AnalyzeOutputBoundary.class));

        assertThrows(IOException.class, offline::executeTurnAnalysis);
    }

    @Test
    void convertsToFenCorrectly() {
        BoardToFenTranslator translator = new BoardToFenTranslator();
        assertEquals(START_FEN, translator.convertToFen(new Board(), true));
        assertEquals(NO_EN_PASSANT_FEN, translator.convertToFen(enPassantBoard(), false));
    }

    private static Board enPassantBoard() {
        // a real double-pawn-move leaves an en-passant-able pawn (code 3);
        // the FEN must still omit the en-passant square, since the API rejects it.
        return new Board(new int[][]{
                {-4, -6, -7, -8, -9, -7, -6, -4},
                {-1, -1, -1,  0, -1, -1, -1, -1},
                { 0,  0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0,  0},
                { 0,  0,  0, -2,  3,  0,  0,  0},
                { 0,  0,  0,  0,  0,  0,  0,  0},
                { 1,  1,  1,  1,  0,  1,  1,  1},
                { 4,  6,  7,  8,  9,  7,  6,  4}
        }, 0, 0, 0);
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
