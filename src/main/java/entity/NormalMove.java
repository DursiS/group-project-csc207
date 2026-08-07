package entity;

import use_case.MakeMoveInteractor;

/**
 * this class is for moves that just have typical behaviour:
 * they move a single piece and remove a piece if they land on it.
 */
public class NormalMove extends Move{

    /**
     * create a new normal move
     * @param origin initial position
     * @param destination final position
     */
    public NormalMove(int[] origin, int[] destination){
        super(origin, destination);
    }

    /**
     *apply the move to the board
     * @param b the board
     */
    @Override
    public void ApplyMove(Board b) {
        int finalPiece = getModifiedPieceType(b.getSquare(this.getOrigin()));

        int[] destination = MoveValidator.applyQuotientRelation(this.getDestination(),b);
        b.setSquare(destination, finalPiece);
        b.setSquare(this.getOrigin(), 0);
    }
}
