package archive;

public interface GameDetailInputBoundary {

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
