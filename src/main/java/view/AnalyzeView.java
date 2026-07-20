package view;

import interface_adapter.AnalyzeViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AnalyzeView extends JPanel implements PropertyChangeListener {
    private final JTextArea textArea = new JTextArea();
    final int WIDTH = 200;
    final int HEIGHT = 600;
    final int TEXT_SIZE = 24;

    public AnalyzeView(AnalyzeViewModel vm) {
        vm.addPropertyChangeListener(this); // Subscribing
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        add(new JScrollPane(textArea));
        addDisplayLabel();

    }

    private void addDisplayLabel() {
        JLabel displayLabel = new JLabel("Analysis Metrics");
        displayLabel.setFont(new Font("Arial", Font.PLAIN, TEXT_SIZE));
        add(displayLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingUtilities.invokeLater(() -> textArea.setText((String) evt.getNewValue()));
    }
}

// View Cycle:
//
// Move is made (The Event)
// -> Controller calls Interactor.executeTurnAnalysis
// -> Adds message to the ViewModel through OutputBoundary
// -> Fires property change in ViewModel
// -> View.propertyChange changes JPanel TextArea in separate thread
