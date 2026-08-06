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

    //private MoveType[] getMoveTypesOfPiece(int pieceType){
    //    //maybe check for valid pieceType...
//
//        //white pieces move in the opposite direction
//        if (pieceType < 0){
//            return this.moveTypes[-pieceType];
//        }
//        else{
//            MoveType[] mirroredMoveTypes = new MoveType[moveTypes[pieceType].length];
//            for (int i = 0; i < mirroredMoveTypes.length; i++) {
//                mirroredMoveTypes[i] = MoveType.createMirroredMove(moveTypes[pieceType][i]);
//            }
//            return mirroredMoveTypes;
//        }
//    }

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
            int[] destination;

            //if (moveType.isEnPassant()){
                //destination = applyMovementVector(destination,moveType,edgeTopologies);
                //int[] horizontal = moveType.getVector();
                //horizontal[1] = 0;
                //horizontal = applyMovementVector(new int[]{x,y},new moveType())


                //OK i think for these move types, it's easiest to have another (normal) move type, and that one is applied to the destination or whatever
                //and would work nicer for the
                //so yeah, make a Move type class, and then make a EnPassantMoveType, and CastleMoveType classes........
                //and pass these values directly to the constructor of the special Move classes (inherited from Move).
            //}
            if (moveType instanceof EnPassantMoveType nMoveType){
                destination = applyQuotientRelation(nMoveType.createMove(origin).getDestination(), board);
                int[] capture = applyQuotientRelation(nMoveType.createMove(origin).getCapture(), board);
                if(destination==null || capture==null){
                    return;
                }

                if(board.isSquareEmpty(nMoveType.createMove(origin).getDestination())
                && board.isSquareEnemy(nMoveType.createMove(origin).getCapture())){
                    moves.add(nMoveType.createMove(origin));
                }
            }


            //do the repeated checking-validation which is required for normal moves
            if (moveType instanceof NormalMoveType nMoveType){
                int repeats = 0;
                do{
                    destination = nMoveType.createMove(origin, repeats).getDestination();
                    destination = applyQuotientRelation(destination, board);
                    //initial validity check:
                    //don't allow move if it results in an invalid location
                    //don't allow move if it loops back to the piece's original location due to an alternate board topology
                    //(this is allowed in some implementations of chess but we disable it for simplicity.)

                    if(destination == null || (destination[0] ==x && destination[1] == y)){
                        break;
                    }else{
                        //allow move if the move captures and square is occupied by an enemy
                        //also allow move if the move isn't required to capture and square is empty
                        if(board.isSquareEnemy(destination[0], destination[1])){
                            if (nMoveType.isCanCapture()){
                                moves.add(nMoveType.createMove(origin, repeats));
                            }
                            break;
                            //can't move past an enemy.
                        }
                        else if(nMoveType.isCanNotCapture() && board.isSquareEmpty(destination[0], destination[1]))
                        {
                            moves.add(nMoveType.createMove(origin, repeats));
                            //can keep moving if the square was empty, so don't break.
                        }else if( !board.isSquareEmptyOrEnemy(destination[0],destination[1]))
                        {
                            //if square is occupied by an ally, don't allow movement through it
                            break;
                        }
                    }
                    repeats += 1;
                }while(repeats <= nMoveType.getMaxRepeats()) ;
            }
        }
    }

    //public boolean checkIllegalMove(Board b, Move m){
        //just use a checkmate method that should maybe exist anyway?
        //Board b2 = ApplyMove(b, m);
    //}

    //public static int[] applyMovementVector(int[] position, int[] moveVector, int[] edgeTopologies){
    //    int[] newPosition = new int[]{position[0]  + moveVector[0], position[1] + moveVector[1]};
    //    //vertical edge type applies to horizontal movement,
    //    for (int i = 0; i < 2; i++) {
    //        boolean locationWithinBoard  = (newPosition[i] >= 0 && newPosition[i] <= 7);
    //        if (edgeTopologies[i] == 0 && !locationWithinBoard) {
    //            return null;
    //        }
    //        else if (edgeTopologies[i] == 1 && !locationWithinBoard){
    //            newPosition[i] = Math.floorMod(newPosition[i], 8);//use floorMod to always return a positive value
    //        }
    //        //mirrored topology unimplemented
    //    }
    //    return newPosition;
    //}

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
        if(!b.isPiecesTurn(m.getOrigin()[0], m.getOrigin()[1])){
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


        //todo add en passant.

        //todo add castle.
        b.incrementTurn();

    }

}
