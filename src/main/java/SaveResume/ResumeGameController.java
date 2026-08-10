package SaveResume;

import MakeMove.GameState;

import java.util.ArrayList;

public class ResumeGameController {
    private ResumeGameInputBoundary interactor;

    public ResumeGameController(ResumeGameInputBoundary interactor){
        this.interactor = interactor;
    }

    public GameState execute(String saveName) {
        ResumeGameInputData inputData = new ResumeGameInputData(saveName);
        return interactor.execute(inputData);
    }

    public ArrayList<String> getSaveNames() {
        return interactor.getSaveNameList();
    }

}
