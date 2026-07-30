package interface_adapter;

import use_case.MoveOutputBoundary;
import use_case.MoveOutputData;

import java.awt.*;

public class MovePresenter implements MoveOutputBoundary {
    private MoveViewModel moveViewModel;

    Color square1 = new Color(240, 217, 181);
    Color square2 = new Color(181, 136, 99);
    Color selected = new Color(0, 120, 215);
    Color moveable = new Color(0,215,120);
    Color[] boardColours = {square1, square2};


    public MovePresenter(MoveViewModel moveViewModel) {
        this.moveViewModel = moveViewModel;

    }

    @Override
    public void present(MoveOutputData data) {

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                moveViewModel.setSquareColour(x,y, boardColours[ (x + y) % 2]);
                moveViewModel.setSquareText(x,y, String.valueOf(data.getPieceType(x,y)));

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
