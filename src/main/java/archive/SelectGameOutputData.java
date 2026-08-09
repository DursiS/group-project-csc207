package archive;

import MakeMove.GameState;

public record SelectGameOutputData(GameRecord gameRecord, GameState gameState, String gameResult,
                                   boolean hasNext){
}
