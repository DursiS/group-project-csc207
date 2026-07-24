package interface_adapter;

import use_case.AnalyzeOutputBoundary;

public class AnalyzePresenter implements AnalyzeOutputBoundary {
    private final AnalyzeViewModel viewModel = new AnalyzeViewModel();

    @Override
    public void addMessage(String message) {
        this.viewModel.addMessage(message);
    }
}
