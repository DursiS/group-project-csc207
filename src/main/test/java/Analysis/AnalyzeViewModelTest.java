package Analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AnalyzeViewModelTest {
    private AnalyzeViewModel viewModel;
    private AtomicReference<String> fired;

    @BeforeEach
    void setUp() {
        viewModel = new AnalyzeViewModel();
        fired = new AtomicReference<>();  // mocking the View's TextArea
        viewModel.addPropertyChangeListener("analysis", evt -> fired.set((String) evt.getNewValue()));
    }

    @Test
    void firesSetMessage() {
        viewModel.setMessage("hello");
        assertEquals("hello", fired.get());
    }

    @Test
    void doesntAddEmptyMessage() {
        viewModel.setMessage("");
        assertNull(fired.get());
    }

    @Test
    void addsMessage() {
        viewModel.setMessage("stored");
        viewModel.setRecentMessage();
        assertEquals("stored", fired.get());
    }

    @Test
    void firesRecentMessage() {
        viewModel.setMessage("first");
        viewModel.setMessage("second");
        viewModel.setRecentMessage();
        assertEquals("second", fired.get());
    }

    @Test
    void firesHistoryMessage() {
        viewModel.setMessage("a");
        viewModel.setMessage("b");
        viewModel.setHistoryMessage();
        assertEquals("a\n\nb", fired.get());
    }
}
