package interface_adapter;
import use_case.ResumeGameInputData;
import use_case.ResumeGameInputBoundary;
import entity.GameState;
public class ResumeGameController {
    private ResumeGameInputBoundary interactor;

    public ResumeGameController(ResumeGameInputBoundary interactor){
        this.interactor = interactor;
    }

    public GameState execute(String saveName) {
        ResumeGameInputData inputData = new ResumeGameInputData(saveName);
        return interactor.execute(inputData);
    }
}
