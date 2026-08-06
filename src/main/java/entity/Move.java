//this is a data type for a move that's made by a player
//or, for a valid move that a player can make

package entity;

public abstract class Move {
    private int[] origin;
    private int[] destination;

    public int[] getOrigin() {
        return origin;
    }

    public int[] getDestination() {
        return destination;
    }

    //public abstract Boolean getIsNormalMove();

    public Move(int[] origin, int[] destination) {
        this.origin = origin;
        this.destination = destination;
    }

    //for debugging
    //public String toString(){
    //    return "(" + origin[0] + "," + origin[1] + ") -> (" + destination[0] + "," + destination[1] + ")";
    //}

    public abstract void ApplyMove(Board b);

    public int getModifiedPieceType(int initialPiece){
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
        return finalPiece;
    }
}
