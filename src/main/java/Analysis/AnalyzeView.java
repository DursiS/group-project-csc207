package Analysis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class AnalyzeView extends JPanel implements PropertyChangeListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    private static final int TITLE_SIZE = 24;
    private static final int TEXT_SIZE = 16;
    private static final int BUTTON_HEIGHT = 40;
    private static final String FONT = "Arial";
    private static final Color COLOR = new Color(210, 180, 140);
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final String ANALYSIS_PROPERTY = "analysis";

    private JTextArea textArea;
    private final AnalyzeController controller;

    /**
     * Builds the analysis view and subscribes it to the view model.
     * @param viewModel the view model to observe
     * @param controller the controller to call
     */
    public AnalyzeView(AnalyzeViewModel viewModel, AnalyzeController controller) {
        this.controller = controller;
        setupTextArea();

        // subscribing the ViewModel
        viewModel.addPropertyChangeListener(ANALYSIS_PROPERTY, this);
        applyPanelConfiguration();
    }

    /** Lays out the label, text area, and button row. */
    private void applyPanelConfiguration() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addDisplayLabel();
        setBackground(COLOR);
        final JScrollPane scrollPane = new JScrollPane(this.textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(scrollPane);
        add(getButtonRow());
    }

    /**
     * SRP
     * Triggers the analysis for the current turn.
     * @throws IOException if the analysis fails
     */
    public void executeTurnAnalysis() throws IOException {
        this.controller.executeTurnAnalysis();
    }

    /**
     * Updates the text area with the latest analysis message.
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> this.textArea.setText((String) evt.getNewValue()));
    }

    /**
     * Builds the row holding the two buttons.
     * @return the button row panel
     */
    private JPanel getButtonRow() {
        final JPanel buttonRow = new JPanel(new GridLayout(1, 2));
        buttonRow.setBackground(COLOR);
        buttonRow.add(getHistoryButton());
        buttonRow.add(getReturnButton());
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_HEIGHT));
        return buttonRow;
    }

    /** Initializes the read-only analysis text area. */
    private void setupTextArea() {
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        this.textArea.setBackground(COLOR);
    }

    /**
     * Builds the message-history button.
     * @return the history button
     */
    private JButton getHistoryButton() {
        final JButton historyButton = new JButton("Message History");
        historyButton.addActionListener(click -> {
            this.controller.executeMessageHistoryDisplay();
        });
        historyButton.setBackground(COLOR);
        historyButton.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        historyButton.setForeground(TEXT_COLOR);
        historyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        historyButton.setVisible(true);
        return historyButton;
    }

    /**
     * Builds the return button.
     * @return the return button
     */
    private JButton getReturnButton() {
        final JButton returnButton = new JButton("Return");
        returnButton.addActionListener(click -> {
            this.controller.executeSingleMessageDisplay();
        });
        returnButton.setBackground(COLOR);
        returnButton.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        returnButton.setForeground(TEXT_COLOR);
        returnButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        returnButton.setVisible(true);
        return returnButton;
    }

    /** Adds the centered title label. */
    private void addDisplayLabel() {
        final JLabel displayLabel = new JLabel("Analysis Metrics");
        displayLabel.setFont(new Font(FONT, Font.PLAIN, TITLE_SIZE));
        displayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(displayLabel);
    }

}
