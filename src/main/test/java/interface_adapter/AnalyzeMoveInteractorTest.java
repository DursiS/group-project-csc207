package interface_adapter;

import com.google.gson.JsonObject;
import entity.Board;
import org.junit.jupiter.api.Test;
import use_case.AnalyzeMoveInteractor;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyzeMoveInteractorTest {

    private static final String START =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private static final Board BOARD = new Board(); // Update with data later


    @Test
    void nonEmptyNewTurnAnalysis() throws Exception {
        ChessApiAdapter adapter = new ChessApiAdapter();
        AnalyzePresenter presenter = new AnalyzePresenter();
        AnalyzeMoveInteractor interactor = new AnalyzeMoveInteractor(
                adapter,
                presenter
        );

        String analysis = interactor.getFinalMessage(START);

        System.out.println(analysis);
        assertNotNull(analysis);
        assertFalse(analysis.isBlank());
    }
}
