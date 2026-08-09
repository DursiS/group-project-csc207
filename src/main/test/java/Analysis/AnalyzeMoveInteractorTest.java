package Analysis;

import com.google.gson.JsonObject;
import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.beans.PropertyChangeEvent;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class AnalyzeMoveInteractorTest {

    private static final String START_FEN
            = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final String NO_EN_PASSANT_FEN
            = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1";
    private static final String UPDATE_CHANNEL = "update-analysis";

    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameState = new GameState(new Board(), 0, 0, new BoardStateList(), "test");
    }

    private AnalyzeMoveInteractor interactorWith(ChessApiInterface api, AnalyzeOutputBoundary out) {
        return new AnalyzeMoveInteractor(api, out, gameState);
    }

    private static ChessApiInterface validApi() throws IOException {
        ChessApiInterface api = mock(ChessApiInterface.class);
        when(api.request(anyString())).thenReturn(mockRequest());
        return api;
    }

    @Test
    void executeTurnAnalysisPresentsOutput() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.executeTurnAnalysis();

        verify(presenter).addMessage(any(AnalyzeOutputData.class));
    }

    @Test
    void getRecentBoardUsesLastBoardInHistory() throws IOException {
        BoardStateList history = new BoardStateList();
        history.addBoardCopy(new Board());
        gameState = new GameState(new Board(), 0, 0, history, "test");
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.executeTurnAnalysis();

        verify(presenter).addMessage(any(AnalyzeOutputData.class));
    }

    @Test
    void turnColourAlternatesByMessageCount() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.executeTurnAnalysis();
        interactor.executeTurnAnalysis();

        // way of checking the raw data the presenter is getting alternates
        ArgumentCaptor<AnalyzeOutputData> captor = ArgumentCaptor.forClass(AnalyzeOutputData.class);
        verify(presenter, times(2)).addMessage(captor.capture());
        assertTrue(captor.getAllValues().get(0).isWhiteTurn());
        assertFalse(captor.getAllValues().get(1).isWhiteTurn());
    }

    @Test
    void propertyChangeOnUpdateChannelRunsAnalysis() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.propertyChange(new PropertyChangeEvent(this, UPDATE_CHANNEL, null, gameState));

        verify(presenter, timeout(3000)).addMessage(any(AnalyzeOutputData.class));
    }

    @Test
    void propertyChangeOnOtherChannelIsIgnored() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        // we subscribed to only the analysis channel
        interactor.propertyChange(new PropertyChangeEvent(this, "some-other-channel", null, gameState));

        verifyNoInteractions(presenter);
    }

    @Test
    void analyzeInitialPositionRunsAnalysis() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.analyzeInitialPosition();

        // may take some time to initial call addMessage at start => timeout
        verify(presenter, timeout(3000)).addMessage(any(AnalyzeOutputData.class));
    }

    @Test
    void singleMessageDisplayDelegatesToBoundary() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.executeSingleMessageDisplay();

        verify(presenter).setRecentMessage();
    }

    @Test
    void messageHistoryDisplayDelegatesToBoundary() throws IOException {
        AnalyzeOutputBoundary presenter = mock(AnalyzeOutputBoundary.class);
        AnalyzeMoveInteractor interactor = interactorWith(validApi(), presenter);

        interactor.executeMessageHistoryDisplay();

        // sees if the method is called
        verify(presenter).setHistoryMessage();
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
        BoardToFenAdapter translator = new BoardToFenAdapter();
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
