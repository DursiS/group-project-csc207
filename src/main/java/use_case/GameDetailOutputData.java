package use_case;

import entity.GameState;

import java.util.UUID;

/**
 * @param gameId the id of the game
 * @param currentStateNumber the number of the current game state, 0 for the initial state
 * @param gameState the current game state
 * @param gameResult the final result of the game
 * @param hasPrevious whether there is a previous move
 * @param hasNext whether there is a next move
 *
 */
public record GameDetailOutputData(UUID gameId, int currentStateNumber, GameState gameState,
                                   String gameResult, boolean hasPrevious, boolean hasNext) {

}
