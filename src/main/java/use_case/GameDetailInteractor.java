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
                    gameState, null);
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
                        gameState, null);
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

                GameDetailOutputData gameDetailOutputData;

                // if the next state is the final state, show the game result
                if (current == game.getHistory().size() - 1) {
                    gameDetailOutputData = new GameDetailOutputData(id, current, gameState,
                            game.getGameResult());
                }
                else {
                    gameDetailOutputData = new GameDetailOutputData(id, current, gameState, null);
                }

                gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
            }
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("An error occurred while loading the move: "
                    + e.getMessage());
        }
    }
}
