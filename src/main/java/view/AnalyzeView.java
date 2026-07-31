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
        setBackground(Color.PINK);
        add(new JScrollPane(this.textArea));
        add(getButtonRow());
    }

    private JPanel getButtonRow() {
        final JPanel buttonRow = new JPanel(new GridLayout(1, 2));
        buttonRow.setBackground(Color.PINK);
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
        this.textArea.setBackground(Color.PINK);
    }

    private JButton getHistoryButton() {
        final JButton historyButton = new JButton("Message History");
        historyButton.addActionListener(click -> {
            // switch to one long history message
        });
        // button aesthetics
        historyButton.setBackground(Color.PINK);
        historyButton.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        historyButton.setForeground(Color.WHITE);
        historyButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        historyButton.setVisible(true);
        return historyButton;
    }

    private JButton getReturnButton() {
        final JButton returnButton = new JButton("Return");
        returnButton.addActionListener(click -> {
            // switch to the most recent message of the list
        });
        returnButton.setBackground(Color.PINK);
        returnButton.setFont(new Font(FONT, Font.PLAIN, TEXT_SIZE));
        returnButton.setForeground(Color.WHITE);
        returnButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        returnButton.setVisible(true);
        return returnButton;
    }

    private void addDisplayLabel() {
        final JLabel displayLabel = new JLabel("Analysis Metrics");
        displayLabel.setFont(new Font(FONT, Font.PLAIN, TITLE_SIZE));
        add(displayLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> this.textArea.setText((String) evt.getNewValue()));
    }

    /**
     * Triggers the analysis for the current turn.
     * @throws IOException if the analysis fails
     */
    public void executeTurnAnalysis() throws IOException {
        this.controller.executeTurnAnalysis();
    }

}

// View Cycle:
//
// Move is made (The Event)
// -> Controller calls Interactor.executeTurnAnalysis
// -> Adds message to the ViewModel through OutputBoundary
// -> Fires property change in ViewModel
// -> View.propertyChange changes JPanel TextArea in separate thread
