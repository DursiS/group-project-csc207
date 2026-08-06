package use_case;

import java.io.IOException;

public interface AnalyzeInputBoundary {

    /**
     * Runs the analysis for the current turn.
     * @throws IOException if the analysis fails
     */
    void executeTurnAnalysis() throws IOException;

    /**
     * Shows only the most recent analysis message.
     */
    void executeSingleMessageDisplay();

    /**
     * Shows the whole analysis message history.
     */
    void executeMessageHistoryDisplay();
}
