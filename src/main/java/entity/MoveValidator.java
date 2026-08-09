package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * //this class has the responsibility of generating a list of all valid moves for each piece each turn
 */
public class MoveValidator {
    private HashMap<Integer,MoveType[]> moveTypes;

    /**
     * constructor for MoveValidator
     * @param moveTypes a map from integer -> movetype[], where the keys are the type of piece
     *                  and the movetype[] is the move types corresponding to the moves the
     *                  piece can make.
     */
    public MoveValidator(HashMap<Integer,MoveType[]> moveTypes){
        this.moveTypes= moveTypes;
    }

    /**
     * method to get the move types given a piece type (
     * @param pieceType the piece type (can be invalid)
     * @return an array containing all the moves the piece can make (will return an empty list
     * if there are no moves designated to this piece type.)
     */
    private MoveType[] getMoveTypesOfPiece(int pieceType){
        if(!moveTypes.containsKey(pieceType)){
            return new MoveType[]{};
        }
        return moveTypes.get(pieceType);
    }

    /**
     * Returns list of all the valid moves that the player whose turn it currently is can make
     * (considering every piece)
     * ("valid" means that it can't be moves that endanger the king)
     * This is necessary to do at the start of each turn to see if there are no valid moves remaining,
     * in which case it's checkmate
     * @param board the board
     * @return list of moves
     */
    public ArrayList<Move> getAllValidMoves(Board board){
        ArrayList<Move> moves = new ArrayList<Move>();
        //add moves for each piece
        addAllMoves(moves, board);

        //remove invalid moves that leave the king vulnerable
        for (int i = moves.size()-1; i >=0; i--) {
            Board imaginaryBoard = board.Copy();
            ApplyMove(imaginaryBoard, moves.get(i));
            //if there are performance issues, this can easily be optimized slightly
            ArrayList<Move> enemyImaginaryMoves = new ArrayList<>();
            addAllMoves(enemyImaginaryMoves, imaginaryBoard);
            boolean invalid = false;
            //this is kind of cheating, but the only moves that could theoretically endanger the king are normal moves that have destination at the king...
            for (int j = 0; j < enemyImaginaryMoves.size(); j++) {
                if(isNonTurnPlayersKing(imaginaryBoard, enemyImaginaryMoves.get(j).getDestination())){
                    invalid = true;
                    break;
                }
            }
            if(invalid){
                moves.remove(i);
            }
        }
        return moves;
    }

    /**
     * return if the specified square is the enemy king wrt the player whose turn it currently is
     * @param b the board
     * @param location the square in question
     * @return true iff it's the enemy king
     */
    private Boolean isNonTurnPlayersKing(Board b, int[] location){
        //on white's turn, return if it is targeting the enemy (black) king
        if (b.getTurn()%2 == 0){
            return(b.getSquare(location) == -9 || b.getSquare(location) == -10);
        }
        //on black's turn, return if it is targeting the enemy (white) king
        return(b.getSquare(location) == 9 || b.getSquare(location) == 10);
    }

    /**
     * add all the moves the currently moving player can make to a list
     * this includes everything, doesn't check if they're valid, i.e. they can leave
     * the king vulnerable.
     * @param moves list of moves
     * @param b current board
     */
    public void addAllMoves(ArrayList<Move> moves, Board b){
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if(b.isPiecesTurn(x,y)){
                    //adds all valid moves for this piece.
                    addPieceMoves(moves, b, x, y);
                }
            }
        }
    }

    /**
     * add all the moves for a piece at a specific (x,y) location to a list
     * @param moves list to add to
     * @param board the board
     * @param x x coord
     * @param y y coord
     */
    public void addPieceMoves(ArrayList<Move> moves, Board board, int x, int y){
        int[] origin = new int[]{x,y};
        MoveType[] pieceMoveTypes = getMoveTypesOfPiece(board.getSquare(x,y));

        for (int i = 0; i < pieceMoveTypes.length; i++) {
            MoveType moveType = pieceMoveTypes[i];
            moveType.AddPossibleMoves(moves,board,origin);
        }
    }

    /**
     * given an [x,y] position vector, will return null if it's an invalid position on the board,
     * otherwise will return the equivalent point that's within
     * the range 0<=x<=7, 0<=y<=7 considering the board's topology
     * @param position the position vector (can be invalid)
     * @param b the board (includes topology information)
     * @return null or a vector within the valid range
     */
    public static int[] applyQuotientRelation(int[] position, Board b) {
        int[] edgeTopologies = new int[]{b.getVerticalEdgeType(), b.getHorizontalEdgeType()};
        for (int i = 0; i < 2; i++) {
            boolean locationWithinBoard = (position[i] >= 0 && position[i] <= 7);
            if (edgeTopologies[i] == 0 && !locationWithinBoard) {
                return null;
            } else if (edgeTopologies[i] == 1 && !locationWithinBoard) {
                position[i] = Math.floorMod(position[i], 8);//use floorMod to always return a positive value
            }
            //(implementation unfinished - doesn't support mirrored topology)
            //if more topologies are considered, we should refactor the different topologies
            //into classes, and have each one overwrite an applyQuotientRelation function
            //to do their unique behaviour
        }
        return position;
    }

    /**
     * Apply the given move to the board.
     * @param b the board
     * @param m the move to apply
     */
    public void ApplyMove(Board b, Move m){
        if(!b.isPiecesTurn(m.getOrigin())){
            throw new IllegalArgumentException();
        }

        //remove en passant status
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (b.isPiecesTurn(x,y)){
                    if(b.getSquare(x,y) == 3){
                        b.setSquare(x,y, 2);
                    }
                    if(b.getSquare(x,y) == -3){
                        b.setSquare(x,y, -2);
                    }
                }
            }
        }

        //apply move
        m.ApplyMove(b);

        //turn pawns into queens
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if(b.getSquare(x,y) == 2 && y == 0){
                    b.setSquare(x,y, 8);
                }
                if(b.getSquare(x,y) == -2 && y == 7){
                    b.setSquare(x,y,-8);
                }
            }
        }

        //advance turn
        b.incrementTurn();
    }
}
