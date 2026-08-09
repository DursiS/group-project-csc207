package Analysis;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class AnalyzeControllerTest {

    @Test
    void executeTurnAnalysisDelegates() throws IOException {
        AnalyzeInputBoundary boundary = mock(AnalyzeInputBoundary.class);
        AnalyzeController controller = new AnalyzeController(boundary);

        controller.executeTurnAnalysis();

        verify(boundary).executeTurnAnalysis();
    }

    @Test
    void executeSingleMessageDisplayDelegates() {
        AnalyzeInputBoundary boundary = mock(AnalyzeInputBoundary.class);
        AnalyzeController controller = new AnalyzeController(boundary);

        controller.executeSingleMessageDisplay();

        verify(boundary).executeSingleMessageDisplay();
    }

    @Test
    void executeMessageHistoryDisplayDelegates() {
        AnalyzeInputBoundary boundary = mock(AnalyzeInputBoundary.class);
        AnalyzeController controller = new AnalyzeController(boundary);

        controller.executeMessageHistoryDisplay();

        verify(boundary).executeMessageHistoryDisplay();
    }
}
