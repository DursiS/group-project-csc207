package use_case;

import java.util.UUID;

public class GameDetailInputData {

    private final UUID id; // id of the game
    private final int currentStateNumber; // the current state number

    public GameDetailInputData(UUID id, int currentStateNumber) {
        this.id = id;
        this.currentStateNumber = currentStateNumber;
    }

    public UUID getId() {
        return id;
    }

    public int getCurrentStateNumber() {
        return currentStateNumber;
    }
}
