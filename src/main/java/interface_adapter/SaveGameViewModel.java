package interface_adapter;

public class SaveGameViewModel {
    private String Message;
    private String error;
    private String overwriteMessage;

    public SaveGameViewModel() {
        this.Message = "";
        this.error = "";
        this.overwriteMessage = "";
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

    public String getOverwriteMessage() {return overwriteMessage;}

    public void setOverwriteMessage(String anotherMessage) {
        this.overwriteMessage = anotherMessage;
    }
}
