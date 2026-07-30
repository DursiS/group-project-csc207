package use_case;

import entity.Board;
import entity.GameState;
import entity.Move;
import entity.MoveValidator;

import javax.swing.*;
import java.util.ArrayList;

public class MakeMoveInteractor implements MoveInputBoundary {
    private MoveValidator validator;
    private GameState gameState;
    private MoveOutputBoundary moveOutputBoundary;

    private int[] selectedSquare;

    private ArrayList<Move> moves;

    public MakeMoveInteractor(MoveValidator validator, GameState gameState, MoveOutputBoundary moveOutputBoundary) {
        this.validator = validator;
        this.gameState = gameState;
        this.moveOutputBoundary = moveOutputBoundary;
        selectedSquare = null;

        initializeTurn();
    }



    public void initializeTurn(){
        moves = validator.getAllValidMoves(gameState.getBoard());

        if(moves.size() == 0){
            String winner = "";
            int turn = gameState.getBoard().getTurn();
            if(turn%2 ==0){
                winner = "black";
            }else{
                winner = "white";
            }
            JOptionPane.showMessageDialog(null, "CHECKMATE! " + winner + " WINS!", "CHECKMATE!", JOptionPane.INFORMATION_MESSAGE);
            //checkmate, game is over, return to menu or something?
        }
    }

    @Override
    public void receiveMove(MoveInputData data) {
        Board b = gameState.getBoard();
        boolean moved = false;

        //if nothing is selected, select the clicked square
        if(selectedSquare == null){
            selectedSquare = data.getVector();
        }else{
            //if the already is selected square is selected, then deselect.
            if (selectedSquare[0] == data.getX() && selectedSquare[1] == data.getY()){
                selectedSquare= null;
            }else
            {//if something is selected, try to make a move if one exists.
                for (int i = 0; i < moves.size(); i++) {
                    Move m = moves.get(i);
                    if(m.getOrigin()[0] == selectedSquare[0] &&
                            m.getOrigin()[1] == selectedSquare[1] &&
                            m.getDestination()[0] == data.getX()&&
                            m.getDestination()[1] == data.getY()){

                        //Move is applied, make board state copy before changing board
                        gameState.getBoardStateList().addBoardCopy(b);
                        validator.ApplyMove(b, m);
                        initializeTurn();
                        selectedSquare=null;
                        moved = true;
                        break;
                    }
                }

                if (moved == false) {//if no move was made, then the user was just trying to select another square, so select it.
                    selectedSquare = data.getVector();
                }
            }
        }

        ConcludeInteraction();
    }

    private void ConcludeInteraction(){
        Board b = gameState.getBoard();


        int[][] tileVisuals = new int[8][8];//0: default (will be checkerboard), 1:selected piece, 2:moveable square for the currently selected piece
        int[][] pieceTypes = new int[8][8];

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                pieceTypes[y][x] = b.getSquare(x,y);
                tileVisuals[y][x] = 0;
            }
        }
        if (selectedSquare != null){
            tileVisuals[selectedSquare[1]][selectedSquare[0]] = 1; //selected

            for (int i = 0; i < moves.size(); i++) {
                Move m = moves.get(i);
                if(m.getOrigin()[0] == selectedSquare[0] &&
                        m.getOrigin()[1] == selectedSquare[1]){

                    tileVisuals[m.getDestination()[1]][m.getDestination()[0]] = 2;//moveable
                }
            }
        }

        MoveOutputData outData = new MoveOutputData(pieceTypes,tileVisuals);


        moveOutputBoundary.present(outData);
    }

    public void UpdateVisuals(){
        ConcludeInteraction();
    }
}
