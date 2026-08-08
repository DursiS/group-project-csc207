package interface_adapter;

import use_case.ResumeGameOutputBoundary;
import use_case.ResumeGameOutputData;

public class ResumeGamePresenter implements ResumeGameOutputBoundary {

    private ResumeGameViewModel viewModel;

    public ResumeGamePresenter(ResumeGameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessResumeView(ResumeGameOutputData outputData){
        viewModel.setMessage(outputData.getsaveName() + "Successfully Resumed!");
    }

    @Override
    public void prepareFailResumeView(String errorMessage) {
        viewModel.setErrorMessage("Error: " + errorMessage);
    }
}
