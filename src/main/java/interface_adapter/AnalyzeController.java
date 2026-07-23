package interface_adapter;

public class AnalyzeController {
    AnalyzeInputBoundary inputBoundary;

    AnalyzeController(AnalyzeInputBoundary inputBoundary){
        this.inputBoundary = inputBoundary;
    }

    public void executeTurnAnalysis() {
        inputBoundary.executeTurnAnalysis();
    }
}
