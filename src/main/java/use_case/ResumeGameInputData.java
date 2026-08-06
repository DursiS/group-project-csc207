package use_case;
import entity.GameState;
public class ResumeGameInputData {
    private String saveName;
    private GameState gameState;

    public ResumeGameInputData(String saveName, GameState gameState){
        this.saveName = saveName;
        this.gameState = gameState;
    }

    public String getsaveName() {
        return saveName;
    }

    public GameState getGameState() {
        return gameState;
    }
}
