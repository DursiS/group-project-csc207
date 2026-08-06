package interface_adapter;
import entity.GameState;
import use_case.SaveGameInputBoundary;
import use_case.SaveGameInputData;
public class SaveGameController {
    private SaveGameInputBoundary interactor;

    public SaveGameController(SaveGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String saveName, GameState gameState){
        SaveGameInputData inputData = new SaveGameInputData(saveName, gameState);
        interactor.execute(saveName, gameState);
    }
}
