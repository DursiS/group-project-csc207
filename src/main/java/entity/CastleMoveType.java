package entity;

import java.util.ArrayList;

public class CastleMoveType extends MoveType{
    private int[] king;
    private int[] rook;
    private int horizontalDirection;//needs this to check if the path is clear to the rook.

    /**
     * create new castle move type ("blueprint" for a castle move)
     * @param kingRookVector the vector from the king to the rook
     * @param kingMovement the movement vector for the king
     * @param rookMovement the movement vector for the rook
     * @param horizontalDirection the 1-tile movement vector from the king to the rook (required to check if the path is clear)
     */
    public CastleMoveType(int[] kingRookVector, int[] kingMovement, int[] rookMovement, int horizontalDirection){
        this.movementVector = kingRookVector;
        this.king= kingMovement;
        this.rook=  rookMovement;
        this.horizontalDirection=horizontalDirection;
    }

    /**
     * Creates the move from the MoveType
     * @param origin where the piece is that's executing the move
     * @return the move
     */
    public CastleMove createMove(int[] origin) {
        //rook destination vector is the movetype's destination vector (rook location) plus rook movement vector
        return new CastleMove(origin, addVectors(origin,movementVector), addVectors(origin,king),addVectors(addVectors(origin,movementVector),rook));
    }

    /**
     * creates duplicate of the black move for the equivalent white pieces
     * @return the mirrored move
     */
    @Override
    public MoveType createMirroredMove() {
        return this; //move types are supposed to be immutable so shouldn't cause any issues
    }

    /**
     * Given the board, and location of the piece,
     * and adds the move based on this move type to the list moves, if possible
     * (this allows it to return 0, 1, or potentially more moves
     *     //technically this doesn't work like the normal castling in chess (can't move if any tile in the path is captureable by enemy)
     *     //  since it would require computation of fake moves during the turn...
     *     //it's possible to implement, but would require much more computation
     *     //In fact it would cause an infinite recursion if implemented naively (without restructuring)
     * @param moves list of moves to add to
     * @param b current board
     * @param origin location of moving piece
     */
    @Override
    public void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin) {
        int[] destination = new int[]{origin[0],origin[1]};

        do {
            //System.out.println("currently " + destination[0]);
            destination[0] += horizontalDirection;
            destination = MoveValidator.applyQuotientRelation(destination,b);
            if (destination == null){
                return;
            }
            else if(b.isSquareEnemy(destination)){
                return;
            }
            else if(b.getSquare(destination) == 4 || b.getSquare(destination) == -4){
                moves.add(createMove(origin));
            }
            else if(!b.isSquareEmpty(destination)){
                return; //can't move through pieces
            }
        }while(destination[0] != origin[0]);
    }
}
