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

    public abstract Boolean getIsNormalMove();

    public Move(int[] origin, int[] destination) {
        this.origin = origin;
        this.destination = destination;
    }

    //for debugging
    public String toString(){
        return "(" + origin[0] + "," + origin[1] + ") -> (" + destination[0] + "," + destination[1] + ")";
    }

    public abstract void ApplyMove(Board b);
}
