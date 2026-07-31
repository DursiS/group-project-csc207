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

    /**
     * Requests showing only the most recent message.
     */
    public void executeSingleMessageDisplay() {
        this.inputBoundary.executeSingleMessageDisplay();
    }

    /**
     * Requests showing the whole message history.
     */
    public void executeMessageHistoryDisplay() {
        this.inputBoundary.executeMessageHistoryDisplay();
    }
}
