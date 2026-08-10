package MakeMove;

public interface SaveGameInputBoundary {

    //save a game with a choosen name
    void execute(SaveGameInputData inputData);

    //automatically save the current game
    void autosave(GameState gameState);

    void setCurrentSaveName(String saveName);
}
