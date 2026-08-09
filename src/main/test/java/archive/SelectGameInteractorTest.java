package archive;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import app.ViewManagerModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SelectGameInteractorTest {

    private GameRecord gameRecord;
    private GameRecord gameRecord2;
    private final GameDataAccessObject gameDataAccessObject = new GameDataAccessObject() {
        @Override
        public GameRecord load(UUID id) {
            return gameRecord;
        }
    };
    private final GameDataAccessObject gameDataAccessObject2 = new GameDataAccessObject() {
        @Override
        public GameRecord load(UUID id) {
            return gameRecord2;
        }
    };
    private final GameDataAccessObject failGameDataAccessObject = new GameDataAccessObject() {
        @Override
        public GameRecord load(UUID id) {
            throw new RuntimeException("E");
        }
    };

    @BeforeEach
    void setUp() {
        Board board1 = new Board();
        BoardStateList boardStateList = new BoardStateList();
        GameState gameState1 = new GameState(board1, 60000, 60000, boardStateList, "");
        gameRecord = new GameRecord(gameState1);
        gameRecord2 = new GameRecord(gameState1);
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
    void successTest() {
        SelectGameInputData data = new SelectGameInputData(gameRecord.getId());
        GameDetailPresenter testPresenter = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void initializeGameDetailView(SelectGameOutputData selectGameOutputData) {
                assertEquals(gameRecord.getId(), selectGameOutputData.gameRecord().getId());
                assertEquals(gameRecord.getHistory().get(0), selectGameOutputData.gameState());
                assertEquals(gameRecord.getGameResult(), selectGameOutputData.gameResult());
                assertTrue(selectGameOutputData.hasNext());
            }
        };
        SelectGameInteractor interactor = new SelectGameInteractor(gameDataAccessObject,
                testPresenter);
        interactor.selectGame(data);

        SelectGameInputData data2 = new SelectGameInputData(gameRecord2.getId());
        GameDetailPresenter testPresenter2 = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void initializeGameDetailView(SelectGameOutputData selectGameOutputData) {
                assertEquals(gameRecord2.getId(), selectGameOutputData.gameRecord().getId());
                assertEquals(gameRecord2.getHistory().get(0), selectGameOutputData.gameState());
                assertEquals(gameRecord2.getGameResult(), selectGameOutputData.gameResult());
                assertFalse(selectGameOutputData.hasNext());
            }
        };
        SelectGameInteractor interactor2 = new SelectGameInteractor(gameDataAccessObject2,
                testPresenter2);
        interactor2.selectGame(data2);
    }

    @Test
    void failTest() {
        SelectGameInputData data = new SelectGameInputData(UUID.randomUUID());
        GameDetailPresenter testPresenter = new GameDetailPresenter(new GameDetailViewModel(),
                new ViewManagerModel()) {
            @Override
            public void prepareErrorView(String errorMessage) {
                assertEquals("Could not load the game: E", errorMessage);
            }
        };
        SelectGameInteractor interactor = new SelectGameInteractor(failGameDataAccessObject,
                testPresenter);
        interactor.selectGame(data);
    }
}