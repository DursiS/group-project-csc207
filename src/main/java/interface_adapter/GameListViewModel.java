package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;

public class GameListViewModel {

    private Object[][] data;
    private UUID[] ids;
    private String errorMessage = null;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GameListViewModel() {}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this);
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public UUID[] getIds() {
        return ids;
    }

    public void setIds(UUID[] ids) {
        this.ids = ids;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
