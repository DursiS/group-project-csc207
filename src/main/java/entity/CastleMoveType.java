package entity;

import java.util.ArrayList;

public class CastleMoveType extends MoveType{

    //@Override
    //public boolean isNormalMove() {
    //    return false;
    //}

    @Override
    public Move createMove(int[] origin) {
        return null;
    }

    @Override
    public MoveType createMirroredMove() {
        return null;
    }

    @Override
    public void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin) {

    }
}
