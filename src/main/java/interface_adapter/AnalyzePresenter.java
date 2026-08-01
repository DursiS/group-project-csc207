package interface_adapter;

import use_case.AnalyzeOutputBoundary;
import use_case.AnalyzeOutputData;

public class AnalyzePresenter implements AnalyzeOutputBoundary {
    private final AnalyzeViewModel viewModel;

    public AnalyzePresenter(AnalyzeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Formats the analysis output and pushes it to the view model.
     * @param outputData the raw analysis results
     */
    @Override
    public void addMessage(AnalyzeOutputData outputData) {
        final String resultTail = outputData.boardEval() + "\n" + outputData.bestMove();
        final String header;
        if (outputData.isWhiteTurn()) {
            header = "== WHITE'S TURN == \n\nWhite's Metrics: \n";
        }
        else {
            header = "== BLACK'S TURN == \n\nBlack's Metrics: \n";
        }
        this.viewModel.setMessage(header + resultTail + "\n\n");
    }

    /**
     * Tells the view model to show only the most recent message.
     */
    @Override
    public void setRecentMessage() {
        this.viewModel.setRecentMessage();
    }

    /**
     * Tells the view model to show the whole message history.
     */
    @Override
    public void setHistoryMessage() {
        this.viewModel.setHistoryMessage();
    }
}
