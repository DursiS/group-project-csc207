//this class has the responsibility of generating a list of all valid moves for each piece each turn

package entity;

import java.util.ArrayList;

public class MoveValidator {
    private MoveType[][] moveTypes = new MoveType[11][];
    //private BoardTopology topology;

    //default constructor creates all movement rules corresponding to each piece type
    //i suppose this could instead be stored in a special data storage object....
    public MoveValidator(){
        //this.topology = topology;
        moveTypes[0] = new MoveType[]{}; //empty tile has no moves
        moveTypes[1] = new MoveType[]{//unmoved pawn
                new MoveType(new int[]{0,1}, 1, false, true, false, false),
                new MoveType(new int[]{1,1}, 0, true, false, false, false),
                new MoveType(new int[]{-1,1}, 0, true, false, false, false),
                new MoveType(new int[]{1,1}, 0, false, true, true, false),
                new MoveType(new int[]{-1,1}, 0, false, true, true, false)
        };
        moveTypes[2] = new MoveType[5];//moved pawn
        for (int i = 1; i < 5; i++) {
            moveTypes[2][i] = moveTypes[1][i];
        }
        moveTypes[2][0] = new MoveType(new int[]{0,1}, 0, false, true, false, false);
        moveTypes[3] = moveTypes[2];//moved pawn (enpassantable)(has the same moves)

        moveTypes[4] = new MoveType[]{//unmoved rook
                new MoveType(new int[]{0, 1}, 6),
                new MoveType(new int[]{0, -1}, 6),
                new MoveType(new int[]{1, 0}, 6),
                new MoveType(new int[]{-1, 0}, 6),
        };
        moveTypes[5] = moveTypes[4];//moved rook (castle is only considered a king move, so it's not added here)
        moveTypes[6] = new MoveType[]{//🐴
                new MoveType(new int[]{1, 2}, 0),
                new MoveType(new int[]{1, -2}, 0),
                new MoveType(new int[]{-1, 2}, 0),
                new MoveType(new int[]{-1, -2}, 0),
                new MoveType(new int[]{2, 1}, 0),
                new MoveType(new int[]{2, -1}, 0),
                new MoveType(new int[]{-2, 1}, 0),
                new MoveType(new int[]{-2, -1}, 0),
        };
        moveTypes[7] = new MoveType[]{//bishop
                new MoveType(new int[]{1, 1}, 30),
                new MoveType(new int[]{-1, -1}, 30),
                new MoveType(new int[]{1, -1}, 30),
                new MoveType(new int[]{-1, 1}, 30),
        };
        moveTypes[8] = new MoveType[]{//queen
                new MoveType(new int[]{0, 1}, 6),
                new MoveType(new int[]{0, -1}, 6),
                new MoveType(new int[]{1, 0}, 6),
                new MoveType(new int[]{-1, 0}, 6),
                new MoveType(new int[]{1, 1}, 30),
                new MoveType(new int[]{-1, -1}, 30),
                new MoveType(new int[]{1, -1}, 30),
                new MoveType(new int[]{-1, 1}, 30),
        };
        moveTypes[10] = new MoveType[]{//moved king (can't castle)
                new MoveType(new int[]{0, 1}, 0),
                new MoveType(new int[]{0, -1}, 0),
                new MoveType(new int[]{1, 0}, 0),
                new MoveType(new int[]{-1, 0}, 0),
                new MoveType(new int[]{1, 1}, 0),
                new MoveType(new int[]{-1, -1}, 0),
                new MoveType(new int[]{1, -1}, 0),
                new MoveType(new int[]{-1, 1}, 0),
        };
        moveTypes[9] = new MoveType[10];//unmoved king (can castle)
        for (int i = 0; i < 8; i++) {
            moveTypes[9][i] = moveTypes[10][i];
        }
        moveTypes[9][8] = new MoveType(new int[]{3, 0}, 0, false, false, false, true);
        moveTypes[9][9] = new MoveType(new int[]{-4, 0}, 0, false, false, false, true);
    }

