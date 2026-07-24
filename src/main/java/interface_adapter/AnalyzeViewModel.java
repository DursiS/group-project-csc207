package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeViewModel {

    private final List<String> messageHistory = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Adds a message to the history,
     * and fires a property change towards AnalyzeView.
     * @param message the message to add
     */
    public void addMessage(String message) {
        this.messageHistory.add(message);
        this.support.firePropertyChange("analysis", null, message);
    }

    /**
     * Registers a listener for analysis updates.
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }
}
