package SaveResume;

import MakeMove.GameState;

import java.util.ArrayList;

public class SaveGameController {
    private SaveGameInputBoundary interactor;

    public SaveGameController(SaveGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String saveName, GameState gameState){
        SaveGameInputData inputData = new SaveGameInputData(saveName, gameState, false);
        interactor.execute(inputData);
    }

    public void overwrite(String saveName, GameState gameState) {
        SaveGameInputData inputData = new SaveGameInputData(saveName, gameState, true);
        interactor.execute(inputData);
    }

}
