package Analysis;

public interface GameStateDataAccessInterface {
    /**
     * Returns the most recent board state.
     * @return the current board
     */
    Board getRecentBoard();
}
