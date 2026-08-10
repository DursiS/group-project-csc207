package SaveResume;

public class ResumeGameViewModel {
    private String Message;
    private String errorMessage;

    public ResumeGameViewModel() {
        this.Message = "";
        this.errorMessage = "";
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
