package Analysis;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnalyzePresenterTest {
    static AnalyzeOutputData outputData;
    static AnalyzeViewModel viewModel;
    static AnalyzePresenter presenter;
    static String formatted_message;

    @BeforeAll
    static void setUp() {
        outputData = new AnalyzeOutputData(39.0,
                0.49,
                "e4",
                "e5",
                true,
                1
        );
        viewModel = new AnalyzeViewModel();
        presenter = new AnalyzePresenter(viewModel);
        formatted_message = "== WHITE'S TURN  (1) ==\n\n"
                + "White WinChance: 39.0% \nWhite Eval: 0.49\n"
                + "Best Move: e4 -> e5\n\n";
    }

    @Test
    void testAnalyzePresenterMakeMessage() {
        String message = presenter.makeMessage(outputData);
        assertEquals(formatted_message, message);
    }

    @Test
    void testAnalyzePresenterMakeMessageBlackTurn() {
        AnalyzeOutputData blackData = new AnalyzeOutputData(60.0,
                0.30,
                "d2",
                "d4",
                false,
                2
        );
        String message = presenter.makeMessage(blackData);
        assertTrue(message.startsWith("== BLACK'S TURN  (2) =="));
        assertTrue(message.contains("Black WinChance:"));
        assertTrue(message.contains("Black Eval:"));
        assertTrue(message.contains("Best Move: d2 -> d4"));
    }
}
