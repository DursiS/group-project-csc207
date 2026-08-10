package MakeMove;

public interface SaveGameOutputBoundary {

    void prepareSuccessSaveView(SaveGameOutputData outputData);

    void prepareFailSaveView(String errorMessage);

    void prepareOverwriteView(String overwriteMessage);
}
