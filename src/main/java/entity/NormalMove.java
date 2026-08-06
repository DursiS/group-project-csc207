package entity;

import use_case.MakeMoveInteractor;

public class NormalMove extends Move{

    //@Override
    //public Boolean getIsNormalMove() {
    //    return true;
    //}

    public NormalMove(int[] origin, int[] destination){
        super(origin, destination);
    }

    @Override
    public void ApplyMove(Board b) {
        int finalPiece = getModifiedPieceType(b.getSquare(this.getOrigin()));

        int[] destination = MoveValidator.applyQuotientRelation(this.getDestination(),b);
        b.setSquare(destination, finalPiece);
        b.setSquare(this.getOrigin(), 0);
    }
}
