package use_case;

import java.util.List;

public class GameListInteractor implements GameListInputBoundary {

    private final GameListDataAccess gameListDataAccess;
    private final GameListOutputBoundary gameListOutputBoundary;

    public GameListInteractor(GameListDataAccess gameListDataAccess,
                              GameListOutputBoundary gameListOutputBoundary) {
        this.gameListDataAccess = gameListDataAccess;
        this.gameListOutputBoundary = gameListOutputBoundary;
    }

    @Override
    public void getGameList() {
        try {
            List<GameSummary> gameSummaries = gameListDataAccess.browse();
            gameListOutputBoundary.prepareGameListView(new GameListOutputData(gameSummaries));
        } catch (RuntimeException e) {
            // This notifies the UI that something went wrong
            gameListOutputBoundary.prepareErrorView("Could not load games: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
