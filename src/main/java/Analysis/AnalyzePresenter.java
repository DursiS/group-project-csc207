package Analysis;

public class AnalyzePresenter implements AnalyzeOutputBoundary {
    private static final double NUM_TO_ROUND = 100.0;

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
        this.viewModel.setMessage(makeMessage(outputData));
    }

    /**
     * Formats a display message from the raw analysis data.
     * @param outputData the raw data from the interactor
     * @return the formatted analysis message
     */
    public String makeMessage(AnalyzeOutputData outputData) {
        final String header;
        final String side;
        final double winChance;
        final double eval;
        final String turn = " (" + outputData.messageNum() + ") ";
        final String doubleSpace = "\n\n";

        if (outputData.isWhiteTurn()) {
            header = "== WHITE'S TURN " + turn + "==" + doubleSpace;
            side = "White";
            winChance = outputData.winChance();
            eval = outputData.eval();
        }
        else {
            header = "== BLACK'S TURN " + turn + "==" + doubleSpace;
            side = "Black";
            winChance = (-1) * (1 - outputData.winChance());
            eval = (-1) * outputData.eval();
        }
        final String body = side + " WinChance: " + roundTwo(winChance) + "% \n"
                + side + " Eval: " + roundTwo(eval) + "\n"
                + "Best Move: " + outputData.from() + " -> " + outputData.to();
        return header + body + doubleSpace;
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

    /**
     * Rounds a value to two decimal places.
     * @param value the value to round
     * @return the value rounded to two decimals
     */
    private static double roundTwo(double value) {
        return Math.round(value * NUM_TO_ROUND) / NUM_TO_ROUND;
    }
}
