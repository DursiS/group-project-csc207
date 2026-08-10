package Analysis;

import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnalyzeViewTest {

    private static void flushEventQueue() throws Exception {
        SwingUtilities.invokeAndWait(() -> { });
    }

    private static void clickAllButtons(Container container) {
        for (Component child : container.getComponents()) {
            if (child instanceof JButton) {
                ((JButton) child).doClick();
            }
            else if (child instanceof Container) {
                clickAllButtons((Container) child);
            }
        }
    }

    @Test
    void viewRendersMessageAndButtonsDriveController() throws Exception {
        AnalyzeViewModel viewModel = new AnalyzeViewModel();
        AnalyzeInputBoundary boundary = mock(AnalyzeInputBoundary.class);
        AnalyzeController controller = new AnalyzeController(boundary);
        AnalyzeView view = new AnalyzeView(viewModel, controller);

        view.executeTurnAnalysis();

        viewModel.setMessage("analysis output");
        flushEventQueue();

        clickAllButtons(view);

        verify(boundary).executeTurnAnalysis();
        verify(boundary).executeMessageHistoryDisplay();
        verify(boundary).executeSingleMessageDisplay();
    }
}
