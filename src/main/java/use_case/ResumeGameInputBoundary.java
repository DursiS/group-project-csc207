package use_case;

import entity.GameState;

public interface ResumeGameInputBoundary {

    GameState execute(ResumeGameInputData inputData);

    GameState recoverAutosave();
}