package use_case;

import entity.Board;
import entity.GameState;
import entity.Move;
import entity.MoveValidator;

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
                        validator.ApplyMove(b, m);
                        initializeTurn(); //new turn beings (this seems like a weird place to do it though...)
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

        ConcludeMove();
    }

    private void ConcludeMove(){
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
        ConcludeMove();
    }
}
