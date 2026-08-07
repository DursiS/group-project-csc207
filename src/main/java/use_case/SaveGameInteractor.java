package use_case;

import entity.GameState;

public class SaveGameInteractor implements SaveGameInputBoundary {
    public static final String AUTOSAVE_NAME = "autosave";
    private GameDataAccess gameDataAccess;
    private SaveGameOutputBoundary presenter;

    public SaveGameInteractor(GameDataAccess gameDataAccess, SaveGameOutputBoundary presenter) {

        this.gameDataAccess = gameDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveGameInputData inputData) {
        String saveName = inputData.getSaveName();
        GameState gameState = inputData.getGameState();
        if (saveName == null || saveName.trim().equals("")) {
            presenter.prepareFailSaveView("Save Name Cannot Be Empty!");
            return;
        }

        if (gameState == null) {
            presenter.prepareFailSaveView("Game state cannot be null!");
            return;
        }

        gameDataAccess.saveGame(saveName, gameState);

        SaveGameOutputData outputData = new SaveGameOutputData(saveName);

        presenter.prepareSuccessSaveView(outputData);

    }

    @Override
    public void autosave(GameState gameState) {

        SaveGameInputData inputData = new SaveGameInputData(AUTOSAVE_NAME, gameState);

        execute(inputData);
    }
}