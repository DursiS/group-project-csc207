package entity;

public class NormalMove extends Move{

    @Override
    public Boolean getIsNormalMove() {
        return true;
    }

    public NormalMove(int[] origin, int[] destination){
        super(origin, destination);
    }

    @Override
    public void ApplyMove(Board b) {
        int initialPiece =  b.getSquare(this.getOrigin());
        int finalPiece = Math.abs((initialPiece));

        if ( Math.abs(initialPiece) == 1){//pawn
            finalPiece = 2;
            if(Math.abs(this.getOrigin()[1] - this.getDestination()[1]) == 2){
                finalPiece = 3;
            }
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

        b.setSquare(this.getDestination(), finalPiece);
        b.setSquare(this.getOrigin(), 0);
    }
}
