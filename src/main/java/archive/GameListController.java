package archive;

public class GameListController {

    private final GameListInputBoundary gameListInteractor;
    private final SelectGameInputBoundary selectGameInteractor;

    public GameListController(GameListInputBoundary gameListInteractor,
                              SelectGameInputBoundary selectGameInteractor) {
        this.gameListInteractor = gameListInteractor;
        this.selectGameInteractor = selectGameInteractor;
    }

    public void getGameList() {
        gameListInteractor.getGameList();
    }

    public void selectGame(SelectGameInputData selectGameInputData) {
        selectGameInteractor.selectGame(selectGameInputData);
    }
}
