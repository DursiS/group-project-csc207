package interface_adapter;

public class SaveGameViewModel {
    private String Message;
    private String error;

    public SaveGameViewModel() {
        this.Message = "";
        this.error = "";
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String anotherMessage) {
        this.Message = anotherMessage;
    }

    public String getError() {
        return error;
    }

    public void setError(String anotherError) {
        this.error = anotherError;
    }
}
