package SaveResume;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileGameDataAccessObjectTest {

    @TempDir
    Path tempFolder;

    @Test
    void savedGameStillExistsInNewDataAccessObjectTest() {
        String fileName = tempFolder.resolve("test-saves.json").toString();

        Board board = new Board(1, 0);
        board.setTurn(3);
        board.setSquare(0, 0, 8);

        BoardStateList boardHistory = new BoardStateList();
        boardHistory.addBoardCopy(new Board());

        GameState gameState = new GameState(
                board,
                300000,
                295000,
                boardHistory,
                "IN_PROCESS"
        );

        GameDataAccess firstDataAccess = new FileGameDataAccessObject(fileName);
        firstDataAccess.saveGame("save1", gameState);

        GameDataAccess secondDataAccess = new FileGameDataAccessObject(fileName);
        GameState loadedGame = secondDataAccess.loadGame("save1");

        assertTrue(secondDataAccess.saveExists("save1"));
        assertEquals(300000, loadedGame.getWhiteMilliSec());
        assertEquals(295000, loadedGame.getBlackMilliSec());
        assertEquals("IN_PROCESS", loadedGame.getGameResult());
        assertEquals(3, loadedGame.getBoard().getTurn());
        assertEquals(8, loadedGame.getBoard().getSquare(0, 0));
        assertEquals(1, loadedGame.getBoard().getVerticalEdgeType());
        assertEquals(1, loadedGame.getBoardStateList().size());
    }
}
