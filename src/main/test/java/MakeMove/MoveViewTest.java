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

public class MoveViewTest {
    @Test
    void castleViewTest(){
        MoveViewModel moveViewModel;
        MoveView moveView;
        MoveController moveController;
        MakeMoveInteractor makeMoveInteractor;
        MovePresenter movePresenter;
        GameState gameState;
        GameRecord gameRecord;

        Board b = new Board(0,0);

        int[][] squares = new int[8][8];
        squares[0][4] = -9;
        squares[0][0] = -4;
        b = new Board(squares, 0,0,0);
        b.setTurn(1);

        GameDataAccessObject gameDataAccessObject = new GameDataAccessObject();
        moveViewModel = new MoveViewModel();
        //this should not be stored here, rather it should be created and passed to the MakeMoveInteractor when starting a game...
        gameState = new GameState(b,0,0, new BoardStateList(),"idk what this is");
        gameRecord = new GameRecord(gameState);
        MoveValidator moveValidator = new MoveValidatorBuilder().doDefaultSetup().build();
        movePresenter = new MovePresenter(moveViewModel);
        makeMoveInteractor = new MakeMoveInteractor(moveValidator, gameDataAccessObject, gameRecord,
                gameState, movePresenter);
        moveController = new MoveController(makeMoveInteractor);
        moveView = new MoveView(moveViewModel, moveController);
        makeMoveInteractor.updateVisuals();

        //select king
        moveController.ReceiveClick(4,0);

        //king normal movement
        assertEquals(moveViewModel.getSquareColour(5,1), moveViewModel.getSquareColour(5,0));
        //king castle
        assertEquals(moveViewModel.getSquareColour(0,0), moveViewModel.getSquareColour(5,0));

        String king = moveViewModel.getSquareText(4,0);

        moveController.ReceiveClick(0,0);

        assertEquals(moveViewModel.getSquareText(2,0), king);

        //final JFrame application = new JFrame("Chess");
        //application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //application.setSize(600,600);

        //application.add(moveView);//as a panel

        //application.setVisible(true);


        //Thread.sleep(5000);

    }
}
