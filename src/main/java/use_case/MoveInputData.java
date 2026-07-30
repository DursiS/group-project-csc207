package use_case;

public class MoveInputData {
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getVector(){
        return new int[]{x,y};
    }

    public MoveInputData(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
