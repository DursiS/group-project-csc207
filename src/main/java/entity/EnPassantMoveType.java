package entity;

import java.util.ArrayList;

import static entity.MoveValidator.applyQuotientRelation;

public class EnPassantMoveType extends MoveType {
    int[] captureVector;

    /**
     * create new en passant move type (the "blueprint" for the actual move)
     * @param movementVector vector from the intial location to final location
     * @param captureVector vector from initial location to location of the piece being captured
     */
    public EnPassantMoveType(int[] movementVector, int[] captureVector) {
        this.movementVector = movementVector;
        this.captureVector = captureVector;
    }

    /**
     * creates move from the MoveType
     * @param origin initial location of the piece
     * @return the Move
     */
    public EnPassantMove createMove(int[] origin) {
        return new EnPassantMove(origin, addVectors(origin,movementVector), addVectors(origin,captureVector));
    }

    /**
     * create equivalent version of black move for the white pieces
     * @return the move
     */
    @Override
    public MoveType createMirroredMove() {
        return new EnPassantMoveType(mirrorVector(movementVector), mirrorVector(captureVector));
    }

    /**
     * add the possible move corresponding to this move type to the moves list,
     * if any exist.
     * @param moves the list to add to
     * @param b the current board
     * @param origin the location of the piece being moved
     */
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
