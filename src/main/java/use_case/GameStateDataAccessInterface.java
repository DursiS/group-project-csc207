package use_case;

import entity.Board;

public interface GameStateDataAccessInterface {
    /**
     * Returns the most recent board state.
     * @return the current board
     */
    Board getRecentBoard();
}
