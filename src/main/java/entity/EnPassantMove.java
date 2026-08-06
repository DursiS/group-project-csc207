package entity;

public class EnPassantMove extends Move {
    private int[] capture;
    public EnPassantMove(int[] origin, int[] destination, int[] capture) {
        super(origin, destination);
        this.capture= capture;
    }

    public int[] getCapture() {
        return capture;
    }
//@Override
    //public Boolean getIsNormalMove() {
    //    return false;
    //}

    @Override
    public void ApplyMove(Board b) {
        int finalPiece = getModifiedPieceType(b.getSquare(this.getOrigin()));

        b.setSquare(this.getOrigin(),0);
        b.setSquare(this.getDestination(), finalPiece);
        b.setSquare(this.capture, 0);
    }
}
