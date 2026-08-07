package use_case;
import entity.GameState;
public class ResumeGameOutputData {
    private GameState gameState;
    public ResumeGameOutputData(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
