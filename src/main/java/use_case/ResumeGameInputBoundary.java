package use_case;

import entity.GameState;

public interface ResumeGameInputBoundary {

    GameState execute(String saveName);

    GameState recoverAutosave();
}