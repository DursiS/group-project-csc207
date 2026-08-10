/*
Increases a given player's clock in a given GameState
 */
package Timer;

import MakeMove.GameState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ClockInteractorManager implements ClockInputBoundary, PropertyChangeListener {
    private ClockInteractor black;
    private ClockInteractor white;
    private GameState gameState;

    public ClockInteractorManager(ClockInteractor black, ClockInteractor white, GameState gameState){
        this.gameState = gameState;
        this.black = black;
        this.white = white;
    }

    public void start(){
        start();
    }

    public void stop(){

    }

    /**
     * Receives an update signal and changes the active clock off the UI thread.
     * Updates reference to the latest game state from MakeMoveInteractor
     * @param propertyChangeEvent the fired update event
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (UPDATE_CHANNEL.equals(propertyChangeEvent.getPropertyName())) {
            this.gameState = (GameState) propertyChangeEvent.getNewValue();
            runAnalysisAsync();
        }
    }

    public void changeTurn(){

    }
}
