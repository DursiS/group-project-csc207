package entity;

public class BoardTopology {
    //0: impassible wall (chess default), 1: passible and identified with the opposite side, 2: passible and identified with the other side with reversed orientation (this one is less interesting so we don't have to implement it).
    private int verticalEdgeType;
    private int horizontalEdgeType;

    public BoardTopology(int verticalEdgeType, int horizontalEdgeType) {
        this.verticalEdgeType = verticalEdgeType;
        this.horizontalEdgeType = horizontalEdgeType;
    }

    public int getVerticalEdgeType() {
        return verticalEdgeType;
    }

    public int getHorizontalEdgeType() {
        return horizontalEdgeType;
    }
}
