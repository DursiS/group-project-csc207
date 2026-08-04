package use_case;

import entity.GameRecord;

import java.util.UUID;

public interface ViewGameDataAccess {

    /**
     * Loads the game.
     * @param id the UUID of the game
     * @return the game with the given id
     */
    GameRecord load(UUID id);
}
