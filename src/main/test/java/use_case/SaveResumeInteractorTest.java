package use_case;

import data_access.InMemoryGameDataAccessObject;
import entity.Board;
import entity.BoardStateList;
import entity.GameState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveResumeInteractorTest {
    private GameDataAccess gameDataAccess;
    private SaveGameInputBoundary saveInteractor;
    private ResumeGameInputBoundary resumeInteractor;
    private GameState gameState;

    @BeforeEach
    void setUp(){
        gameDataAccess = new InMemoryGameDataAccessObject();
        saveInteractor = new SaveGameInteractor(gameDataAccess);
        resumeInteractor = new ResumeGameInteractor(gameDataAccess);
        Board board = new Board();

        BoardStateList history = new BoardStateList();

        history.addBoardCopy(board);

        gameState = new GameState(
                board,
                300000,
                295000,
                history,
                "IN_PROCESS"
        );
    }

    @Test
    void saveAndResumeGame() {
        saveInteractor.execute("save1", gameState);

        GameState loadedGame = resumeInteractor.execute("save1");
        assertEquals(300000, loadedGame.getWhiteMilliSec());
        assertEquals(295000, loadedGame.getBlackMilliSec());
        assertEquals("IN_PROCESS", loadedGame.getGameResult());
        assertEquals(1, loadedGame.getBoardStateListCopy().size());
        assertNotSame(gameState.getBoardCopy(), loadedGame.getBoardCopy());
    }
    @Test
    void loadingMissingSaveThrowexceptionTest(){
        assertThrows(IllegalArgumentException.class, () -> resumeInteractor.execute("save1"));
    }
    @Test
    void emptySaveNameThrowExceptiontest(){
        assertThrows(IllegalArgumentException.class, () -> saveInteractor.execute("",gameState));
    }

    @Test
    void autoSaveAndRecoverGameTest() {
        saveInteractor.autosave(gameState);
        GameState autosavedGame = resumeInteractor.recoverAutosave();

        assertTrue(gameDataAccess.saveExists(SaveGameInteractor.AUTOSAVE_NAME));
    }

}
