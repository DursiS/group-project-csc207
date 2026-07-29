package interface_adapter;

import java.io.IOException;

public interface AnalyzeInputBoundary {

    /**
     * Runs the analysis for the current turn.
     * @throws IOException if the analysis fails
     */
    void executeTurnAnalysis() throws IOException;
}
