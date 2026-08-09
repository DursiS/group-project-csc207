package Analysis;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import com.google.gson.JsonObject;
import entity.Board;
import entity.BoardStateList;
import entity.GameState;

public class AnalyzeMoveInteractor implements AnalyzeInputBoundary, PropertyChangeListener {
    private static final String UPDATE_CHANNEL = "update-analysis";
    private Integer messageCount = 1;
    private final ChessApiInterface apiInterface;
    private final AnalyzeOutputBoundary analyzeOutputBoundary;
    private final BoardToFenAdapter fenTranslator = new BoardToFenAdapter();
    private GameState gameState;

    /**
     * Constructs the interactor with its dependencies.
     * Uses dependencies injecting instead of hard dependencies.
     * @param apiInterface the chess API to use
     * @param analyzeOutputBoundary the output boundary to present results
     * @param gameState the initial game state to analyze
     */
    public AnalyzeMoveInteractor(ChessApiInterface apiInterface,
                                 AnalyzeOutputBoundary analyzeOutputBoundary,
                                 GameState gameState) {
        this.apiInterface = apiInterface;
        this.analyzeOutputBoundary = analyzeOutputBoundary;
        this.gameState = gameState;
    }

    /**
     * Receives an update signal and re-runs the analysis off the UI thread.
     * Updates reference to the latest game state from MakeMoveInteractor
     * @param propertyChangeEvent the fired update event
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (UPDATE_CHANNEL.equals(propertyChangeEvent.getPropertyName())) {
            this.gameState = (GameState) propertyChangeEvent.getNewValue();
            runAnalysisAsync();
        }
    }

    /**
     * Runs one analysis of the starting position at app startup.
     * The observer signal from MakeMove fires before this listener is
     * subscribed, so the opening position is kicked off here instead.
     */
    public void analyzeInitialPosition() {
        runAnalysisAsync();
    }

    /**
     * Runs the analysis on a background thread to keep the UI responsive.
     */
    private void runAnalysisAsync() {
        new Thread(() -> {
            try {
                executeTurnAnalysis();
            }
            catch (IOException event) {
                throw new RuntimeException(event);
            }
        }).start();
    }

    /**
     * Analyzes the most recent board and pushes the result to the presenter.
     * @throws IOException if the API request fails
     */
    @Override
    public void executeTurnAnalysis() throws IOException {
        final Board board = getRecentBoard();
        final String fen = this.fenTranslator.convertToFen(board,
                isWhiteTurn()
        );
        final JsonObject response = requestOrThrow(fen);
        final AnalyzeOutputData outputData = new AnalyzeOutputData(
                response.get("winChance").getAsDouble(),
                response.get("eval").getAsDouble(),
                response.get("from").getAsString(),
                response.get("to").getAsString(),
                isWhiteTurn(),
                messageCount
        );
        this.analyzeOutputBoundary.addMessage(outputData);
        messageCount += 1;
    }

    /**
     * Reports whether it is white's turn for the current analysis.
     * @return true if white is to move, false otherwise
     */
    private boolean isWhiteTurn() {
        return messageCount % 2 == 1;
    }

    /**
     * Returns the latest board to analyze.
     * @return the last board in the state list, or the opening board if empty
     */
    private Board getRecentBoard() {
        final BoardStateList boardList = gameState.getBoardStateListCopy();
        if (boardList.size() == 0) {
            return gameState.getBoard();
        }
        else {
            return boardList.getBoardCopy(boardList.size() - 1);
        }
    }

    /**
     * Shows only the most recent analysis message in the view.
     */
    @Override
    public void executeSingleMessageDisplay() {
        this.analyzeOutputBoundary.setRecentMessage();
    }

    /**
     * Shows the full analysis message history in the view.
     */
    @Override
    public void executeMessageHistoryDisplay() {
        this.analyzeOutputBoundary.setHistoryMessage();
    }

    /**
     * Sends a request and fails fast if the API rejects the position.
     * @param fen the position as a FEN string
     * @return the API response
     * @throws IOException if the request fails
     * @throws IllegalStateException if the API rejects the position
     */
    private JsonObject requestOrThrow(String fen)
            throws IOException, IllegalStateException {
        final JsonObject response = this.apiInterface.request(fen);

        // we handle it here because we can communicate the fen
        // that led to the error right away, good
        if (response.has("type") && "error".equals(response.get("type").getAsString())) {
            throw new IllegalStateException("Chess API rejected the position: "
                    + response.get("text"));
        }
        return response;
    }
}
