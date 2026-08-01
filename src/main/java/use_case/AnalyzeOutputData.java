package use_case;

public record AnalyzeOutputData(String boardEval, String bestMove,
                                boolean isWhiteTurn) {
    /**
     * Bundles the raw analysis results for the presenter to format.
     *
     * @param boardEval, the evaluation text
     * @param bestMove, the best move text
     * @param isWhiteTurn, whether it is White's turn
     */
    public AnalyzeOutputData {
    }
}
