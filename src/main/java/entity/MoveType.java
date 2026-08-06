//this is a data object, for validation purposes only
//that just stores the information about the types of moves a piece can make. (not an actual move made by a player)
//it also tracks whether move types are a special move, in which case the movement code will have to activate some special case
//there is a list of MoveType for each piece,
//and the move validator checks all of them when checking for valid moves.

package entity;

import java.util.ArrayList;

public abstract class MoveType {
    protected int[] movementVector;//how the actual piece moves

    //public abstract Move createMove(int[] origin);

    public static int[] mirrorVector(int[] vector){
        return new int[]{vector[0], -vector[1]};
    }

    public static int[] addVectors(int[] v1, int[] v2){
        return new int[]{v1[0] + v2[0], v1[1] + v2[1]};
    }

    public static int[] multiplyVector(int[] v, int n){
        return new int[]{v[0]*n, v[1]* n};
    }

    public abstract MoveType createMirroredMove();

    //Add all possible moves resulting from this move type to the list.
    //implementation depends on the circumstances under which the possible moves are allowed.
    public abstract void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin);
}