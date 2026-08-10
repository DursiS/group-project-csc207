/*
Increases the player times over time for a given GameState
 */
package Timer;

import MakeMove.GameState;

import java.beans.PropertyChangeListener;

public class ClockInteractor extends Thread implements PropertyChangeListener {
    private final GameState gameState;
    private final int increment;
    private boolean running = false;
    private final boolean white;

    public ClockInteractor(GameState gameState, boolean white){
        this.gameState = gameState;
        this.increment = gameState.getIncrement();
        this.white = white;
    }

    public void run(){
        long lastTime = System.currentTimeMillis();
        long thisTime;
        running = true;
        int count = 0;
        while(running){
            try {
                wait(1);
            }
            catch (InterruptedException exception){
                throw new RuntimeException(exception);
            }

            thisTime = System.currentTimeMillis();

            if(white) { //White Turn
                gameState.setWhiteMilliSec(gameState.getWhiteMilliSec() - (int)(thisTime - lastTime));
            }
            else{
                gameState.setBlackMilliSec(gameState.getBlackMilliSec() - (int)(thisTime - lastTime));
            }

            lastTime = thisTime;

            count ++;
            if(count >= 100){
                //TODO Shout that value has changed
                count = 0;
            }
        }
        if(white) { //White Turn
            gameState.setWhiteMilliSec(gameState.getWhiteMilliSec() + increment);
        }
        else{
            gameState.setBlackMilliSec(gameState.getBlackMilliSec() + increment);
        }
        //TODO Shout that the value has changed
    }

    public void pause(){
        running = false;
    }
}
