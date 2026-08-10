package archive;

import java.util.List;

public class GameListInteractor implements GameListInputBoundary {

    private final GameListDataAccess gameDataAccessObject;
    private final GameListOutputBoundary gameListPresenter;

    public GameListInteractor(GameListDataAccess gameListDataAccess,
                              GameListOutputBoundary gameListOutputBoundary) {
        this.gameDataAccessObject = gameListDataAccess;
        this.gameListPresenter = gameListOutputBoundary;
    }

    @Override
    public void getGameList() {
        try {
            List<GameSummary> gameSummaries = gameDataAccessObject.browse();
            gameListPresenter.prepareGameListView(new GameListOutputData(gameSummaries));
        } catch (RuntimeException e) {
            // This notifies the UI that something went wrong
            gameListPresenter.prepareErrorView("Could not load games: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
