package SaveResume;

import MakeMove.Board;
import MakeMove.BoardStateList;
import MakeMove.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SaveGameInteractorTest {
    private GameDataAccess gameDataAccess;
    private SaveGameViewModel viewModel;
    private SaveGameInputBoundary interactor;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        gameDataAccess = new InMemoryGameDataAccessObject();
        viewModel = new SaveGameViewModel();
        SaveGameOutputBoundary presenter = new SaveGamePresenter(viewModel);
        interactor = new SaveGameInteractor(gameDataAccess, presenter);

        gameState = new GameState(
                new Board(),
                300000,
                295000,
                new BoardStateList(),
                "IN_PROCESS"
        );
    }

    @Test
    void saveGameTest() {
        SaveGameInputData inputData = new SaveGameInputData("save1", gameState, false);
        interactor.execute(inputData);

        assertTrue(gameDataAccess.saveExists("save1"));
        assertEquals("save1 Successfully Saved!", viewModel.getMessage());
    }

    @Test
    void automaticSaveNameTest() {
        interactor.execute(new SaveGameInputData("", gameState, false));
        interactor.execute(new SaveGameInputData("", gameState, false));

        assertTrue(gameDataAccess.saveExists("save1"));
        assertTrue(gameDataAccess.saveExists("save2"));
    }

    @Test
    void saveAlreadyExistsTest() {
        interactor.execute(new SaveGameInputData("save1", gameState, false));
        interactor.execute(new SaveGameInputData("save1", gameState, false));

        assertEquals("Save Already Exist, Do You Want To Overwrite Save?",
                viewModel.getOverwriteMessage());
    }

    @Test
    void nullGameStateTest() {
        interactor.execute(new SaveGameInputData("save1", null, false));

        assertEquals("Game state cannot be null!", viewModel.getError());
    }

    @Test
    void autosaveTest() {
        interactor.autosave(gameState);

        assertTrue(gameDataAccess.saveExists(SaveGameInteractor.AUTOSAVE_NAME));
    }
}
