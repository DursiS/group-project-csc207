package use_case;

import entity.GameState;

public class SaveGameInteractor implements SaveGameInputBoundary {
    public static final String AUTOSAVE_NAME = "autosave";
    private GameDataAccess gameDataAccess;

    public SaveGameInteractor(GameDataAccess gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }

    @Override
    public void execute(String saveName, GameState gameState) {
        if (saveName == null || saveName.trim().equals("")) {
            throw new IllegalArgumentException(
                    "Save name cannot be empty."
            );
        }

        if (gameState == null) {
            throw new IllegalArgumentException(
                    "Game state cannot be null."
            );
        }

        gameDataAccess.saveGame(saveName, gameState);
    }

    @Override
    public void autosave(GameState gameState) {
        execute(AUTOSAVE_NAME, gameState);
    }
}