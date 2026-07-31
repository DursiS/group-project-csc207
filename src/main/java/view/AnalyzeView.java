package view;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.*;

import interface_adapter.AnalyzeController;
import interface_adapter.AnalyzeViewModel;

public class AnalyzeView extends JPanel implements PropertyChangeListener {
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;
    private static final int TITLE_SIZE = 24;
    private static final int TEXT_SIZE = 16;
    private static final int BUTTON_HEIGHT = 40;
    private static final String FONT = "Arial";
    private static final Color COLOR = new Color(210, 180, 140);
    private static final Color TEXT_COLOR = Color.BLACK;

    private JTextArea textArea;
    private final AnalyzeController controller;

    public AnalyzeView(AnalyzeViewModel viewModel, AnalyzeController controller) {
        // Setup
        this.controller = controller;
        setupTextArea();
        viewModel.addPropertyChangeListener(this);

        // Panel Config
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> this.textArea.setText((String) evt.getNewValue()));
    }

    private JPanel getButtonRow() {
        final JPanel buttonRow = new JPanel(new GridLayout(1, 2));
        buttonRow.setBackground(COLOR);
        buttonRow.add(getHistoryButton());
        buttonRow.add(getReturnButton());
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, BUTTON_HEIGHT));
        return buttonRow;
    }

    private void setupTextArea() {
        this.textArea = new JTextArea();
        this.textArea.setEditable(false);
        this.textArea.setLineWrap(true);
        this.textArea.setWrapStyleWord(true);
        this.textArea.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        this.textArea.setBackground(COLOR);
    }

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

    private void addDisplayLabel() {
        final JLabel displayLabel = new JLabel("Analysis Metrics");
        displayLabel.setFont(new Font(FONT, Font.PLAIN, TITLE_SIZE));
        displayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(displayLabel);
    }

}

// View Cycle:
//
// Move is made (The Event)
// -> Controller calls Interactor.executeTurnAnalysis
// -> Adds message to the ViewModel through OutputBoundary
// -> Fires property change in ViewModel
// -> View.propertyChange changes JPanel TextArea in separate thread
