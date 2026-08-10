package app;

import javax.swing.JPanel;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Swaps the visible card in a CardLayout whenever the ViewManagerModel's
 * current view changes. This is the only place that knows about card switching.
 */
public class ViewManager implements PropertyChangeListener {

    private final JPanel views;
    private final CardLayout cardLayout;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("view".equals(evt.getPropertyName())) {
            this.cardLayout.show(this.views, (String) evt.getNewValue());
        }
    }
}
