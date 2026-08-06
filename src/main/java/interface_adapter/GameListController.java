package interface_adapter;

import use_case.GameListInputBoundary;

public class GameListController {

    private final GameListInputBoundary gameListInteractor;

    public GameListController(GameListInputBoundary gameListInteractor) {
        this.gameListInteractor = gameListInteractor;
    }

    public void getGameList() {
        gameListInteractor.getGameList();
    }
}
