package use_case;

public interface SelectGameOutputBoundary {

    /**
     * Prepare data for initial game detail: initial board, time
     */
    void initializeGameDetailView(SelectGameOutputData selectGameOutputData);

    /**
     * Prepare error view if something went wrong
     */
    void prepareErrorView(String errorMessage);
}
