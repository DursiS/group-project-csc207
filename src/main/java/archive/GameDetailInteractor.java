package archive;

import MakeMove.GameState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameDetailInteractor implements GameDetailInputBoundary{

    private static final String UPDATE_CHANNEL = "update-analysis";
    private final GameDetailOutputBoundary gameDetailPresenter;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GameDetailInteractor(GameDetailOutputBoundary gameDetailPresenter) {
        this.gameDetailPresenter = gameDetailPresenter;
    }

    @Override
    public void back(GameDetailInputData gameDetailInputData) {
        try {
            GameRecord gameRecord = gameDetailInputData.gameRecord();
            int current = gameDetailInputData.currentStateNumber();
            if (current > 0 && current < gameRecord.getHistory().size()) {
                current--;
                GameState gameState = gameRecord.getHistory().get(current);
                GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(current,
                        gameState, current > 0, true);
                updateAnalyzeMoveInteractor(gameState);
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
            if (current >= 0 && current <  gameRecord.getHistory().size() - 1) {
                current++;
                GameState gameState = gameRecord.getHistory().get(current);
                GameDetailOutputData gameDetailOutputData = new GameDetailOutputData(current,
                        gameState, true,  current < gameRecord.getHistory().size() - 1);
                if (current != gameRecord.getHistory().size() - 1) {
                    updateAnalyzeMoveInteractor(gameState);
                }
                gameDetailPresenter.prepareGameDetailView(gameDetailOutputData);
            }
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("An error occurred while loading the move: "
                    + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the AnalyzeMoveInteractor's reference of GameState
     * by using an Observer Pattern to solve the problem of consistency.
     */
    private void updateAnalyzeMoveInteractor(GameState gameState) {
        support.firePropertyChange(UPDATE_CHANNEL, null, gameState);
    }

    /**
     * Add a change listener
     * @param listener a property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
