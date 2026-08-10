package archive;

public interface GameListOutputBoundary {

    /**
     * Prepare data for the game list view
     * @param gameListOutputData the output data
     */
    void prepareGameListView(GameListOutputData gameListOutputData);

    /**
     * Prepare the error view if something went wrong
     * @param errorMessage the error message
     */
    void prepareErrorView(String errorMessage);
}
