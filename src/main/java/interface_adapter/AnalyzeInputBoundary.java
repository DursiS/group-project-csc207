package interface_adapter;

public interface AnalyzeInputBoundary {

    /**
     * Runs the analysis for the current turn.
     * @throws Exception if the analysis fails
     */
    void executeTurnAnalysis() throws Exception;
}
