/*
Increases a given player's clock in a given GameState
 */
package Timer;

import MakeMove.GameState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ClockInteractorManager implements ClockInputBoundary, PropertyChangeListener {
    private static final int S_TO_MS = 1000;
    private final ClockInteractor black;
    private final ClockInteractor white;
    private final GameState gameState;
    private final int increment;

    public ClockInteractorManager(ClockInteractor black, ClockInteractor white, GameState gameState){
        this.gameState = gameState;
        this.increment = gameState.getIncrement();
        this.black = black;
        this.white = white;
    }

    public void start(){
        white.start();
        black.start();

        if(gameState.getBoard().getTurn() % 2 == 0) { // White Turn
            white.unpause();
        }
        else{
            black.unpause();
        }
    }

    public void stop(){
        black.pause();
        white.pause();
    }

    /**
     * Receives an update signal and changes the active clock off the UI thread.
     * Updates reference to the latest game state from MakeMoveInteractor
     * @param propertyChangeEvent the fired update event
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if ("update-analysis".equals(propertyChangeEvent.getPropertyName())) {
            changeTurn();
        }
    }

    public void changeTurn(){
        if (gameState.getBoard().getTurn() % 2 == 0){ // White Turn
            black.pause();
            white.unpause();
            gameState.setBlackMilliSec(gameState.getBlackMilliSec() + increment * S_TO_MS);
        }
        else {
            white.pause();
            black.unpause();
            gameState.setWhiteMilliSec(gameState.getWhiteMilliSec() + increment * S_TO_MS);
        }
    }
}
