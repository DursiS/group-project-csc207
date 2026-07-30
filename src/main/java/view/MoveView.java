package view;

import interface_adapter.MoveController;
import interface_adapter.MoveViewModel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MoveView extends JPanel implements PropertyChangeListener  {

    final int EIGHT = 8;
    final int OUTER_MARGIN = 10;
    final int INNER_MARGIN = 5;
    //final int BUTTON_SIZE = 50;
    //final int WIDTH = OUTER_MARGIN*2 + BUTTON_SIZE*EIGHT + INNER_MARGIN * (EIGHT-1);
    //final int HEIGHT = WIDTH;
    private MoveViewModel vm;
    private MoveController con;
    JButton[][] buttons = new JButton[8][8];


    public MoveView(MoveViewModel vm, MoveController con){
        this.vm = vm;
        vm.addPropertyChangeListener(this);
        this.con=con;



        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(null);
        setPreferredSize(new Dimension(400,400));



        for (int y = 0; y < EIGHT; y++) {
            for (int x = 0; x < EIGHT; x++) {
                buttons[y][x] = new JButton(x + " " + y);
                buttons[y][x].setBounds(1,1,1,1);
                //copy to effectively final for each button
                int finalX = x;
                int finalY = y;
                buttons[y][x].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        con.ReceiveClick(finalX, finalY);
                    }
                });
                this.add(buttons[y][x]);
            }
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Get the updated dimensions of the panel itself
                //int width = e.getComponent().getWidth();
                //int height = e.getComponent().getHeight();

                //System.out.println("Panel resized to: " + width + "x" + height);
                RefreshButtons();
            }
        });

        RefreshButtons();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("property change " + evt.getPropertyName());
        RefreshButtons();
    }

    private void RefreshButtons(){
        int width = this.getWidth();
        int height = this.getHeight();

        for (int y = 0; y < EIGHT; y++) {
            for (int x = 0; x < EIGHT; x++) {
                buttons[y][x].setBackground(vm.getSquareColour(x,y));
                buttons[y][x].setText(vm.getSquareText(x,y));


                int size = Math.min(width, height);

                int sideLength = (size - 7*INNER_MARGIN - 2*OUTER_MARGIN)/8;
                int totalLength = 7*INNER_MARGIN + 2*OUTER_MARGIN + 8*sideLength;

                int x_0 = (width-totalLength)/2 + OUTER_MARGIN;
                int y_0 = (height - totalLength)/2 + OUTER_MARGIN;




                buttons[y][x].setBounds(x_0 + (sideLength+INNER_MARGIN)*x,
                        y_0 + (sideLength+INNER_MARGIN)*y,
                        sideLength, sideLength);

            }
        }
    }
}
