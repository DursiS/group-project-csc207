package entity;

import java.util.UUID;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class GameRecord {

    private final UUID uuid;
    private final ArrayList<GameState> history; // list of game states after each move in order
    private final String timeCreated;

    private boolean isCompleted;
    private String gameResult; // "White Wins", "Black Wins", "Draw", or "In Progress"

    public GameRecord(GameState initialGameState) {
        uuid = UUID.randomUUID();
        history = new ArrayList<>();
        history.add(initialGameState);
        timeCreated = OffsetDateTime.now().toString();
        isCompleted = false;
        gameResult = "In Progress";
    }

    public GameRecord(UUID id, ArrayList<GameState> history, String timeCreated,
                      boolean isCompleted, String gameResult) {
        this.uuid = id;
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

    public UUID getUuid() {
        return uuid;
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
