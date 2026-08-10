package MakeMove;

import MakeMove.GameState;
import java.util.ArrayList;

public interface GameDataAccess {
    //save a game
    void saveGame(String saveName, GameState gameState);
    //load a saved game
    GameState loadGame(String saveName);

    //check if a save exists
    boolean saveExists(String saveName);

    //return all save names
    ArrayList<String> getSaveNames();


}
