package SaveResume;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryGameDataAccessObjectTest {
    private GameDataAccess gameDataAccess;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameDataAccess = new InMemoryGameDataAccessObject();
        gameState = new GameState(
                new Board(),
                300000,
                295000,
                new BoardStateList(),
                "IN_PROCESS"
        );
    }

    @Test
    void saveAndLoadTest() {
        gameDataAccess.saveGame("save1", gameState);

        GameState loadedGame = gameDataAccess.loadGame("save1");

        assertEquals(300000, loadedGame.getWhiteMilliSec());
        assertNotSame(gameState, loadedGame);
    }

    @Test
    void getSaveNamesTest() {
        gameDataAccess.saveGame("save1", gameState);

        assertTrue(gameDataAccess.getSaveNames().contains("save1"));
    }

    @Test
    void loadMissingSaveTest() {
        assertThrows(IllegalArgumentException.class,
                () -> gameDataAccess.loadGame("save1"));
    }
}
