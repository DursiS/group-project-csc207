package view;

import interface_adapter.MoveController;
import interface_adapter.MoveViewModel;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MoveView extends JPanel implements PropertyChangeListener  {

    final int EIGHT = 8;
    final int OUTER_MARGIN = 10;
    final int INNER_MARGIN = 5;
    final int BUTTON_SIZE = 50;
    final int WIDTH = OUTER_MARGIN*2 + BUTTON_SIZE*EIGHT + INNER_MARGIN * (EIGHT-1);
    final int HEIGHT = WIDTH;
    private MoveViewModel vm;
    private MoveController con;
    JButton[][] buttons = new JButton[8][8];


    public MoveView(MoveViewModel vm, MoveController con){
        this.vm = vm;
        vm.addPropertyChangeListener(this);
        this.con=con;



        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));


        for (int y = 0; y < EIGHT; y++) {
            for (int x = 0; x < EIGHT; x++) {
                buttons[y][x] = new JButton(x + " " + y);
                buttons[y][x].setBounds(OUTER_MARGIN + (BUTTON_SIZE+INNER_MARGIN)*x,
                        OUTER_MARGIN + (BUTTON_SIZE+INNER_MARGIN)*y,
                        BUTTON_SIZE, BUTTON_SIZE);
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




    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("property change " + evt.getPropertyName());
        RefreshButtons();
    }

    private void RefreshButtons(){
        for (int y = 0; y < EIGHT; y++) {
            for (int x = 0; x < EIGHT; x++) {
                buttons[y][x].setBackground(vm.getSquareColour(x,y));
            }
        }
    }
}
