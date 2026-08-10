package Timer;


public class ClockViewModel {
    private String time;

    public ClockViewModel(String time){
        this.time = time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }
}
