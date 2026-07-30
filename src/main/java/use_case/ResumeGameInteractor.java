package use_case;
import entity.GameState;

public class ResumeGameInteractor
        implements ResumeGameInputBoundary {

    private GameDataAccess gameDataAccess;

    public ResumeGameInteractor(
            GameDataAccess gameDataAccess) {
        this.gameDataAccess = gameDataAccess;
    }
    @Override
    public GameState execute(String saveName) {
        if (saveName == null
                || saveName.trim().equals("")) {

            throw new IllegalArgumentException(
                    "Save name cannot be empty."
            );
        }

        if (!gameDataAccess.saveExists(saveName)) {
            throw new IllegalArgumentException(
                    "Save does not exist."
            );
        }
        return gameDataAccess.loadGame(saveName);
    }
    @Override
    public GameState recoverAutosave() {
        return execute(
                SaveGameInteractor.AUTOSAVE_NAME
        );
    }
}