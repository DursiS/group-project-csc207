//this is a data type for a move that's made by a player
//or, for a valid move that a player can make

package entity;

public class Move {
    private int[] origin;
    private int[] destination;


    public int[] getOrigin() {
        return origin;
    }

    public int[] getDestination() {
        return destination;
    }

    public Move(int[] origin, int[] destination) {
        this.origin = origin;
        this.destination = destination;
    }
}
