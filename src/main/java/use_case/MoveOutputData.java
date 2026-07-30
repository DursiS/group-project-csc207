package use_case;

public class MoveOutputData {
    private int[][] pieceTypes;
    private int[][] squareVisuals;

    public MoveOutputData(int[][] pieceTypes, int[][] squareVisuals) {
        this.pieceTypes = pieceTypes;
        this.squareVisuals = squareVisuals;
    }

    public int getPieceType(int x, int y){
        return pieceTypes[y][x];
    }
    public int getSquareVisual(int x, int y){
        return squareVisuals[y][x];
    }
}
