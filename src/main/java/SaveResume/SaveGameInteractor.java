package SaveResume;

import MakeMove.GameState;


public class SaveGameInteractor implements SaveGameInputBoundary {
    public static final String AUTOSAVE_NAME = "autosave";
    private GameDataAccess gameDataAccess;
    private SaveGameOutputBoundary presenter;
    private String currentSaveName;

    public SaveGameInteractor(GameDataAccess gameDataAccess, SaveGameOutputBoundary presenter) {

        this.gameDataAccess = gameDataAccess;
        this.presenter = presenter;
        this.currentSaveName = null;
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

        if (!AUTOSAVE_NAME.equals(saveName)) {
            this.currentSaveName = saveName;
        }

        SaveGameOutputData outputData = new SaveGameOutputData(saveName);

        presenter.prepareSuccessSaveView(outputData);

    }

    @Override
    public void autosave(GameState gameState) {

        String saveName = AUTOSAVE_NAME;

        if (currentSaveName != null) {
            saveName = currentSaveName;
        }

        SaveGameInputData inputData = new SaveGameInputData(
                        saveName,
                        gameState,
                        true);

        execute(inputData);
    }

    @Override
    public void setCurrentSaveName(String saveName) {
        this.currentSaveName = saveName;
    }

    private String generateSaveName() {
        int i = 1;
        while(gameDataAccess.saveExists("save" + i)) {
            i++;
        }
        return "save" + i;
    }
}