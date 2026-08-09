package use_case;

import entity.Board;
import entity.GameState;
import entity.Move;
import entity.MoveValidator;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * this interactor is responsible for the movement use case,
 * which includes player input to the board, move validation, and game movement logic
 */
public class MakeMoveInteractor implements MoveInputBoundary {
    private static final String UPDATE_CHANNEL = "update-analysis";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private MoveValidator validator;
    private GameState gameState;
    private MoveOutputBoundary moveOutputBoundary;
    private SaveGameInputBoundary saveGameInputBoundary;

    private int[] selectedSquare;

    private ArrayList<Move> moves;

    /**
     * create make move interactor
     * @param validator the move validator it's assigned to
     * @param gameState reference to the gamestate to retrieve the board from
     * @param moveOutputBoundary after making a move, the raw information to present is passed here
     */
    public MakeMoveInteractor(MoveValidator validator,
                              GameState gameState,
                              MoveOutputBoundary moveOutputBoundary,
                              SaveGameInputBoundary saveGameInputBoundary) {
        this.validator = validator;
        this.gameState = gameState;
        this.moveOutputBoundary = moveOutputBoundary;
        this.saveGameInputBoundary = saveGameInputBoundary;
        selectedSquare = null;

        initializeTurn();
    }

    /**
     * this method is called at the start of each turn, to check if there are valid moves,
     * if there are none, it's game over. (checkmate.).
     */
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
        updateAnalyzeMoveInteractor();
    }

    /**
     * this takes the player input for making a selection or move, and
     * either changes the selection if they were trying to select something
     * or makes a move if they were trying to make a move
     * @param data the input data
     */
    @Override
    public void receiveInput(MoveInputData data) {
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

                        saveGameInputBoundary.autosave(gameState);

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

        updateVisuals();
    }

    /**
     * after the player input, redetermine the board appearance
     * and prepare to present that data through the output boundary
     */
    public void updateVisuals(){
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

    /**
     * Updates the AnalyzeMoveInteractor's reference of GameState
     * by using an Observer Pattern to solve the problem of consistency.
     */
    private void updateAnalyzeMoveInteractor() {
        support.firePropertyChange(UPDATE_CHANNEL,
                null,
                gameState
        );
    }

    /**
     * Add a change listener so fires are actually listener to.
     * @param listener a property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
