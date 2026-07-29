package interface_adapter;

import java.io.IOException;

public class AnalyzeController {
    private final AnalyzeInputBoundary inputBoundary;

    public AnalyzeController(AnalyzeInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    /**
     * Triggers the analysis for the current turn.
     * @throws IOException if the analysis fails
     */
    public void executeTurnAnalysis() throws IOException {
        this.inputBoundary.executeTurnAnalysis();
    }
}
