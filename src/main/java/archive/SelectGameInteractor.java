package archive;

import MakeMove.GameState;

public class SelectGameInteractor implements SelectGameInputBoundary{

    private final ViewGameDataAccess gameDataAccessObject;
    private final SelectGameOutputBoundary gameDetailPresenter;

    public SelectGameInteractor(ViewGameDataAccess gameDataAccessObject,
                                SelectGameOutputBoundary gameDetailPresenter) {
        this.gameDataAccessObject = gameDataAccessObject;
        this.gameDetailPresenter = gameDetailPresenter;
    }

    @Override
    public void selectGame(SelectGameInputData selectGameInputData) {
        try {
            GameRecord gameRecord = gameDataAccessObject.load(selectGameInputData.id());
            GameState gameState = gameRecord.getHistory().get(0);
            SelectGameOutputData selectGameOutputData = new SelectGameOutputData(gameRecord,
                    gameState, gameRecord.getGameResult(), gameRecord.getHistory().size() > 1);
            gameDetailPresenter.initializeGameDetailView(selectGameOutputData);
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("Could not load the game: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
