package SaveResume;

public interface ResumeGameOutputBoundary {

    void prepareSuccessResumeView(ResumeGameOutputData outputData);

    void prepareFailResumeView(String errorMessage);

}
