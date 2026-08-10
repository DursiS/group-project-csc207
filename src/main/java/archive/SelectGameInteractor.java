package archive;

import MakeMove.GameState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SelectGameInteractor implements SelectGameInputBoundary{

    private final ViewGameDataAccess gameDataAccessObject;
    private final SelectGameOutputBoundary gameDetailPresenter;
    private static final String UPDATE_CHANNEL = "update-analysis";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public SelectGameInteractor(ViewGameDataAccess gameDataAccessObject,
                                SelectGameOutputBoundary gameDetailPresenter) {
        this.gameDataAccessObject = gameDataAccessObject;
        this.gameDetailPresenter = gameDetailPresenter;
    }

    @Override
    public void selectGame(SelectGameInputData selectGameInputData) {
        try {
            GameRecord gameRecord = gameDataAccessObject.load(selectGameInputData.id());
            GameState gameState = gameRecord.getHistory().get(0);
            SelectGameOutputData selectGameOutputData;
            selectGameOutputData = new SelectGameOutputData(gameRecord, gameState,
                        gameRecord.getGameResult(), gameRecord.getHistory().size() > 1);
            updateAnalyzeMoveInteractor(gameState);
            gameDetailPresenter.initializeGameDetailView(selectGameOutputData);
        } catch (RuntimeException e) {
            gameDetailPresenter.prepareErrorView("Could not load the game: " + e.getMessage());
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
     * Add a property change listener
     * @param listener a property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
