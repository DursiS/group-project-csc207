package entity;

public class EnPassantMove extends Move {
    public EnPassantMove(int[] origin, int[] destination) {
        super(origin, destination);
    }

    @Override
    public Boolean getIsNormalMove() {
        return false;
    }
}
