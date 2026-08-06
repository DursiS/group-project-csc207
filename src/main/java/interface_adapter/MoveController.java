package interface_adapter;

import use_case.MoveInputBoundary;
import use_case.MoveInputData;

public class MoveController {
    private MoveInputBoundary moveInputBoundary;

    public MoveController(MoveInputBoundary moveInputBoundary) {
        this.moveInputBoundary = moveInputBoundary;
    }

    public void ReceiveClick(int x, int y){
        //updateColours();
        //DO STUFF HERE!!!!!
        //support.firePropertyChange("update Move View", null,null);
        //System.out.println("receiving click at " +x + " "+y);

        MoveInputData data = new MoveInputData(x,y);
        moveInputBoundary.receiveMove(data);
    }
}
