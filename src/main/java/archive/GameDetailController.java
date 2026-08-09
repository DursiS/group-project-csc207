package archive;


public class GameDetailController {

    private final GameDetailInputBoundary gameDetailInteractor;

    public GameDetailController(GameDetailInputBoundary gameDetailInteractor) {
        this.gameDetailInteractor = gameDetailInteractor;
    }

    public void forward(GameRecord gameRecord, int current) {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(gameRecord, current);
        gameDetailInteractor.forward(gameDetailInputData);
    }

    public void back(GameRecord gameRecord, int current) {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(gameRecord, current);
        gameDetailInteractor.back(gameDetailInputData);
    }
}
