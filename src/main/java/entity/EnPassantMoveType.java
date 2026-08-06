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
        //System.out.println(d);
        if(b.isSquareEmpty(destination)
                && b.isSquareEnemy(capture)
                &&b.getSquare(capture) == 3 || b.getSquare(capture) == -3 ){
            moves.add(createMove(origin));
        }
    }
}
