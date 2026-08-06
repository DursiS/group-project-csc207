package use_case;

public interface GameListOutputBoundary {

    /**
     * Prepare data for the game list view
     * @param gameListOutputData the output data
     */
    void prepareSuccessView(GameListOutputData gameListOutputData);

    /**
     * Prepare the failed view
     * @param errorMessage the error message
     */
    void prepareFailedView(String errorMessage);
}
