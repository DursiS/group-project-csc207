package use_case;

import entity.GameRecord;

import java.util.UUID;

public interface LeaderBoardGameDataAccess {

    /**
     * Loads the game.
     * @param id the UUID of the game
     */
    GameRecord load(UUID id);
}
