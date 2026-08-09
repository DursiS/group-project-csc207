package use_case;

import data_access.InMemoryGameDataAccessObject;
import entity.Board;
import entity.BoardStateList;
import entity.GameState;

import interface_adapter.SaveGameViewModel;
import interface_adapter.SaveGamePresenter;


import interface_adapter.ResumeGameViewModel;
import interface_adapter.ResumeGamePresenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveResumeInteractorTest {
    private SaveGameViewModel saveGameViewModel;
    private ResumeGameViewModel resumeGameViewModel;
    private GameDataAccess gameDataAccess;
    private SaveGameInputBoundary saveInteractor;
    private ResumeGameInputBoundary resumeInteractor;
    private GameState gameState;
    private SaveGameOutputBoundary savePresenter;
    private ResumeGameOutputBoundary resumePresenter;
    private String saveName;

    @BeforeEach
    void setUp(){

        saveName = new String("save1");
        gameDataAccess = new InMemoryGameDataAccessObject();
        saveGameViewModel = new SaveGameViewModel();
        savePresenter = new SaveGamePresenter(saveGameViewModel);
        saveInteractor = new SaveGameInteractor(gameDataAccess, savePresenter);

        resumeGameViewModel = new ResumeGameViewModel();
        resumePresenter = new ResumeGamePresenter(resumeGameViewModel);
        resumeInteractor = new ResumeGameInteractor(gameDataAccess, resumePresenter);
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
        SaveGameInputData SaveinputData = new SaveGameInputData(saveName, gameState);
        saveInteractor.execute(SaveinputData);
        ResumeGameInputData ResumeInputData = new ResumeGameInputData(saveName);

        GameState loadedGame = resumeInteractor.execute(ResumeInputData);
        assertEquals(300000, loadedGame.getWhiteMilliSec());
        assertEquals(295000, loadedGame.getBlackMilliSec());
        assertEquals("IN_PROCESS", loadedGame.getGameResult());
        assertNotSame(gameState.getBoardCopy(), loadedGame.getBoardCopy());
    }
    @Test
    void loadingMissingSaveTest(){
        ResumeGameInputData inputData = new ResumeGameInputData("save2");
        resumeInteractor.execute(inputData);
        String errormessage = resumeGameViewModel.getErrorMessage();
        assertEquals("Error: Save does not exist.", errormessage);
    }

    @Test
    void loadingNameEmptyTest() {
        ResumeGameInputData inputData = new ResumeGameInputData("");
        resumeInteractor.execute(inputData);
        String errormessage = resumeGameViewModel.getErrorMessage();
        assertEquals("Error: Save name cannot be empty.", errormessage);
    }
    @Test
    void automaticSaveNameTest(){
        SaveGameInputData inputData = new SaveGameInputData("", gameState);
        saveInteractor.execute(inputData);
        assertTrue(gameDataAccess.saveExists("save1"));
        SaveGameInputData inputData1 = new SaveGameInputData("", gameState);
        saveInteractor.execute(inputData1);
        assertTrue(gameDataAccess.saveExists("save2"));

    }

    @Test
    void autoSaveAndRecoverGameTest() {
        saveInteractor.autosave(gameState);
        GameState autosavedGame = resumeInteractor.recoverAutosave();
        assertNotNull(autosavedGame);
        assertTrue(gameDataAccess.saveExists(SaveGameInteractor.AUTOSAVE_NAME));
    }

}
