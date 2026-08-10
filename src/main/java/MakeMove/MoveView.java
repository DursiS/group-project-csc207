package MakeMove;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MoveView extends JPanel implements PropertyChangeListener  {

    //amount of buttons in each dimension
    final int EIGHT = 8;
    //amount of pixels between edge of panel and board squares
    final int OUTER_MARGIN = 10;
    //amount of pixels between squares
    final int INNER_MARGIN = 5;
    private MoveViewModel vm;
    private MoveController con;
    JButton[][] buttons = new JButton[8][8];

    /**
     * create new Move View (graphical interface thing)
     * @param vm the view model
     * @param con the controller
     */
    public MoveView(MoveViewModel vm, MoveController con){
        this.vm = vm;
        vm.addPropertyChangeListener(this);
        this.con=con;

        setLayout(null);
        setPreferredSize(new Dimension(400,400));

        for (int y = 0; y < EIGHT; y++) {
            for (int x = 0; x < EIGHT; x++) {
                buttons[y][x] = new JButton(x + " " + y);
                buttons[y][x].setBounds(1,1,1,1);//will be resized dynamically when the user changes the window size
                buttons[y][x].setMargin(new Insets(0,0,0,0));

                //copy these to be effectively final for each button
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
                RefreshButtons();
            }
        });

        RefreshButtons();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RefreshButtons();
    }

    /**
     * update buttons visual appearance
     * also resizes them for increased accessibility
     * and compatibility with all monitor sizes.
     */
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

                buttons[y][x].setFont(new Font(buttons[y][x].getFont().getName(), buttons[y][x].getFont().getStyle(), (int)(sideLength*.75)));

            }
        }
    }
}
