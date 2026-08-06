package entity;

public class EnPassantMoveType extends MoveType {
    int[] captureVector;

    public EnPassantMoveType(int[] movementVector, int[] captureVector) {
        this.movementVector = movementVector;
        this.captureVector = captureVector;
    }

    @Override
    public boolean isNormalMove() {
        return false;
    }

    @Override
    public Move createMove(int[] origin) {
        return null;
        //TODO
    }

    @Override
    public MoveType createMirroredMove() {
        return new EnPassantMoveType(mirrorVector(movementVector), mirrorVector(captureVector));
    }
}
