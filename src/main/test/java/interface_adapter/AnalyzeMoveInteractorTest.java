package interface_adapter;

import entity.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.AnalyzeMoveInteractor;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyzeMoveInteractorTest {

    private static String START_FEN;
    private static String OTHER_FEN;
    private static Board BOARD;
    private static Board OTHER_BOARD;
    private static AnalyzeMoveInteractor interactor;

    @BeforeEach
    void setUp() {
        ChessApiAdapter adapter = new ChessApiAdapter();
        AnalyzePresenter presenter = new AnalyzePresenter();
        interactor = new AnalyzeMoveInteractor(
                adapter,
                presenter
        );

        START_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        OTHER_FEN = "rnbqkbnr/ppp1pppp/8/8/3pP3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";

        BOARD = new Board();
        OTHER_BOARD = new Board(new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1, 0,-1,-1,-1,-1},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0,-2, 3, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 1, 1, 1, 1, 0, 1, 1, 1},
                { 4, 6, 7, 8, 9, 7, 6, 4}
        }, 5);
    }

    @Test
    void nonEmptyNewTurnAnalysis() throws Exception {


        String analysis = interactor.getFinalMessage(START_FEN);

        System.out.println(analysis);
        assertNotNull(analysis);
        assertFalse(analysis.isBlank());
    }

    @Test
    void convertFen() {
        String convertedStartFen = interactor.convertToFen(BOARD);
        assertEquals(START_FEN, convertedStartFen);

        String convertedRandomFen = interactor
                .convertToFen(OTHER_BOARD);
        assertEquals(OTHER_FEN, convertedRandomFen);
    }
}
