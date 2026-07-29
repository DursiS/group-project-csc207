package interface_adapter;

import use_case.AnalyzeOutputBoundary;

public class AnalyzePresenter implements AnalyzeOutputBoundary {
    private final AnalyzeViewModel viewModel;

    public AnalyzePresenter() {
        this(new AnalyzeViewModel());
    }

    public AnalyzePresenter(AnalyzeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void addMessage(String message) {
        this.viewModel.addMessage(message);
    }
}
