package archive;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import app.ViewManagerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDetailInteractorTest {

    private GameRecord gameRecord;

    @BeforeEach
    void setUp() {
        Board board1 = new Board();
        BoardStateList boardStateList = new BoardStateList();
        GameState gameState1 = new GameState(board1, 60000, 60000, boardStateList, "");
        gameRecord = new GameRecord(gameState1);
        Board board2 = new Board();
        board2.setSquare(6, 0, 0);
        board2.setSquare(5, 1, 1);
        GameState gameState2 = new GameState(board2, 1, 60000, boardStateList, "");
        gameRecord.updateGameRecord(gameState2);
        Board board3 = new Board();
        board3.setSquare(6, 0, 0);
        board3.setSquare(5, 1, 1);
        board3.setSquare(1, 0, 0);
        board3.setSquare(2, 1, -1);
        GameState gameState3 = new GameState(board3, 0, 1, boardStateList, "");
        gameRecord.updateGameRecord(gameState3);
        gameRecord.endGame("Black wins (Time)");
    }

    @Test
    void back() {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(gameRecord, 2);
        GameDetailPresenter testPresenter = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareGameDetailView(GameDetailOutputData gameDetailOutputData) {
                assertEquals(1, gameDetailOutputData.currentStateNumber());
                assertEquals(gameRecord.getHistory().get(1), gameDetailOutputData.gameState());
                assertTrue(gameDetailOutputData.hasPrevious());
                assertTrue(gameDetailOutputData.hasNext());
            }
        };
        GameDetailInteractor testInteractor = new GameDetailInteractor(testPresenter);
        testInteractor.back(gameDetailInputData);

        GameDetailInputData gameDetailInputData2 = new GameDetailInputData(gameRecord, 1);
        GameDetailPresenter testPresenter2 = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareGameDetailView(GameDetailOutputData gameDetailOutputData) {
                assertEquals(0, gameDetailOutputData.currentStateNumber());
                assertEquals(gameRecord.getHistory().get(0), gameDetailOutputData.gameState());
                assertFalse(gameDetailOutputData.hasPrevious());
                assertTrue(gameDetailOutputData.hasNext());
            }
        };
        GameDetailInteractor testInteractor2 = new GameDetailInteractor(testPresenter2);
        testInteractor2.back(gameDetailInputData2);
    }

    @Test
    void forward() {
        GameDetailInputData gameDetailInputData = new GameDetailInputData(gameRecord, 0);
        GameDetailPresenter testPresenter = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareGameDetailView(GameDetailOutputData gameDetailOutputData) {
                assertEquals(1, gameDetailOutputData.currentStateNumber());
                assertEquals(gameRecord.getHistory().get(1), gameDetailOutputData.gameState());
                assertTrue(gameDetailOutputData.hasPrevious());
                assertTrue(gameDetailOutputData.hasNext());
            }
        };
        GameDetailInteractor testInteractor = new GameDetailInteractor(testPresenter);
        testInteractor.forward(gameDetailInputData);

        GameDetailInputData gameDetailInputData2 = new GameDetailInputData(gameRecord, 1);
        GameDetailPresenter testPresenter2 = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareGameDetailView(GameDetailOutputData gameDetailOutputData) {
                assertEquals(2, gameDetailOutputData.currentStateNumber());
                assertEquals(gameRecord.getHistory().get(2), gameDetailOutputData.gameState());
                assertTrue(gameDetailOutputData.hasPrevious());
                assertFalse(gameDetailOutputData.hasNext());
            }
        };
        GameDetailInteractor testInteractor2 = new GameDetailInteractor(testPresenter2);
        testInteractor2.forward(gameDetailInputData2);
    }
}