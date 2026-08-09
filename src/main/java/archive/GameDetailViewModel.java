package archive;

import MakeMove.Board;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameDetailViewModel {

    public static final String VIEW_NAME = "Game Detail";

    private GameRecord gameRecord;
    private Board board;
    private int currentStateNumber = 0;
    private int whiteMilliSec;
    private int blackMilliSec;
    private boolean hasPrevious = false;
    private boolean hasNext = true;
    private String gameResult = null;
    private String errorMessage = null;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GameDetailViewModel() {}

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void firePropertyChanged() {
        support.firePropertyChange(null, null, this);
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public GameRecord getGameRecord() {
        return gameRecord;
    }

    public void setGameRecord(GameRecord gameRecord) {
        this.gameRecord = gameRecord;
    }

    public int getCurrentStateNumber() {
        return currentStateNumber;
    }

    public void setCurrentStateNumber(int currentStateNumber) {
        this.currentStateNumber = currentStateNumber;
    }

    public int getWhiteMilliSec() {
        return whiteMilliSec;
    }

    public void setWhiteMilliSec(int whiteMilliSec) {
        this.whiteMilliSec = whiteMilliSec;
    }

    public int getBlackMilliSec() {
        return blackMilliSec;
    }

    public void setBlackMilliSec(int blackMilliSec) {
        this.blackMilliSec = blackMilliSec;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getGameResult() {
        return gameResult;
    }

    public void setGameResult(String gameResult) {
        this.gameResult = gameResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
