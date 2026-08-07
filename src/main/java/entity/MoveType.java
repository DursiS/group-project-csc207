package entity;

import java.util.ArrayList;

/**
 * this class is an abstract class, the classes inheriting it represent the
 * types of possible moves pieces can make (like, the movemnt rules), and they're repsponsible for creating
 * the corresponding move objects
 */
public abstract class MoveType {
    protected int[] movementVector;

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


    /**
     *     //Add all possible moves resulting from this move type to the list.
     *     //implementation depends on the circumstances under which the possible moves are allowed.
     * @param moves moves list to add to
     * @param b current board
     * @param origin location of piece that's making the move
     */
    public abstract void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin);
}