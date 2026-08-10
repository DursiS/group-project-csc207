package SaveResume;

import MakeMove.GameState;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryGameDataAccessObject implements GameDataAccess {

    private HashMap<String, GameState> savedGames;

    public InMemoryGameDataAccessObject() {
        this.savedGames = new HashMap<String, GameState>();
    }

    private GameState copyGameState(GameState game){
        GameState copyGame = new GameState(game.getBoardCopy(),
                game.getWhiteMilliSec(),
                game.getBlackMilliSec(),
                game.getBoardStateListCopy(),
                game.getGameResult());
        return copyGame;
    }

    @Override
    public void saveGame(String saveName, GameState game){
        GameState copyGameState = copyGameState(game);
        this.savedGames.put(saveName, copyGameState);
    }

    @Override
    public GameState loadGame(String saveName){
        if (!saveExists(saveName)) {
            throw new IllegalArgumentException(
                    "Save doesn't exist"
            );
        }
        GameState savedGame = savedGames.get(saveName);
        return(copyGameState(savedGame));
    }

    @Override
    public boolean saveExists(String saveName){
        return this.savedGames.containsKey(saveName);
    }

    @Override
    public ArrayList<String> getSaveNames(){
        ArrayList<String> saveNames = new ArrayList<String>();
        for (String saveName : savedGames.keySet()) {
            saveNames.add(saveName);
        }

        return saveNames;
    }
}
