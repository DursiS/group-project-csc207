package interface_adapter;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import entity.Board;
import use_case.AnalyzeMoveInteractor;
import use_case.GameStateDataAccessInterface;
import view.AnalyzeView;

public class AnalyzeEndToEndTest {

    static void boardToViewModelDemo() throws IOException {
        // needed to put it in main, because test threading exists early
        final Board board = new Board();
        final AnalyzeView view = getView(board);

        // TODO: Replace with AppBuilder when ready
        final JFrame mainFrame = new JFrame("Analyze View");

        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.add(view, BorderLayout.EAST);
        mainFrame.pack();
        mainFrame.setVisible(true);

        // TODO: wire in with move signals later instead of manual executions
        // TODO: test real game data access retrieval

        // first move made to the board
        firstMove(board);
        view.executeTurnAnalysis();

        // second move
        secondMove(board);
        view.executeTurnAnalysis();

        // third move
        thirdMove(board);
        view.executeTurnAnalysis();
    }

    private static AnalyzeView getView(Board board) {
        // CA setup, sharing the one board through the game-state gateway
        AnalyzeViewModel viewModel = new AnalyzeViewModel();
        AnalyzePresenter presenter = new AnalyzePresenter(viewModel);
        GameStateDataAccessInterface gameState = () -> board;
        AnalyzeMoveInteractor interactor =
                new AnalyzeMoveInteractor(new ChessApiAdapter(), presenter, gameState);
        AnalyzeController controller = new AnalyzeController(interactor);
        return new AnalyzeView(viewModel, controller);
    }

    // 1. e4 : white pawn e2 -> e4. Kept as a moved pawn (2), not en-passant
    // because the chess API rejects any FEN that carries an en-passant square.
    private static void firstMove(Board board) {
        board.setSquare(4, 6, 0);
        board.setSquare(4, 4, 2);
        board.incrementTurn();
    }

    // 1... e5 : black pawn e7 -> e5
    private static void secondMove(Board board) {
        board.setSquare(4, 1, 0);
        board.setSquare(4, 3, -2);
        board.incrementTurn();
    }

    // 2. Nf3 : white knight g1 -> f3
    private static void thirdMove(Board board) {
        board.setSquare(6, 7, 0);
        board.setSquare(5, 5, 6);
        board.incrementTurn();
    }

    public static void main(String[] args) throws IOException {
        boardToViewModelDemo();
    }
}
