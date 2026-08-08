package use_case;
import entity.GameState;

public class ResumeGameInteractor
        implements ResumeGameInputBoundary {

    private GameDataAccess gameDataAccess;
    private ResumeGameOutputBoundary presenter;

    public ResumeGameInteractor(
            GameDataAccess gameDataAccess, ResumeGameOutputBoundary presenter) {
        this.gameDataAccess = gameDataAccess;
        this.presenter = presenter;
    }
    @Override
    public GameState execute(ResumeGameInputData inputData) {
        String saveName = inputData.getSaveName();
        if (saveName == null
                || saveName.trim().equals("")) {

            presenter.prepareFailResumeView("Save name cannot be empty.");
            return null;

        }

        if (!gameDataAccess.saveExists(saveName)) {
            presenter.prepareFailResumeView("Save does not exist.");
            return null;
        }
        ResumeGameOutputData outputData = new ResumeGameOutputData(saveName);
        GameState gameState = gameDataAccess.loadGame(saveName);
        presenter.prepareSuccessResumeView(outputData);
        return gameState;
    }
    @Override
    public GameState recoverAutosave() {
        ResumeGameInputData recoverData = new ResumeGameInputData(SaveGameInteractor.AUTOSAVE_NAME);
        return execute(recoverData);
    }
}