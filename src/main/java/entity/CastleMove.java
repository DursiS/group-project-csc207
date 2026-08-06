package entity;

public class CastleMove extends Move{
    private int[] kingDest;
    private int[] rookDest;
    public CastleMove(int[] origin, int[] destination, int[] kingDest, int[] rookDest) {
        super(origin, destination);
        this.kingDest=kingDest;
        this.rookDest=rookDest;
    }

    @Override
    public void ApplyMove(Board b) {
        int finalKing = getModifiedPieceType(b.getSquare(getOrigin()));
        int finalRook = getModifiedPieceType(b.getSquare(getDestination()));
        b.setSquare(getOrigin(),0);
        b.setSquare(getDestination(),0);
        b.setSquare(kingDest,finalKing);
        b.setSquare(rookDest,finalRook);

    }

    //@Override
    //public Boolean getIsNormalMove() {
    //    return false;
    //}
}
