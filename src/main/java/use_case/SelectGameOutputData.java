package use_case;

import entity.GameRecord;
import entity.GameState;

public record SelectGameOutputData(GameRecord gameRecord, GameState gameState, String gameResult,
                                   boolean hasNext){
}
