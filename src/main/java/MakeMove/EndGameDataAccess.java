package MakeMove;

import archive.GameRecord;

public interface EndGameDataAccess {

    /**
     * Saves the game.
     * @param game the game to save
     */
    void save(GameRecord game);
}
