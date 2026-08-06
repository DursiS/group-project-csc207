//this class has the responsibility of generating a list of all valid moves for each piece each turn

package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoveValidator {
    //private MoveType[][] moveTypes;
    private HashMap<Integer,MoveType[]> moveTypes;

    public MoveValidator(HashMap<Integer,MoveType[]> moveTypes){
        this.moveTypes= moveTypes;
    }

    private MoveType[] getMoveTypesOfPiece(int pieceType){
        //System.out.println(pieceType);
        //System.out.println(moveTypes.containsKey(pieceType));
        if(!moveTypes.containsKey(pieceType)){
            return new MoveType[]{};
        }
        return moveTypes.get(pieceType);
    }

    //it generates all moves for every piece belonging to the player who's currently is (in board).
    //it's necessary to see if the player is currently in checkmate (no valid moves)
    //this method might be computationally difficult so we might want to do it differently....
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
                //System.out.println(enemyImaginaryMoves.get(j).getOrigin()[0] + " " + enemyImaginaryMoves.get(j).getOrigin()[1]);
                //System.out.println(enemyImaginaryMoves.get(j).getDestination()[0] + " " + enemyImaginaryMoves.get(j).getDestination()[1]);

                //System.out.println(enemyImaginaryMoves.get(j) instanceof EnPassantMove);
                //System.out.println(enemyImaginaryMoves.get(j) instanceof CastleMove);
                //System.out.println(enemyImaginaryMoves.get(j) instanceof NormalMove);


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

    private Boolean isNonTurnPlayersKing(Board b, int[] location){
        //on white's turn, return if it is targeting the enemy (black) king
        if (b.getTurn()%2 == 0){
            return(b.getSquare(location) == -9 || b.getSquare(location) == -10);
        }
        //on black's turn, return if it is targeting the enemy (white) king
        return(b.getSquare(location) == 9 || b.getSquare(location) == 10);
    }

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

    //generate moves for a specific piece
    public void addPieceMoves(ArrayList<Move> moves, Board board, int x, int y){
        int[] origin = new int[]{x,y};
        MoveType[] pieceMoveTypes = getMoveTypesOfPiece(board.getSquare(x,y));


        for (int i = 0; i < pieceMoveTypes.length; i++) {
            MoveType moveType = pieceMoveTypes[i];
            moveType.AddPossibleMoves(moves,board,origin);
        }
    }

    public static int[] applyQuotientRelation(int[] position, Board b) {
        int[] edgeTopologies = new int[]{b.getVerticalEdgeType(), b.getHorizontalEdgeType()};
        for (int i = 0; i < 2; i++) {
            boolean locationWithinBoard = (position[i] >= 0 && position[i] <= 7);
            if (edgeTopologies[i] == 0 && !locationWithinBoard) {
                return null;
            } else if (edgeTopologies[i] == 1 && !locationWithinBoard) {
                position[i] = Math.floorMod(position[i], 8);//use floorMod to always return a positive value
            }
            //mirrored topology unimplemented
        }
        return position;
    }



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

        b.incrementTurn();

    }

}
