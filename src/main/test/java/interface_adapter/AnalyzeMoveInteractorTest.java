package interface_adapter;

import com.google.gson.JsonObject;
import entity.Board;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import use_case.AnalyzeMoveInteractor;
import use_case.AnalyzeOutputData;
import use_case.BoardToFenTranslator;
import use_case.ChessApiInterface;
import use_case.GameStateDataAccessInterface;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AnalyzeMoveInteractorTest {

    private static final String START_FEN
            = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";;
    private static final String BAD_FEN = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";;
    private static final Board BOARD = new Board();
    private static Board BAD_BOARD;
    private static AnalyzeMoveInteractor interactor;
    private static AnalyzeMoveInteractor integrated_interactor;

    @BeforeAll
    static void setUp() throws IOException {
        ChessApiInterface adapter = mock(ChessApiInterface.class);
        AnalyzePresenter presenter = mock(AnalyzePresenter.class);
        GameStateDataAccessInterface dataInterface
                = mock(GameStateDataAccessInterface.class);

        when(adapter.request(anyString())).thenReturn(mockRequest());
        when(dataInterface.getRecentBoard()).thenReturn(BOARD);
        interactor = new AnalyzeMoveInteractor(adapter,
                presenter, dataInterface);

        integrated_interactor = new AnalyzeMoveInteractor(new ChessApiAdapter(),
                presenter, dataInterface);

        BAD_BOARD = new Board(new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1, 0,-1,-1,-1,-1},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0,-2, 3, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 1, 1, 1, 1, 0, 1, 1, 1},
                { 4, 6, 7, 8, 9, 7, 6, 4}
        }, 4);
    }

    @Test
    void rejectedPositionThrowsIllegalState() throws IOException {
        ChessApiInterface rejectingApi = mock(ChessApiInterface.class);
        JsonObject error = new JsonObject();
        error.addProperty("type", "error");
        error.addProperty("text", "wrong FEN");
        when(rejectingApi.request(anyString())).thenReturn(error);

        GameStateDataAccessInterface data = mock(GameStateDataAccessInterface.class);
        when(data.getRecentBoard()).thenReturn(new Board());
        AnalyzeMoveInteractor rejecting = new AnalyzeMoveInteractor(
                rejectingApi, mock(AnalyzePresenter.class), data);

        assertThrows(IllegalStateException.class, rejecting::executeTurnAnalysis);
    }

    @Test
    void networkFailureThrowsIOException() throws IOException {
        ChessApiInterface downApi = mock(ChessApiInterface.class);
        when(downApi.request(anyString())).thenThrow(new IOException("network down"));

        GameStateDataAccessInterface data = mock(GameStateDataAccessInterface.class);
        when(data.getRecentBoard()).thenReturn(new Board());
        AnalyzeMoveInteractor offline = new AnalyzeMoveInteractor(
                downApi, mock(AnalyzePresenter.class), data);

        assertThrows(IOException.class, offline::executeTurnAnalysis);
    }

    @Test
    void convertsToFenCorrectly() {
        BoardToFenTranslator translator = new BoardToFenTranslator();
        String convertedStartFen = translator.convertToFen(BOARD);
        assertEquals(START_FEN, convertedStartFen);

        String convertedRandomFen = translator.convertToFen(BAD_BOARD);
        assertEquals(BAD_FEN, convertedRandomFen);
    }

    private static JsonObject mockRequest(){
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
