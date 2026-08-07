package interface_adapter;

import use_case.SaveGameOutputBoundary;
import use_case.SaveGameOutputData;

public class SaveGamePresenter implements SaveGameOutputBoundary {

    private SaveGameViewModel viewModel;

    public SaveGamePresenter(SaveGameViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void prepareSuccessSaveView(SaveGameOutputData outputData){
        viewModel.setMessage(outputData.getSaveName() + " Successfully Saved!");
        viewModel.setError("");
    }

    public void prepareFailSaveView(String errorMessage) {
        viewModel.setMessage("");
        viewModel.setError(errorMessage);

    }
}
