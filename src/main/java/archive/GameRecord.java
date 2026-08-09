package archive;

import MakeMove.GameState;

import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * An entity that stores information of the entire game
 */
public class GameRecord {

    private final UUID id;
    private final ArrayList<GameState> history; // list of game states after each move in order
    private final String timeCreated;

    private boolean isCompleted;

    // "Black/White wins (checkmate)", "Black/White wins (time)", "Draw", or "In progress"
    private String gameResult;

    public GameRecord(GameState initialGameState) {
        id = UUID.randomUUID();
        history = new ArrayList<>();
        history.add(initialGameState);
        timeCreated = LocalDateTime.now().toString();
        isCompleted = false;
        gameResult = "In progress";
    }

    public GameRecord(UUID id, ArrayList<GameState> history, String timeCreated,
                      boolean isCompleted, String gameResult) {
        this.id = id;
        this.history = history;
        this.timeCreated = timeCreated;
        this.isCompleted = isCompleted;
        this.gameResult = gameResult;
    }

    // update the game record by appending a game state
    // Precondition: The new game state must be attainable from the last game state
    // (needs to be verified before)
    public void updateGameRecord(GameState state) {
        history.add(state);
    }

    public void endGame(String result) {
        isCompleted = true;
        gameResult = result;
    }

    public UUID getId() {
        return id;
    }

    public ArrayList<GameState> getHistory() {
        return history;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public String getGameResult() {
        return gameResult;
    }
}
