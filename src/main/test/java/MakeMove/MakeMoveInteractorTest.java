package MakeMove;

import MakeMove.MoveController;
import MakeMove.MovePresenter;
import MakeMove.MoveViewModel;
import archive.GameDataAccessObject;
import archive.GameRecord;
import org.junit.jupiter.api.Test;
import MakeMove.MakeMoveInteractor;
import MakeMove.MoveView;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MakeMoveInteractorTest {
    @Test
    void basicMakeMoveInteractorTest(){
        MoveViewModel moveViewModel;
        MoveView moveView;
        MoveController moveController;
        MakeMoveInteractor makeMoveInteractor;
        MovePresenter movePresenter;
        GameState gameState;
        GameRecord gameRecord;

        GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
        moveViewModel = new MoveViewModel();
        //this should not be stored here, rather it should be created and passed to the MakeMoveInteractor when starting a game...
        gameState = new GameState(new Board(0,0), 0, 0, new BoardStateList(),"idk what this is");
        gameRecord = new GameRecord(gameState);
        MoveValidator moveValidator = new MoveValidatorBuilder().doDefaultSetup().build();
        movePresenter = new MovePresenter(moveViewModel);
        makeMoveInteractor = new MakeMoveInteractor(moveValidator, gameDataAccessObject, gameRecord,
                gameState, movePresenter);
        moveController = new MoveController(makeMoveInteractor);
        moveView = new MoveView(moveViewModel, moveController);
        makeMoveInteractor.updateVisuals();

        moveController.ReceiveClick(0,6);
        moveController.ReceiveClick(0,4);


        assertEquals(gameState.getBoard().getSquare(0,6), 0);
    }
}
