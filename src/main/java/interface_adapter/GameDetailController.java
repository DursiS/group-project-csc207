package interface_adapter;

import use_case.GameDetailInputBoundary;
import use_case.GameDetailInputData;

import java.util.UUID;

public class GameDetailController {

    private final GameDetailInputBoundary gameDetailInteractor;

    public GameDetailController(GameDetailInputBoundary gameDetailInteractor) {
        this.gameDetailInteractor = gameDetailInteractor;
    }

    public void getGame(UUID id) {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(id, 0);
        gameDetailInteractor.getGame(gameDetailInputData);
    }

    public void forward(UUID id, int current) {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(id, current);
        gameDetailInteractor.forward(gameDetailInputData);
    }

    public void back(UUID id, int current) {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(id, current);
        gameDetailInteractor.back(gameDetailInputData);
    }
}
