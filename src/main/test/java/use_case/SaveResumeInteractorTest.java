package use_case;

import data_access.InMemoryGameDataAccessObject;
import entity.Board;
import entity.BoardStateList;
import entity.GameState;

import interface_adapter.SaveGameViewModel;
import interface_adapter.SaveGamePresenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveResumeInteractorTest {
    private SaveGameViewModel saveGameViewModel;
    private GameDataAccess gameDataAccess;
    private SaveGameInputBoundary saveInteractor;
    private ResumeGameInputBoundary resumeInteractor;
    private GameState gameState;
    private SaveGameOutputBoundary savePresenter;
    private String saveName;

    @BeforeEach
    void setUp(){

        saveName = new String("save1");
        gameDataAccess = new InMemoryGameDataAccessObject();
        saveGameViewModel = new SaveGameViewModel();
        savePresenter = new SaveGamePresenter(saveGameViewModel);
        saveInteractor = new SaveGameInteractor(gameDataAccess, savePresenter);
        resumeInteractor = new ResumeGameInteractor(gameDataAccess);
        Board board = new Board();

        gameState = new GameState(
                board,
                300000,
                295000,
                "IN_PROCESS"
        );
    }

    @Test
    void saveAndResumeGame() {
        SaveGameInputData inputData = new SaveGameInputData(saveName, gameState);
        saveInteractor.execute(inputData);

        GameState loadedGame = resumeInteractor.execute(saveName);
        assertEquals(300000, loadedGame.getWhiteMilliSec());
        assertEquals(295000, loadedGame.getBlackMilliSec());
        assertEquals("IN_PROCESS", loadedGame.getGameResult());
        assertNotSame(gameState.getBoardCopy(), loadedGame.getBoardCopy());
    }
    @Test
    void loadingMissingSaveThrowexceptionTest(){
        assertThrows(IllegalArgumentException.class, () -> resumeInteractor.execute(saveName));
    }
    @Test
    void emptySaveNameTest(){
        SaveGameInputData inputData = new SaveGameInputData("", gameState);
        saveInteractor.execute(inputData);
        String errormessage = saveGameViewModel.getError();
        assertEquals("Save Name Cannot Be Empty!", errormessage);
    }

    @Test
    void autoSaveAndRecoverGameTest() {
        saveInteractor.autosave(gameState);
        GameState autosavedGame = resumeInteractor.recoverAutosave();
        assertNotNull(autosavedGame);
        assertTrue(gameDataAccess.saveExists(SaveGameInteractor.AUTOSAVE_NAME));
    }

}
