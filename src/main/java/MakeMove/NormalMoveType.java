package MakeMove;

import java.util.ArrayList;

import static MakeMove.MoveValidator.applyQuotientRelation;

/**
 * Normal Move Type class
 * a member of this class describes a rule set that Normal Moves can be created based off of.
 */
public class NormalMoveType extends MoveType{
    private int maxRepeats;
    private boolean canCapture;
    private boolean canNotCapture;

    /**
     * constructor for normal moves that can either capture or not capture
     * @param vector movement vector
     * @param maxRepeats max amount of times it can repeat, e.g. horses never repeat, pawns repeat 1 time on first move, queen has unlimited repeats
     */
    public NormalMoveType(int[] vector, int maxRepeats) {
        this.movementVector = vector;
        this.maxRepeats = maxRepeats;
        this.canCapture = true;
        this.canNotCapture = true;
    }

    /**
     * general constructor for normal moves
     * @param vector same
     * @param maxRepeats same
     * @param canCapture if the piece is allowed to capture e.g. pawn's diagonal moves
     * @param canNotCapture if the piece is allowed to capture e.g. pawn's straight moves
     */
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

    /**
     * create the move from this move type
     * @param origin location of piece
     * @param repeats amount of repeats of the movement vector
     * @param b the board (board topology can affect the move)
     * @return the move
     */
    public Move createMove(int[] origin, int repeats, Board b) {
        int[] destination = addVectors(origin, multiplyVector(movementVector, repeats+1));
        destination = applyQuotientRelation(destination, b);
        return new NormalMove(origin, destination);
    }

    /**
     * create a new move type which is this one but mirrored vertically
     * @return mirrored move type
     */
    @Override
    public MoveType createMirroredMove() {
        return new NormalMoveType(mirrorVector(movementVector), maxRepeats, canCapture,canNotCapture);
    }

    /**
     * adds possible moves from this move type into the list (can add multiple, if the move type repeats.)
     * @param moves moves list to add to
     * @param b current board
     * @param origin location of piece that's making the move
     */
    @Override
    public void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin) {
        int[] destination;
        int repeats = 0;
        do{
            destination = createMove(origin, repeats, b).getDestination();
            //destination = applyQuotientRelation(destination, b);
            //initial validity check:
            //don't allow move if it results in an invalid location
            //don't allow move if it loops back to the piece's original location due to an alternate board topology
            //(this is allowed in some implementations of chess but we disable it for simplicity.)

            if(destination == null || (destination[0] == origin[0] && destination[1] == origin[1])){
                break;
            }else{
                //allow move if the move captures and square is occupied by an enemy
                //also allow move if the move isn't required to capture and square is empty
                if(b.isSquareEnemy(destination[0], destination[1])){
                    if (isCanCapture()){
                        moves.add(createMove(origin, repeats, b));
                    }
                    break;
                    //can't move past an enemy.
                }
                else if(isCanNotCapture() && b.isSquareEmpty(destination))
                {
                    moves.add(createMove(origin, repeats, b));
                    //can keep moving if the square was empty, so don't break.
                }else if( !b.isSquareEmptyOrEnemy(destination[0],destination[1]))
                {
                    //if square is occupied by an ally, don't allow movement through it
                    break;
                }
            }
            repeats += 1;
        }while(repeats <= getMaxRepeats()) ;
    }
}
