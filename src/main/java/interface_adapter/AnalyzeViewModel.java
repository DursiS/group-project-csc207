package interface_adapter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeViewModel {
    List<String> MESSAGE_HISTORY = new ArrayList<String>(); // Dependency Injection for DIP
    final int FROM = 0;
    final int TO = 2;

    /**
     * Builds the analysis panel with its label and text field.
     * @return the analysis panel
     */
    public JPanel display(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel displayLabel = new JLabel("Analysis Metrics");
        panel.add(displayLabel);

        JTextField textField = new JTextField(getDisplayText(), 15);
        panel.add(textField);

        return panel;
    }

    /**
     * Joins the most recent messages into a single display string.
     * @return the concatenated message text
     */
    private String getDisplayText(){
        StringBuilder result = new StringBuilder(); // Builder design pattern
        for (int i = FROM; i < TO; i++) {
            result.append(MESSAGE_HISTORY.get(i));
        }
        return result.toString();
    }

    /**
     * Adds a message to the history.
     * @param message the message to add
     */
    private void newMessage(String message) {
        this.MESSAGE_HISTORY.add(message);
    }

    /**
     * Removes the message at the given index.
     * @param index the index to remove
     * @return true if a message was removed, false otherwise
     */
    private boolean removeMessage(Integer index) {
        int i = 0;
        for  (String message : this.MESSAGE_HISTORY) {
            if (i == index) { this.MESSAGE_HISTORY.remove(message); }
            i += 1;
            return true;
        }
        return false;
    }

}
