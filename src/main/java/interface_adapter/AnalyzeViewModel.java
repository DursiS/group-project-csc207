package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeViewModel {

    private static final String ANALYSIS_PROPERTY = "analysis";

    private final List<String> messageHistory = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    /**
     * Adds a message to the history,
     * and fires a property change towards AnalyzeView to set textArea.
     * @param message the message to add
     */
    public void setMessage(String message) {
        if (!"".equals(message)) {
            this.messageHistory.add(message);
            this.support.firePropertyChange(ANALYSIS_PROPERTY, null, message);
        }
    }

    /**
     * Fires a property change with only the most recent message,
     * so the view shows the latest analysis.
     */
    public void setRecentMessage() {
        if (!this.messageHistory.isEmpty()) {
            final String message = this.messageHistory.get(this.messageHistory.size() - 1);
            this.support.firePropertyChange(ANALYSIS_PROPERTY, null, message);
        }
    }

    /**
     * Fires a property change with the whole history joined together,
     * so the view shows every past analysis in one message.
     */
    public void setHistoryMessage() {
        if (!this.messageHistory.isEmpty()) {
            final String message = String.join("\n\n", this.messageHistory);
            this.support.firePropertyChange(ANALYSIS_PROPERTY, null, message);
        }
    }

    /**
     * Registers a listener for analysis updates.
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }
}
