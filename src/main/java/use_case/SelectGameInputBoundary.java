package use_case;

public interface SelectGameInputBoundary {

    /**
     * Select the past completed game given the id
     * @param selectGameInputData the input data
     */
    void selectGame(SelectGameInputData selectGameInputData);
}
