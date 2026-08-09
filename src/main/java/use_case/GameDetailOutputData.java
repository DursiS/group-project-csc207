package use_case;

import entity.GameState;

/**
 * @param currentStateNumber the number of the current game state, 0 for the initial state
 * @param gameState the current game state
 * @param hasPrevious whether there is a previous move
 * @param hasNext whether there is a next move
 *
 */
public record GameDetailOutputData(int currentStateNumber, GameState gameState, boolean hasPrevious,
                                   boolean hasNext) {

}
