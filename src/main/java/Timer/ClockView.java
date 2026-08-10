package Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ClockView extends JPanel implements PropertyChangeListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 30;
    private static final int TEXT_SIZE = 30;
    private static final String FONT = "Arial";
    private static final Color COLOR = new Color(210, 180, 140);

    private JTextArea textArea;

    private final ClockViewModel viewModel;
    /**
     * Builds the analysis view and subscribes it to the view model.
     */
    public ClockView(ClockViewModel viewModel) {
        this.viewModel = viewModel;
        setupTextArea();

        applyPanelConfiguration();
    }

    /** Lays out the label, text area, and button row. */
    private void applyPanelConfiguration() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(COLOR);
    }

    /**
     * Updates the text area with the latest analysis message.
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> this.textArea.setText(viewModel.getTime()));
    }

    /** Initializes the read-only analysis text area. */
    private void setupTextArea() {
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setLineWrap(false);
        this.textArea.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        this.textArea.setBackground(COLOR);
        add(textArea);
    }

}
