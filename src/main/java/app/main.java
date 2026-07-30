package app;


//TEMPORARY
import interface_adapter.MoveViewModel;
import view.MoveView;

import javax.swing.JFrame;

public class main {
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addMoveView()
                .build();
        application.pack();
        application.setVisible(true);
    }
}
