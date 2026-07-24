package interface_adapter;

public class AnalyzeController {
    private final AnalyzeInputBoundary inputBoundary;

    public AnalyzeController(AnalyzeInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Triggers the analysis for the current turn.
     * @throws Exception if the analysis fails
     */
    public void executeTurnAnalysis() throws Exception {
        this.inputBoundary.executeTurnAnalysis();
    }
}
