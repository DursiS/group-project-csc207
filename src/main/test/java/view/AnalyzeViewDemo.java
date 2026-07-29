package view;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import interface_adapter.AnalyzeController;
import interface_adapter.AnalyzeViewModel;

public class AnalyzeViewDemo {

    /**
     * Launches a small demo window for the analysis view.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        final AnalyzeViewModel viewModel = new AnalyzeViewModel();

        final AnalyzeController controller = new AnalyzeController(() -> {});

        final AnalyzeView view = new AnalyzeView(viewModel, controller);

        final JFrame mainFrame = new JFrame("Analyze View");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(view);
        mainFrame.pack();
        mainFrame.setVisible(true);

        // Only displays the most recent, good
        viewModel.addMessage("Demo Message #1");
        viewModel.addMessage("Demo Message #2");
        viewModel.addMessage("Demo Message #3");

    }
}
