package use_case;

import java.util.ArrayList;

import entity.GameState;

public interface ResumeGameInputBoundary {

    GameState execute(ResumeGameInputData inputData);

    GameState recoverAutosave();

    ArrayList<String> getSaveNameList();
}