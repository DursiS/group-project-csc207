package entity;

import interface_adapter.MoveController;
import interface_adapter.MovePresenter;
import interface_adapter.MoveViewModel;
import org.junit.jupiter.api.Test;
import use_case.MakeMoveInteractor;
import view.MoveView;

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

        moveViewModel = new MoveViewModel();
        //this should not be stored here, rather it should be created and passed to the MakeMoveInteractor when starting a game...
        gameState = new GameState(new Board(0,0), 0, 0, new BoardStateList(),"idk what this is");
        MoveValidator moveValidator = new MoveValidatorBuilder().doDefaultSetup().build();
        movePresenter = new MovePresenter(moveViewModel);
        makeMoveInteractor = new MakeMoveInteractor(moveValidator, gameState, movePresenter);
        moveController = new MoveController(makeMoveInteractor);
        moveView = new MoveView(moveViewModel, moveController);
        makeMoveInteractor.updateVisuals();

        moveController.ReceiveClick(0,6);
        moveController.ReceiveClick(0,4);


        assertEquals(gameState.getBoard().getSquare(0,6), 0);
    }
}
