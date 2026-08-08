package use_case;

import entity.GameState;

import java.util.UUID;

/**
 * @param gameId the id of the game
 * @param currentStateNumber the number of the current game state, 0 for the initial state
 * @param gameState the current game state
 */
public record GameDetailOutputData(UUID gameId, int currentStateNumber, GameState gameState) {

}
