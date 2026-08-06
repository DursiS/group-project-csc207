package entity;

public class NormalMoveType extends MoveType{
    private int maxRepeats;
    private boolean canCapture;
    private boolean canNotCapture;

    public NormalMoveType(int[] vector, int maxRepeats) {
        this.movementVector = vector;
        this.maxRepeats = maxRepeats;
        this.canCapture = true;
        this.canNotCapture = true;
    }
    public NormalMoveType(int[] vector, int maxRepeats, boolean canCapture, boolean canNotCapture) {
        this.movementVector = vector;
        this.maxRepeats = maxRepeats;
        this.canCapture = canCapture;
        this.canNotCapture = canNotCapture;
    }

    public boolean isCanCapture() {
        return canCapture;
    }

    public boolean isCanNotCapture() {
        return canNotCapture;
    }

    public int getMaxRepeats() {
        return maxRepeats;
    }

    @Override
    public boolean isNormalMove() {
        return true;
    }

    @Override
    public Move createMove(int[] origin) {
        return new NormalMove(origin, addVectors(origin, movementVector));
    }

    public Move createMove(int[] origin, int repeats) {
        return new NormalMove(origin, addVectors(origin, multiplyVector(movementVector, repeats+1)));
    }


    @Override
    public MoveType createMirroredMove() {
        return new NormalMoveType(mirrorVector(movementVector), maxRepeats, canCapture,canNotCapture);
    }
}
