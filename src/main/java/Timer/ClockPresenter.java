package Timer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ClockPresenter implements ClockOutputBoundary {
    private static final String PROPERTY_STRING = "clock-update";
    private static final int HOUR_MULTIPLIER = 3600000;
    private static final int MINUTE_MULTIPLIER = 60000;
    private static final int SECOND_MULTIPLIER = 1000;
    private static final int DECIMAL_CUTOFF = 2;
    private final ClockViewModel viewModel;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ClockPresenter(ClockViewModel viewModel){
        this.viewModel = viewModel;
    }

    public void updateTime(int time){
        String timeStr = "";
        if (time >= HOUR_MULTIPLIER){
            timeStr += time / HOUR_MULTIPLIER + ":";
        }
        if (time >= MINUTE_MULTIPLIER){
            timeStr += (time % HOUR_MULTIPLIER) / MINUTE_MULTIPLIER + ":";
        }
        timeStr += (time % MINUTE_MULTIPLIER) / SECOND_MULTIPLIER + ".";
        timeStr += time % SECOND_MULTIPLIER / (int)Math.pow(10, DECIMAL_CUTOFF);
        viewModel.setTime(timeStr);
        support.firePropertyChange(PROPERTY_STRING, null, time);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(PROPERTY_STRING, listener);
        support.firePropertyChange(PROPERTY_STRING, null, 82374);
    }
}
