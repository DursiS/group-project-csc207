package app;


import entity.Board;
import entity.BoardStateList;
import entity.GameState;
import entity.MoveValidator;
import interface_adapter.MoveController;
import interface_adapter.MovePresenter;
import interface_adapter.MoveViewModel;
import use_case.MakeMoveInteractor;
import view.MoveView;

import javax.swing.*;

public class AppBuilder {
    private MoveViewModel moveViewModel;
    private MoveView moveView;
    private MoveController moveController;
    private MakeMoveInteractor makeMoveInteractor;
    private MovePresenter movePresenter;

    private GameState gameState;

    public AppBuilder addMoveView() {
        moveViewModel = new MoveViewModel();

        //this should not be stored here, rather it should be created and passed to the MakeMoveInteractor when starting a game...
        gameState = new GameState(new Board(0,0), 0, 0, new BoardStateList(),"idk what this is");
        MoveValidator moveValidator = new MoveValidator();

        movePresenter = new MovePresenter(moveViewModel);

        makeMoveInteractor = new MakeMoveInteractor(moveValidator, gameState, movePresenter);
        moveController = new MoveController(makeMoveInteractor);

        moveView = new MoveView(moveViewModel, moveController);
        makeMoveInteractor.UpdateVisuals();

        return this;
    }

    public JFrame build(){
        // 1. Create the window frame
        final JFrame application = new JFrame("Chess");
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.setSize(600,600);

        application.add(moveView);//as a panel

        application.setVisible(true);

        return application;
    }
}
