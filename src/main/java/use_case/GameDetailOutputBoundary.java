package use_case;

public interface GameDetailOutputBoundary {

    /**
     * Prepare data for game detail: current board, time remaining
     */
    void prepareGameDetailView(GameDetailOutputData gameDetailOutputData);

    /**
     * Prepare error view if something went wrong
     */
    void prepareErrorView(String errorMessage);
}