    private MoveType[] getMoveTypesOfPiece(int pieceType){
        //maybe check for valid pieceType...

        //white pieces move in the opposite direction
        if (pieceType < 0){
            return this.moveTypes[-pieceType];
        }
        else{
            MoveType[] mirroredMoveTypes = new MoveType[moveTypes[pieceType].length];
            for (int i = 0; i < mirroredMoveTypes.length; i++) {
                mirroredMoveTypes[i] = MoveType.createMirroredMove(moveTypes[pieceType][i]);
            }
            return mirroredMoveTypes;
        }
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
                if(b.canPieceMove(x,y)){
                    //adds all valid moves for this piece.
                    addPieceMoves(moves, b, x, y);
                }
            }
        }
    }

    //generate moves for a specific piece
    public void addPieceMoves(ArrayList<Move> moves, Board board, int x, int y){
        int[] edgeTopologies = new int[]{board.getVerticalEdgeType(), board.getHorizontalEdgeType()};

        MoveType[] pieceMoveTypes = getMoveTypesOfPiece(board.getSquare(x,y));
        for (int i = 0; i < pieceMoveTypes.length; i++) {
            MoveType moveType = pieceMoveTypes[i];
            int repeats = 0;
            int[] destination = new int[]{x,y};
            do{
                if (moveType.isNormalMove()){
                    destination = applyMovementVector(destination, moveType, edgeTopologies);
                    //initial validity check:
                    //don't allow move if it results in an invalid location
                    //don't allow move if it loops back to the piece's original location due to an alternate board topology
                    //(this is allowed in some implementations of chess but we disable it for simplicity.)
                    if(destination == null || (destination[0] ==x && destination[1] == y)){
                        break;
                    }else{
                        //allow move if the move captures and square is occupied by an enemy
                        //also allow move if the move isn't required to capture and square is empty
                        if(moveType.isCanCapture() && board.isSquareEnemy(destination[0], destination[1])){
                            moves.add(new NormalMove(new int[]{x,y}, destination));
                            break;
                            //can't move past an enemy.
                        }
                        else if(moveType.isCanNotCapture() && board.isSquareEmpty(destination[0], destination[1]))
                        {
                            moves.add(new NormalMove(new int[]{x,y}, destination));

                            //can keep moving if the square was empty
                        }else
                        {
                            //this case only occurs if the tile is occupied by an ally (move not allowed)
                            break;
                        }
                    }
                }
                repeats += 1;
            }while(repeats <= moveType.getMaxRepeats()) ;
        }
    }

    //public boolean checkIllegalMove(Board b, Move m){
        //just use a checkmate method that should maybe exist anyway?
        //Board b2 = ApplyMove(b, m);
    //}

    public int[] applyMovementVector(int[] position, MoveType moveType, int[] edgeTopologies){
        int[] newPosition = new int[]{position[0]  + moveType.getVector()[0], position[1] + moveType.getVector()[1]};
        //vertical edge type applies to horizontal movement,
        for (int i = 0; i < 2; i++) {
            boolean locationWithinBoard  = (newPosition[i] >= 0 && newPosition[i] <= 7);
            if (edgeTopologies[i] == 0 && !locationWithinBoard) {
                return null;
            }
            else if (edgeTopologies[i] == 1 && !locationWithinBoard){
                newPosition[i] = Math.floorMod(newPosition[i], 8);//use floorMod to always return a positive value
            }
            //mirrored topology unimplemented
        }
        return newPosition;
    }

    public void ApplyMove(Board b, Move m){
        //System.out.println( b.toString());
        //System.out.println(m.toString());
        if(!b.canPieceMove(m.getOrigin()[0], m.getOrigin()[1])){
            throw new IllegalArgumentException();
        }
        if(m.getIsNormalMove()){
            int initialPiece =  b.getSquare(m.getOrigin());
            int finalPiece = Math.abs((initialPiece));

            if ( Math.abs(initialPiece) == 1){//pawn
                finalPiece = 2;
            }
            if ( Math.abs(initialPiece) == 4){//rook
                finalPiece = 5;
            }
            if ( Math.abs(initialPiece) == 9){//king
                finalPiece = 10;
            }
            if(initialPiece<0){
                finalPiece  = -finalPiece;
            }



            b.setSquare(m.getDestination(), finalPiece);
            b.setSquare(m.getOrigin(), 0);
        }
        //todo add en passant.

        //todo add castle.
        b.incrementTurn();
    }

}
