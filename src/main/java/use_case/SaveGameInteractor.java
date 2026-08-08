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
        boolean overwrite = inputData.getOverwrite();
        if (saveName == null || saveName.trim().equals("")) {
            saveName = generateSaveName();
        }

        if (gameDataAccess.saveExists(saveName) && !overwrite) {
            presenter.prepareOverwriteView("Save Already Exist, Do You Want To Overwrite Save?");
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

        SaveGameInputData inputData = new SaveGameInputData(AUTOSAVE_NAME, gameState, true);

        execute(inputData);
    }

    private String generateSaveName() {
        int i = 1;
        while(gameDataAccess.saveExists("save" + i)) {
            i++;
        }
        return "save" + i;
    }
}