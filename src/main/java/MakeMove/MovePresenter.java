package MakeMove;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MovePresenter implements MoveOutputBoundary {
    private MoveViewModel moveViewModel;

    private Color square1 = new Color(240, 217, 181);
    private Color square2 = new Color(181, 136, 99);
    private Color selected = new Color(0, 120, 215);
    private Color moveable = new Color(0,215,120);
    private Color[] boardColours = {square1, square2};
    private Map<Integer, String> pieceStrings = new HashMap<>(Map.ofEntries(
            Map.entry(0,""),
            Map.entry(1,"♙"),
            Map.entry(2,"♙"),
            Map.entry(3,"♙"),
            Map.entry(4,"♖"),
            Map.entry(5,"♖"),
            Map.entry(6,"♘"),
            Map.entry(7,"♗"),
            Map.entry(8,"♕"),
            Map.entry(9,"♔"),
            Map.entry(10,"♔"),
            Map.entry(-1,"♟"),
            Map.entry(-2,"♟"),
            Map.entry(-3,"♟"),
            Map.entry(-4,"♜"),
            Map.entry(-5,"♜"),
            Map.entry(-6,"♞"),
            Map.entry(-7,"♝"),
            Map.entry(-8,"♛"),
            Map.entry(-9,"♚"),
            Map.entry(-10,"♚")
    )
    );

    /**
     *create a move presenter
     * @param moveViewModel the view model it's assigned to
     */
    public MovePresenter(MoveViewModel moveViewModel) {
        this.moveViewModel = moveViewModel;
    }

    /**
     * present the data to the view model, and fire it
     * @param data the output data to present
     */
    @Override
    public void present(MoveOutputData data) {

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                moveViewModel.setSquareColour(x,y, boardColours[ (x + y) % 2]);
                moveViewModel.setSquareText(x,y, this.pieceStrings.get(data.getPieceType(x,y)));

                if (data.getSquareVisual(x,y) == 1){
                    moveViewModel.setSquareColour(x,y, selected);
                }
                if (data.getSquareVisual(x,y) == 2){
                    moveViewModel.setSquareColour(x,y, moveable);
                }

            }
        }

        moveViewModel.fire();
    }
}
