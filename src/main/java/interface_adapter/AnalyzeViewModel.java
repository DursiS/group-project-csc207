package interface_adapter;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeViewModel {

    // Dependency Injection agrees with DIP
    List<String> MESSAGE_HISTORY = new ArrayList<>();
    private final PropertyChangeSupport support
            = new PropertyChangeSupport(this)

    /**
     * Adds a message to the history,
     * and fires a property change towards AnalyzeView.
     * @param message the message to add
     */
    public void addMessage(String message) {
        this.MESSAGE_HISTORY.add(message);

        support.firePropertyChange("analysis", null, message);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
