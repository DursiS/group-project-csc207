package use_case;

import entity.GameRecord;
import entity.GameState;

public class GameDetailInteractor implements GameDetailInputBoundary{

    private final GameDetailOutputBoundary gameDetailPresenter;

    public GameDetailInteractor(GameDetailOutputBoundary gameDetailPresenter) {
        this.gameDetailPresenter = gameDetailPresenter;
    }

    @Override
    public void back(GameDetailInputData gameDetailInputData) {
        try {
            GameRecord gameRecord = gameDetailInputData.gameRecord();
            int current = gameDetailInputData.currentStateNumber();
            if (current > 0) {
                current--;
                GameState gameState = gameRecord.getHistory().get(current);
                GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(current,
                        gameState, current > 0, true);
                gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
            }
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("An error occurred while loading the  move: "
                    + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void forward(GameDetailInputData gameDetailInputData) {
        try {
            GameRecord gameRecord = gameDetailInputData.gameRecord();
            int current = gameDetailInputData.currentStateNumber();
            if (current <  gameRecord.getHistory().size() - 1) {
                current++;
                GameState gameState = gameRecord.getHistory().get(current);
                GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(current,
                        gameState, true,  current < gameRecord.getHistory().size() - 1);
                gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
            }
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("An error occurred while loading the move: "
                    + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
