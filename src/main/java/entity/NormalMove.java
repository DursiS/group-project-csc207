package entity;

public class NormalMove extends Move{

    @Override
    public Boolean getIsNormalMove() {
        return true;
    }

    public NormalMove(int[] origin, int[] destination){
        super(origin, destination);
    }
}
