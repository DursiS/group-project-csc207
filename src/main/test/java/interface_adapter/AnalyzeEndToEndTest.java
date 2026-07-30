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

        System.out.println(mainFrame.getPreferredSize());


        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.add(view, BorderLayout.EAST);
        mainFrame.pack();
        mainFrame.setVisible(true);
        viewModel.addMessage("Initial Message");

        view.executeTurnAnalysis();
    }

    public static void main(String[] args) throws IOException {
        boardToViewModelDemo();
    }
}
