package interface_adapter;

public class SaveGameViewModel {
    private String Message;
    private String error;
    private String overwriteMessage;
    private String savedName;

    public SaveGameViewModel() {
        this.Message = "";
        this.error = "";
        this.overwriteMessage = "";
        this.savedName = "";
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

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String anotherName) {
        this.savedName = anotherName;
    }
}
