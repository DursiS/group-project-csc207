package entity;

import java.util.ArrayList;

import static entity.MoveValidator.applyQuotientRelation;

public class EnPassantMoveType extends MoveType {
    int[] captureVector;

    public EnPassantMoveType(int[] movementVector, int[] captureVector) {
        this.movementVector = movementVector;
        this.captureVector = captureVector;
    }

    //@Override
    //public boolean isNormalMove() {
    //    return false;
    //}

    @Override
    public EnPassantMove createMove(int[] origin) {
        return new EnPassantMove(origin, addVectors(origin,movementVector), addVectors(origin,captureVector));
    }

    @Override
    public MoveType createMirroredMove() {
        return new EnPassantMoveType(mirrorVector(movementVector), mirrorVector(captureVector));
    }

    @Override
    public void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin) {
        int[] destination = applyQuotientRelation(createMove(origin).getDestination(), b);
        int[] capture = applyQuotientRelation(createMove(origin).getCapture(), b);
        if(destination==null || capture==null){
            return;
        }

        if(b.isSquareEmpty(createMove(origin).getDestination())
                && b.isSquareEnemy(createMove(origin).getCapture())){
            moves.add(createMove(origin));
        }

    }
}
