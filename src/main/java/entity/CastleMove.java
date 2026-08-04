package entity;

public class CastleMove extends Move{
    public CastleMove(int[] origin, int[] destination) {
        super(origin, destination);
    }

    @Override
    public void ApplyMove(Board b) {

    }

    @Override
    public Boolean getIsNormalMove() {
        return false;
    }
}
