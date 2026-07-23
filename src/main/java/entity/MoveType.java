//this is a data object,
//that just stores the information about the types of moves a piece can make. (not an actual move made by a player)
//it also tracks whether move types are a special move, in which case the movement code will have to activate some special case
//there is a list of MoveType for each piece,
//and the move validator checks all of them when checking for valid moves.

package entity;

public class MoveType {
    private int[] vector;
    private int maxRepeats;
    private boolean canCapture;
    private boolean canNotCapture;
    private boolean isEnPassant;//annoying special moves must be dealt with separately
    private boolean isCastle;
    //promoting pawn takes no move

    public MoveType(int[] vector, int maxRepeats){
        this.vector = vector;
        this.maxRepeats = maxRepeats;
        this.canCapture = true;
        this.canNotCapture = true;
        this.isEnPassant = false;
        this.isCastle = false;
    }

    public MoveType(int[] vector, int maxRepeats, boolean canCapture, boolean canNotCapture, boolean isEnPassant, boolean isCastle) {
        this.vector = vector;
        this.maxRepeats = maxRepeats;
        this.canCapture = canCapture;
        this.canNotCapture = canNotCapture;
        this.isEnPassant = isEnPassant;
        this.isCastle = isCastle;
    }

    public int[] getVector() {
        return vector;
    }

    public int getMaxRepeats() {
        return maxRepeats;
    }

    public boolean isCanCapture() {
        return canCapture;
    }

    public boolean isCanNotCapture(){
        return canNotCapture;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }

    public boolean isCastle() {
        return isCastle;
    }

    public boolean isNormalMove(){
        return !isCastle && !isEnPassant;
    }

    public static MoveType createMirroredMove(MoveType moveType){
        int[] newVector = new int[]{moveType.vector[0], -moveType.vector[1]};
        return new MoveType(newVector, moveType.maxRepeats, moveType.canCapture, moveType.canNotCapture, moveType.isEnPassant, moveType.isCastle);
    }
}