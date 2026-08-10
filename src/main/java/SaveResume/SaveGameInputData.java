package SaveResume;

import MakeMove.GameState;
public class SaveGameInputData {
    private String saveName;
    private GameState gameState;
    private boolean overwrite;

    public SaveGameInputData(String saveName, GameState gameState, boolean overwrite) {
        this.saveName = saveName;
        this.gameState = gameState;
        this.overwrite = overwrite;
    }

    public String getSaveName() {
        return saveName;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean getOverwrite() {return overwrite;}
}
