package use_case;

import entity.GameRecord;

/**
 * @param gameRecord the game
 * @param currentStateNumber the current state number
 */
public record GameDetailInputData(GameRecord gameRecord, int currentStateNumber) {

}
