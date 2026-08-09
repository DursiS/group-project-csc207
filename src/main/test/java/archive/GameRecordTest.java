package archive;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameRecordTest {

    @Test
    public void gameRecordTest() {
        Board board = new Board();
        BoardStateList boardStateList = new BoardStateList();
        GameState gameState1 = new GameState(board, 60000, 60000, boardStateList, "");
        GameRecord gameRecord1 = new GameRecord(gameState1);

        assertNotNull(gameRecord1.getId());
        assertEquals(1, gameRecord1.getHistory().size());
        assertEquals(gameRecord1.getHistory().get(0), gameState1);
        assertNotNull(gameRecord1.getTimeCreated());
        assertFalse(gameRecord1.isCompleted());
        assertEquals("In progress", gameRecord1.getGameResult());

        GameRecord gameRecord2 = new GameRecord(gameRecord1.getId(),
                gameRecord1.getHistory(), gameRecord1.getTimeCreated(),
                gameRecord1.isCompleted(), gameRecord1.getGameResult());

        assertEquals(gameRecord1.getId(), gameRecord2.getId());
        assertEquals(1, gameRecord2.getHistory().size());
        assertEquals(gameRecord1.getHistory().get(0), gameRecord2.getHistory().get(0));
        assertEquals(gameRecord1.getTimeCreated(), gameRecord2.getTimeCreated());
        assertEquals(gameRecord1.isCompleted(), gameRecord2.isCompleted());
        assertEquals(gameRecord1.getGameResult(), gameRecord2.getGameResult());

        GameState gameState2 = new GameState(board, 0, 60000, boardStateList, "");
        gameRecord2.updateGameRecord(gameState2);
        assertEquals(2, gameRecord2.getHistory().size());
        assertEquals(gameRecord2.getHistory().get(0), gameState1);
        assertEquals(gameRecord2.getHistory().get(1), gameState2);

        gameRecord2.endGame("Black wins (time)");
        assertEquals("Black wins (time)", gameRecord2.getGameResult());
    }
}
