package entity;

import java.time.LocalTime;
import java.util.ArrayList;

public class GameRecord {

    private ArrayList<GameState> history;
    private final LocalTime time;

    public GameRecord(GameState initialGameState) {
        history = new ArrayList<>();
        history.add(initialGameState);
        time = LocalTime.now();
    }

    // update the game record by appending a game state
    // Precondition: The new game state must be attainable from the last game state
    // (needs to be verified before)
    public void updateGameRecord(GameState state) {
        history.add(state);
    }

    public ArrayList<GameState> getHistory() {
        return history;
    }
}
