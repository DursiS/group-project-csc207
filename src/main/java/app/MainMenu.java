package app;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.util.function.IntConsumer;


public class MainMenu extends JPanel {
    public static final String VIEW_NAME = "Main Menu";

    private static final int STANDARD_TOPOLOGY = 0;
    private static final int WRAP_TOPOLOGY = 1;

    /**
     * @param onPlay called with the chosen topology when a play button is clicked
     * @param onBrowseSaved called when the user wants to see their saved games
     */
    public MainMenu(IntConsumer onPlay, Runnable onBrowseSaved) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        addCentered(new JLabel("Pick a board topology:"));
        addCentered(playButton("Standard Chess (0)", STANDARD_TOPOLOGY, onPlay));
        addCentered(playButton("Wrap-around (1)", WRAP_TOPOLOGY, onPlay));

        final JButton browse = new JButton("Saved Games");
        browse.addActionListener(click -> onBrowseSaved.run());
        addCentered(browse);
    }

    private JButton playButton(String label, int topology, IntConsumer onPlay) {
        final JButton button = new JButton(label);
        button.addActionListener(click -> onPlay.accept(topology));
        return button;
    }

    private void addCentered(JComponent component) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(component);
    }
}
