//this is a data type for a move that's made by a player
//or, for a valid move that a player can make

package entity;

public class Move {
    private int[] origin;
    private int[] destination;
    private Boolean isNormalMove;
    //private gameState

    public int[] getOrigin() {
        return origin;
    }

    public int[] getDestination() {
        return destination;
    }

    public Boolean getIsNormalMove() {
        return isNormalMove;
    }

    public Move(int[] origin, int[] destination) {
        this.origin = origin;
        this.destination = destination;
        this.isNormalMove = true;
    }

    //for debugging
    public String toString(){
        return "(" + origin[0] + "," + origin[1] + ") -> (" + destination[0] + "," + destination[1] + ")";
    }
}
