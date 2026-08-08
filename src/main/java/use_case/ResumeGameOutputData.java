package use_case;
import entity.GameState;
public class ResumeGameOutputData {
    private String saveName;
    public ResumeGameOutputData(String saveName) {
        this.saveName = saveName;
    }

    public String getsaveName() {
        return saveName;
    }
}
