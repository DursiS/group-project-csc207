package use_case;
import entity.GameState;
public class SaveGameInputData {
    private String saveName;
    private GameState gameState;

    public SaveGameInputData(String saveName, GameState gameState) {
        this.saveName = saveName;
        this.gameState = gameState;
    }

    public String getSaveName() {
        return saveName;
    }

    public GameState getGameState() {
        return gameState;
    }
}
