package data_access;

import entity.Board;
import entity.BoardStateList;
import entity.GameRecord;
import entity.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataAccessObjectTest {

    @Test
    void saveGameTest() {
        GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
        Board board0 = new Board();
        GameState state0 = new GameState(board0, 600000, 600000,
                new BoardStateList(), "");
        GameRecord gameRecord =  new GameRecord(state0);

        int[][] squares1 = new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1,-1,-1,-1,-1,-1},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0},
                {1,1,1,1,0,1,1,1},
                {4,6,7,8,9,7,6,4}
        };
        Board board1 = new Board(squares1, 0, 0, 0);
        GameState state1 = new GameState(board1, 10000, 60000,
                new BoardStateList(), "");
        gameRecord.updateGameRecord(state1);

        int[][] squares2 = new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,0,-1,-1,-1,-1,-1},
                {0,0,0,0,0,0,0,0},
                {0,0,0,-1,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0},
                {1,1,1,1,0,1,1,1},
                {4,6,7,8,9,7,6,4}
        };
        Board board2 = new Board(squares2, 0, 0, 0);
        GameState state2 = new GameState(board2, 10000, 30000,
                new BoardStateList(), "");
        gameRecord.updateGameRecord(state2);

        gameRecord.endGame("Black Wins (Time)");

        gameDataAccessObject.save(gameRecord);

        //check double saving
        gameDataAccessObject.save(gameRecord);
    }

    @Test
    void loadGameTest() {
        GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
        Board board0 = new Board();
        GameState state0 = new GameState(board0, 600000, 600000,
                new BoardStateList(), "");
        GameRecord gameRecord =  new GameRecord(state0);

        int[][] squares1 = new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1,-1,-1,-1,-1,-1},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0},
                {1,1,1,1,0,1,1,1},
                {4,6,7,8,9,7,6,4}
        };
        Board board1 = new Board(squares1, 0, 0, 0);
        GameState state1 = new GameState(board1, 10000, 60000,
                new BoardStateList(), "");
        gameRecord.updateGameRecord(state1);

        int[][] squares2 = new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,0,-1,-1,-1,-1,-1},
                {0,0,0,0,0,0,0,0},
                {0,0,0,-1,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,1,0,0,0},
                {1,1,1,1,0,1,1,1},
                {4,6,7,8,9,7,6,4}
        };
        Board board2 = new Board(squares2, 0, 0, 0);
        GameState state2 = new GameState(board2, 10000, 30000,
                new BoardStateList(), "");
        gameRecord.updateGameRecord(state2);

        gameRecord.endGame("Black Wins (Time)");
        gameDataAccessObject.save(gameRecord);

        GameRecord newGameRecord = gameDataAccessObject.load(gameRecord.getUuid());
        assertEquals(newGameRecord.getUuid(), gameRecord.getUuid());
        assertEquals(newGameRecord.getGameResult(), gameRecord.getGameResult());

        // time will be mutated after the SQL conversion, so check manually:
//        assertEquals(newGameRecord.getTimeCreated(), gameRecord.getTimeCreated());
        System.out.println(gameRecord.getTimeCreated());
        System.out.println(newGameRecord.getTimeCreated());

        assertEquals(newGameRecord.getHistory().size(), gameRecord.getHistory().size());
        assertEquals(newGameRecord.getHistory().get(0).getBoard().toString(),
                gameRecord.getHistory().get(0).getBoard().toString());
        assertEquals(newGameRecord.getHistory().get(1).getWhiteMilliSec(),
                gameRecord.getHistory().get(1).getWhiteMilliSec());
        assertEquals(newGameRecord.getHistory().get(2).getBlackMilliSec(),
                gameRecord.getHistory().get(2).getBlackMilliSec());
    }
}
