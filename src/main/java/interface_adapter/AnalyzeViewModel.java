package interface_adapter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeViewModel {
    List<String> MESSAGE_HISTORY = new ArrayList<String>(); // Dependency Injection for DIP
    final int FROM = 0;
    final int TO = 2;

    public JPanel display(){

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel displayLabel = new JLabel("Analysis Metrics");
        panel.add(displayLabel);

        JTextField textField = new JTextField(getDisplayText(), 15);
        panel.add(textField);

        return panel;
    }

    private String getDisplayText(){
        int i = FROM;
        String result = "";
        while(i < TO){
            result = result + MESSAGE_HISTORY.get(i);
            i += 1;
        }
        return result;
    }

    private void newMessage(String message) {
        this.MESSAGE_HISTORY.add(message);
    }

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
