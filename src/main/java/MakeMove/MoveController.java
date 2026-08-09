package MakeMove;

public class MoveController {
    private MoveInputBoundary moveInputBoundary;

    /**
     * default constructor
     * @param moveInputBoundary the input boundary to send data to once a click happens
     */
    public MoveController(MoveInputBoundary moveInputBoundary) {
        this.moveInputBoundary = moveInputBoundary;
    }

    /**
     * this gets called when the buttons are clicked
     * @param x x position of square corresponding to the button
     * @param y y position of square corresponding to the button
     */
    public void ReceiveClick(int x, int y){
        MoveInputData data = new MoveInputData(x,y);
        moveInputBoundary.receiveInput(data);
    }
}
