package use_case;

import data_access.GameDataAccessObject;
import entity.GameRecord;
import entity.GameState;

import java.util.UUID;

public class GameDetailInteractor implements GameDetailInputBoundary{

    private final GameDataAccessObject gameDataAccessObject;
    private final GameDetailOutputBoundary gameDetailPresenter;

    public GameDetailInteractor(GameDataAccessObject gameDataAccessObject,
                                GameDetailOutputBoundary gameDetailPresenter) {
        this.gameDataAccessObject = gameDataAccessObject;
        this.gameDetailPresenter = gameDetailPresenter;
    }

    @Override
    public void getGame(GameDetailInputData gameDetailInputData) {
        try {
            UUID id = gameDetailInputData.getId();
            GameRecord game = gameDataAccessObject.load(id);
            GameState gameState = game.getHistory().get(0);
            GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(id, 0, 
                    gameState, game.getGameResult(), false, game.getHistory().size() > 1);
            gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("Could not load the game: " + e.getMessage());
        }
    }

    @Override
    public void back(GameDetailInputData gameDetailInputData) {
        try {
            UUID id = gameDetailInputData.getId();
            int current = gameDetailInputData.getCurrentStateNumber();
            GameRecord game = gameDataAccessObject.load(id);
            if (current > 0) {
                current--;
                GameState gameState = game.getHistory().get(current);
                GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(id, current,
                        gameState, game.getGameResult(), current > 0, true);
                gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
            }
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("An error occurred while loading the  move: "
                    + e.getMessage());
        }
    }

    @Override
    public void forward(GameDetailInputData gameDetailInputData) {
        try {
            UUID id = gameDetailInputData.getId();
            int current = gameDetailInputData.getCurrentStateNumber();
            GameRecord game = gameDataAccessObject.load(id);
            if (current <  game.getHistory().size() - 1) {
                current++;
                GameState gameState = game.getHistory().get(current);
                GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(id, current,
                        gameState, game.getGameResult(), true,
                        current < game.getHistory().size() - 1);
                gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
            }
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("An error occurred while loading the move: "
                    + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
