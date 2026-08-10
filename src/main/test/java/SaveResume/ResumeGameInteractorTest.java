package SaveResume;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResumeGameInteractorTest {
    private GameDataAccess gameDataAccess;
    private SaveGameInputBoundary saveInteractor;
    private ResumeGameInputBoundary resumeInteractor;
    private ResumeGameViewModel viewModel;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameDataAccess = new InMemoryGameDataAccessObject();

        SaveGameViewModel saveViewModel = new SaveGameViewModel();
        SaveGameOutputBoundary savePresenter = new SaveGamePresenter(saveViewModel);
        saveInteractor = new SaveGameInteractor(gameDataAccess, savePresenter);

        viewModel = new ResumeGameViewModel();
        ResumeGameOutputBoundary resumePresenter = new ResumeGamePresenter(viewModel);
        resumeInteractor = new ResumeGameInteractor(
                gameDataAccess,
                resumePresenter,
                saveInteractor
        );

        gameState = new GameState(
                new Board(),
                300000,
                295000,
                new BoardStateList(),
                "IN_PROCESS"
        );
    }

    @Test
    void saveAndResumeGameTest() {
        saveInteractor.execute(new SaveGameInputData("save1", gameState, false));

        GameState loadedGame = resumeInteractor.execute(new ResumeGameInputData("save1"));

        assertNotNull(loadedGame);
        assertEquals(300000, loadedGame.getWhiteMilliSec());
        assertEquals(295000, loadedGame.getBlackMilliSec());
        assertEquals("IN_PROCESS", loadedGame.getGameResult());
        assertNotSame(gameState.getBoardCopy(), loadedGame.getBoardCopy());
    }

    @Test
    void loadingMissingSaveTest() {
        GameState loadedGame = resumeInteractor.execute(new ResumeGameInputData("save2"));

        assertNull(loadedGame);
        assertEquals("Error: Save does not exist.", viewModel.getErrorMessage());
    }

    @Test
    void loadingEmptyNameTest() {
        GameState loadedGame = resumeInteractor.execute(new ResumeGameInputData(""));

        assertNull(loadedGame);
        assertEquals("Error: Save name cannot be empty.", viewModel.getErrorMessage());
    }

    @Test
    void getSaveNameListTest() {
        saveInteractor.execute(new SaveGameInputData("save1", gameState, false));

        assertTrue(resumeInteractor.getSaveNameList().contains("save1"));
    }

    @Test
    void recoverAutosaveTest() {
        saveInteractor.autosave(gameState);

        GameState autosavedGame = resumeInteractor.recoverAutosave();

        assertNotNull(autosavedGame);
        assertEquals(300000, autosavedGame.getWhiteMilliSec());
    }
}
