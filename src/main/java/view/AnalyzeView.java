package view;

import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import interface_adapter.AnalyzeController;
import interface_adapter.AnalyzeViewModel;

public class AnalyzeView extends JPanel implements PropertyChangeListener {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 600;
    private static final int TEXT_SIZE = 24;

    private final JTextArea textArea = new JTextArea();
    private final AnalyzeController controller;

    public AnalyzeView(AnalyzeViewModel viewModel, AnalyzeController controller) {
        this.controller = controller;
        // Subscribing
        viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(new JScrollPane(this.textArea));
        addDisplayLabel();
    }

    private void addDisplayLabel() {
        final JLabel displayLabel = new JLabel("Analysis Metrics");
        displayLabel.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        add(displayLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> this.textArea.setText((String) evt.getNewValue()));
    }

    /**
     * Triggers the analysis for the current turn.
     * @throws Exception if the analysis fails
     */
    public void executeTurnAnalysis() throws Exception {
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
