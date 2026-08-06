package Analysis;

import java.util.ArrayList;

public interface GameDataAccess {
    /**
     * Saves a game under the given name.
     * @param saveName the name to save under
     * @param gameState the game state to save
     */
    void saveGame(String saveName, GameState gameState);

    /**
     * Loads a saved game by name.
     * @param saveName the name to load
     * @return the loaded game state
     */
    GameState loadGame(String saveName);

    /**
     * Checks whether a save exists.
     * @param saveName the name to check
     * @return true if a save with that name exists
     */
    boolean saveExists(String saveName);

    /**
     * Returns all save names.
     * @return the list of save names
     */
    ArrayList<String> getSaveNames();
}
