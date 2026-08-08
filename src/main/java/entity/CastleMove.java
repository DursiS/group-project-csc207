package entity;

public class CastleMove extends Move{
    private int[] kingDest;
    private int[] rookDest;

    /**
     * create a new castle move
     * @param origin the king's initial position
     * @param destination the rook's initial position (where the user clicks to make the move)
     * @param kingDest the king's final position
     * @param rookDest the rook's final position
     */
    public CastleMove(int[] origin, int[] destination, int[] kingDest, int[] rookDest) {
        super(origin, destination);
        this.kingDest=kingDest;
        this.rookDest=rookDest;
    }

    /**
     * Apply the move to the board
     * @param b the board
     */
    @Override
    public void ApplyMove(Board b) {
        int finalKing = getModifiedPieceType(b.getSquare(getOrigin()));
        int finalRook = getModifiedPieceType(b.getSquare(getDestination()));
        b.setSquare(getOrigin(),0);
        b.setSquare(getDestination(),0);
        b.setSquare(kingDest,finalKing);
        b.setSquare(rookDest,finalRook);

    }
}
