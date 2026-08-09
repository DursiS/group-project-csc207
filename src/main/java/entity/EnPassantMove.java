package entity;

public class EnPassantMove extends Move {
    private int[] capture;

    /**
     * Create an EnPassant move
     * @param origin initial location of pawn
     * @param destination final location of pawn
     * @param capture location of pawn that's getting captured
     */
    public EnPassantMove(int[] origin, int[] destination, int[] capture) {
        super(origin, destination);
        this.capture= capture;
    }

    /**
     *
     * @return the location of the pawn that is getting captured
     */
    public int[] getCapture() {
        return capture;
    }

    /**
     * apply the move to the board
     * @param b the board
     */
    @Override
    public void ApplyMove(Board b) {
        int finalPiece = getModifiedPieceType(b.getSquare(this.getOrigin()));

        b.setSquare(this.getOrigin(),0);
        b.setSquare(this.getDestination(), finalPiece);
        b.setSquare(this.capture, 0);
    }
}
