package MakeMove;

import java.util.ArrayList;


public interface ResumeGameInputBoundary {

    GameState execute(ResumeGameInputData inputData);

    GameState recoverAutosave();

    ArrayList<String> getSaveNameList();
}