package use_case;

public interface GameDetailInputBoundary {

    /**
     * Get the past completed game given the id
     * @param gameDetailInputData the input data
     */
    void getGame(GameDetailInputData gameDetailInputData);

    /**
     * Goes to the previous state, given id of the game and the current state number
     * @param gameDetailInputData the input data
     */
    void back(GameDetailInputData gameDetailInputData);

    /**
     * Goes to the next state
     * @param gameDetailInputData the input data
     */
    void forward(GameDetailInputData gameDetailInputData);
}
