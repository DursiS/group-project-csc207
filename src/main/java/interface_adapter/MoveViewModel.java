package interface_adapter;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MoveViewModel {

    private Color[][] squareColours = new Color[8][8];
    private String[][] squareTexts = new String[8][8];

    public Color getSquareColour(int x, int y) {
        return squareColours[y][x];
    }

    public String getSquareText(int x, int y) {
        return squareTexts[y][x];
    }

    public void setSquareColour(int x, int y, Color c) {
        squareColours[y][x] = c;
    }

    public void setSquareText(int x, int y, String t) {
        squareTexts[y][x] = t;
    }

    private final PropertyChangeSupport support
            = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * updates the move view (this is called by presenter)
     */
    public void fire(){
        support.firePropertyChange("update Move View", null,null);
    }
}
