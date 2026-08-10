package SaveResume;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveResumeControllerTest {
    private GameDataAccess gameDataAccess;
    private SaveGameController saveController;
    private ResumeGameController resumeController;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameDataAccess = new InMemoryGameDataAccessObject();

        SaveGameViewModel saveViewModel = new SaveGameViewModel();
        SaveGameInputBoundary saveInteractor = new SaveGameInteractor(
                gameDataAccess,
                new SaveGamePresenter(saveViewModel)
        );
        saveController = new SaveGameController(saveInteractor);

        ResumeGameViewModel resumeViewModel = new ResumeGameViewModel();
        ResumeGameInputBoundary resumeInteractor = new ResumeGameInteractor(
                gameDataAccess,
                new ResumeGamePresenter(resumeViewModel),
                saveInteractor
        );
        resumeController = new ResumeGameController(resumeInteractor);

        gameState = new GameState(
                new Board(),
                300000,
                295000,
                new BoardStateList(),
                "IN_PROCESS"
        );
    }

    @Test
    void saveControllerTest() {
        saveController.execute("save1", gameState);

        assertTrue(gameDataAccess.saveExists("save1"));
    }

    @Test
    void resumeControllerTest() {
        saveController.execute("save1", gameState);

        GameState loadedGame = resumeController.execute("save1");

        assertNotNull(loadedGame);
        assertEquals(300000, loadedGame.getWhiteMilliSec());
    }

    @Test
    void getSaveNamesTest() {
        saveController.execute("save1", gameState);

        assertTrue(resumeController.getSaveNames().contains("save1"));
    }
}
