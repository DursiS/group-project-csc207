package Analysis;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import entity.Board;
import entity.BoardStateList;
import entity.GameState;

public class AnalyzeEndToEndTest {

    static void boardToViewModelDemo() throws IOException {
        final AnalyzeView view = getView();

        final JFrame mainFrame = new JFrame("Analyze View");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.add(view, BorderLayout.EAST);
        mainFrame.pack();
        mainFrame.setVisible(true);

        // The interactor reads a fresh board internally (getRecentBoard is a stub),
        // so each call analyzes the start position and the turn alternates by message count.
        view.executeTurnAnalysis();
        view.executeTurnAnalysis();
        view.executeTurnAnalysis();
    }

    private static AnalyzeView getView() {
        final AnalyzeViewModel viewModel = new AnalyzeViewModel();
        final AnalyzePresenter presenter = new AnalyzePresenter(viewModel);
        final GameState gameState =
                new GameState(new Board(), 0, 0, new BoardStateList(), "demo");
        final AnalyzeMoveInteractor interactor =
                new AnalyzeMoveInteractor(new ChessApiAdapter(), presenter, gameState);
        final AnalyzeController controller = new AnalyzeController(interactor);
        return new AnalyzeView(viewModel, controller);
    }

    public static void main(String[] args) throws IOException {
        boardToViewModelDemo();
    }
}
