package SaveResume;

import MakeMove.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class FileGameDataAccessObject implements GameDataAccess {
    private static final String DEFAULT_SAVE_FILE = "saved-games.json";

    private final File saveFile;
    private final Gson gson;
    private HashMap<String, GameState> savedGames;

    public FileGameDataAccessObject() {
        this(DEFAULT_SAVE_FILE);
    }

    public FileGameDataAccessObject(String fileName) {
        this.saveFile = new File(fileName);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.savedGames = new HashMap<String, GameState>();
        loadSavedGames();
    }

    private GameState copyGameState(GameState game) {
        return new GameState(game.getBoardCopy(),
                game.getWhiteMilliSec(),
                game.getBlackMilliSec(),
                game.getBoardStateListCopy(),
                game.getGameResult());
    }

    private void loadSavedGames() {
        if (!saveFile.exists()) {
            return;
        }

        try (FileReader reader = new FileReader(saveFile)) {
            Type saveMapType = new TypeToken<HashMap<String, GameState>>() { }.getType();
            HashMap<String, GameState> loadedGames = gson.fromJson(reader, saveMapType);

            if (loadedGames != null) {
                savedGames = loadedGames;
            }
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to load saved games", exception);
        }
    }

    private void writeSavedGames() {
        File parentFolder = saveFile.getParentFile();
        if (parentFolder != null) {
            parentFolder.mkdirs();
        }

        try (FileWriter writer = new FileWriter(saveFile)) {
            gson.toJson(savedGames, writer);
        }
        catch (IOException exception) {
            throw new RuntimeException("Failed to save games", exception);
        }
    }

    @Override
    public void saveGame(String saveName, GameState game) {
        savedGames.put(saveName, copyGameState(game));
        writeSavedGames();
    }

    @Override
    public GameState loadGame(String saveName) {
        if (!saveExists(saveName)) {
            throw new IllegalArgumentException("Save doesn't exist");
        }

        return copyGameState(savedGames.get(saveName));
    }

    @Override
    public boolean saveExists(String saveName) {
        return savedGames.containsKey(saveName);
    }

    @Override
    public ArrayList<String> getSaveNames() {
        return new ArrayList<String>(savedGames.keySet());
    }
}
