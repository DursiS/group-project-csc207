package use_case;

public interface SaveGameOutputBoundary {

    void prepareSuccessSaveView(SaveGameOutputData outputData);

    void prepareFailSaveView(String errorMessage);
}
