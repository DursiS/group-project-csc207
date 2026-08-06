package Analysis;

import java.io.IOException;

import com.google.gson.JsonObject;

public class AnalyzeMoveInteractor implements AnalyzeInputBoundary {

    private Integer messageCount = 1;
    private final ChessApiInterface apiInterface;
    private final AnalyzeOutputBoundary analyzeOutputBoundary;
    private final GameStateDataAccessInterface gameStateDataAccess;
    private final BoardToFenTranslator fenTranslator = new BoardToFenTranslator();

    /**
     * Constructs the interactor with its dependencies.
     * Uses dependencies injecting instead of hard dependencies.
     * @param apiInterface the chess API to use
     * @param analyzeOutputBoundary the output boundary to present results
     * @param gameStateDataAccess the source of the current board
     */
    public AnalyzeMoveInteractor(ChessApiInterface apiInterface,
                                 AnalyzeOutputBoundary analyzeOutputBoundary,
                                 GameStateDataAccessInterface gameStateDataAccess) {
        this.apiInterface = apiInterface;
        this.analyzeOutputBoundary = analyzeOutputBoundary;
        this.gameStateDataAccess = gameStateDataAccess;
    }

    // SRP
    @Override
    public void executeTurnAnalysis() throws IOException {
        final Board board = getRecentBoard();
        final String fen = this.fenTranslator.convertToFen(board);
        final JsonObject response = requestOrThrow(fen);
        final AnalyzeOutputData outputData = new AnalyzeOutputData(
                response.get("winChance").getAsDouble(),
                response.get("eval").getAsDouble(),
                response.get("from").getAsString(),
                response.get("to").getAsString(),
                board.isWhiteTurn(),
                messageCount
        );
        this.analyzeOutputBoundary.addMessage(outputData);
        messageCount += 1;
    }

    private Board getRecentBoard() {
        return this.gameStateDataAccess.getRecentBoard();
    }

    @Override
    public void executeSingleMessageDisplay() {
        this.analyzeOutputBoundary.setRecentMessage();
    }

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
        // that led to the error right away
        if (response.has("type") && "error".equals(response.get("type").getAsString())) {
            throw new IllegalStateException("Chess API rejected the position: "
                    + response.get("text"));
        }
        return response;
    }
}
