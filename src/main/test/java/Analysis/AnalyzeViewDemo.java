package Analysis;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class AnalyzeViewDemo {

    /**
     * Launches a small demo window for the analysis view.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        final AnalyzeViewModel viewModel = new AnalyzeViewModel();

        // No-op boundary: this demo only showcases the view, so the buttons do nothing.
        final AnalyzeInputBoundary noOp = new AnalyzeInputBoundary() {
            @Override
            public void executeTurnAnalysis() {
            }

            @Override
            public void executeSingleMessageDisplay() {
            }

            @Override
            public void executeMessageHistoryDisplay() {
            }
        };
        final AnalyzeController controller = new AnalyzeController(noOp);

        final AnalyzeView view = new AnalyzeView(viewModel, controller);

        final JFrame mainFrame = new JFrame("Analyze View");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.add(view);
        mainFrame.pack();
        mainFrame.setVisible(true);

        // Only displays the most recent, good
        viewModel.setMessage("Demo Message #1");
        viewModel.setMessage("Demo Message #2");
        viewModel.setMessage("Demo Message #3");

    }
}
