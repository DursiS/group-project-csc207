package interface_adapter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class MoveViewModel {
    //copy from sean because im unfamiliar with this stuff lol
    Color square1 = new Color(240, 217, 181);
    Color square2 = new Color(181, 136, 99);
    Color selected = new Color(0, 120, 215);
    Color[] boardColours = {square1, square2};
    private Color[][] squareColours = new Color[8][8];

    public Color getSquareColour(int x, int y) {
        return squareColours[y][x];
    }

    private final PropertyChangeSupport support
            = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }





    private void updateColours(){
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squareColours[x][y] = boardColours[ (x + y) % 2];

            }
        }

    }

}
