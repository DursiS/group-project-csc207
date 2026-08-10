/*
Increases the player times over time for a given GameState
 */
package Timer;

import MakeMove.GameState;

import javax.swing.*;

public class ClockInteractor extends Thread{
    private static final int UPDATE_INTERVAL = 10;
    private final GameState gameState;
    private boolean running = false;
    private boolean stop = false;
    private final boolean white;
    private final ClockOutputBoundary output;

    public ClockInteractor(GameState gameState, boolean white, ClockOutputBoundary output){
        this.gameState = gameState;
        this.white = white;
        this.output = output;
    }

    public void run(){
        long lastTime;
        long thisTime;
        int count = 0;
        while (!stop) {
            lastTime = System.currentTimeMillis();
            while (running) {
                try {
                    sleep(1);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }

                thisTime = System.currentTimeMillis();

                if (white) { //White Turn
                    gameState.setWhiteMilliSec(gameState.getWhiteMilliSec() - (int) (thisTime - lastTime));
                } else {
                    gameState.setBlackMilliSec(gameState.getBlackMilliSec() - (int) (thisTime - lastTime));
                }

                lastTime = thisTime;

                count++;
                if (count >= UPDATE_INTERVAL) {
                    updateOutput();
                    count = 0;
                }

                if (gameState.getBlackMilliSec() <= 0) {
                    gameState.setBlackMilliSec(0);
                    JOptionPane.showMessageDialog(null, "BLACK RAN OUT OF TIME! WHITE WINS!",
                            "TIME OUT!", JOptionPane.INFORMATION_MESSAGE);
                    pause();
                }

                if (gameState.getWhiteMilliSec() <= 0) {
                    gameState.setWhiteMilliSec(0);
                    JOptionPane.showMessageDialog(null, "WHITE RAN OUT OF TIME! BLACK WINS!",
                            "TIME OUT!", JOptionPane.INFORMATION_MESSAGE);
                    pause();
                }
            }
            updateOutput();
            while (!running){
                try {
                    sleep(1);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
        }
    }

    private void updateOutput(){
            if(white) {
                output.updateTime(gameState.getWhiteMilliSec());
            }
            else{
                output.updateTime(gameState.getBlackMilliSec());
            }
    }

    public void pause(){
        running = false;
    }

    public void unpause(){
        running = true;
    }

    public void stop(){
        stop = true;
    }
}
