package app;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.CountDownLatch;

import javax.swing.*;

import entity.Board;
import interface_adapter.AnalyzeController;
import interface_adapter.AnalyzePresenter;
import interface_adapter.AnalyzeViewModel;
import interface_adapter.ChessApiAdapter;
import org.junit.jupiter.api.Test;
import use_case.AnalyzeMoveInteractor;
import use_case.GameStateDataAccessInterface;
import view.AnalyzeView;

public class AnalyzeEndToEndTest {

    @Test
    void executeTurnAnalysisPresentsOnTheView() throws Exception {
        // random board
        Board board = new Board(new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1, 0,-1,-1,-1,-1},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 0, 0, 0,-2, 2, 0, 0, 0},
                { 0, 0, 0, 0, 0, 0, 0, 0},
                { 1, 1, 1, 1, 0, 1, 1, 1},
                { 4, 6, 7, 8, 9, 7, 6, 4}
        }, 5);

        // CA setup
        AnalyzeViewModel viewModel = new AnalyzeViewModel();
        AnalyzePresenter presenter = new AnalyzePresenter(viewModel);
        GameStateDataAccessInterface gameState = () -> board;
        AnalyzeMoveInteractor interactor =
                new AnalyzeMoveInteractor(new ChessApiAdapter(), presenter, gameState);
        AnalyzeController controller = new AnalyzeController(interactor);
        AnalyzeView view = new AnalyzeView(viewModel, controller);

        // will be the AppBuilder eventually that holds all the views
        final JFrame mainFrame = new JFrame("Analyze View");
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.add(view, BorderLayout.EAST);
        mainFrame.pack();
        mainFrame.setVisible(true);
        viewModel.addMessage("Initial Message");

        view.executeTurnAnalysis();

        // Fix I found to keep the test thread alive until you close the window
        // makes it not really a normal test, more interactive
        final CountDownLatch closed = new CountDownLatch(1);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                closed.countDown();
            }
        });
        closed.await();
    }
}
