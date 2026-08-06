package entity;

import java.util.ArrayList;

import static entity.MoveValidator.applyQuotientRelation;

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

    //@Override
    //public Move createMove(int[] origin) {
    //    return new NormalMove(origin, addVectors(origin, movementVector));
    //}

    public Move createMove(int[] origin, int repeats) {
        return new NormalMove(origin, addVectors(origin, multiplyVector(movementVector, repeats+1)));
    }


    @Override
    public MoveType createMirroredMove() {
        return new NormalMoveType(mirrorVector(movementVector), maxRepeats, canCapture,canNotCapture);
    }

    @Override
    public void AddPossibleMoves(ArrayList<Move> moves, Board b, int[] origin) {
        int[] destination;
        int repeats = 0;
        do{
            destination = createMove(origin, repeats).getDestination();
            destination = applyQuotientRelation(destination, b);
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
                        moves.add(createMove(origin, repeats));
                    }
                    break;
                    //can't move past an enemy.
                }
                else if(isCanNotCapture() && b.isSquareEmpty(destination))
                {
                    moves.add(createMove(origin, repeats));
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
