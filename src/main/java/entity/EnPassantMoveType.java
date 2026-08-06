package entity;

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
}
