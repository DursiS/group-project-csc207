package MakeMove;


public class SaveGamePresenter implements SaveGameOutputBoundary {

    private SaveGameViewModel viewModel;

    public SaveGamePresenter(SaveGameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessSaveView(SaveGameOutputData outputData){
        viewModel.setMessage(outputData.getSaveName() + " Successfully Saved!");
        viewModel.setSavedName(outputData.getSaveName());
        viewModel.setError("");
        viewModel.setOverwriteMessage("");
    }

    @Override
    public void prepareFailSaveView(String errorMessage) {
        viewModel.setMessage("");
        viewModel.setError(errorMessage);
        viewModel.setOverwriteMessage("");

    }

    public void prepareOverwriteView(String overwriteMessage){
        viewModel.setMessage("");
        viewModel.setOverwriteMessage(overwriteMessage);
        viewModel.setError("");
    }

}
