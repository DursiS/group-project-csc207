package MakeMove;

/**
 * //this is a data type for a move that's made by a player
 * //or, for a valid move that a player can make
 */
public abstract class Move {
    private int[] origin;
    private int[] destination;

    /**
     *
     * @return the initial position of the piece
     */
    public int[] getOrigin() {
        return origin;
    }

    /**
     *
     * @return the final position of the piece
     */
    public int[] getDestination() {
        return destination;
    }

    /**
     *default constructor
     * @param origin
     * @param destination
     */
    public Move(int[] origin, int[] destination) {
        this.origin = origin;
        this.destination = destination;
    }

    //for debugging
    //public String toString(){
    //    return "(" + origin[0] + "," + origin[1] + ") -> (" + destination[0] + "," + destination[1] + ")";
    //}

    /**
     * abstract method for applying  a move to the board, that needs to be implemented by each move type
     * @param b the board
     */
    public abstract void ApplyMove(Board b);

    /**
     * change an "unmoved" piece to the corresponding "moved" piece
     * this behaviour depends on the type of move, so it's called by the inherited classes
     * if necessary.
     * @param initialPiece the id of the "unmoved" piece
     * @return the id of the "moved" piece
     */
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
