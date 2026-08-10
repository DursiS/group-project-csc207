package MakeMove;

import java.util.List;

public class GameState {
    // current chess board
    private Board board;

    // remaining time for both players in millisecond
    private int whiteMilliSec;
    private int blackMilliSec;

    // stores previous board states
    private BoardStateList boardStateList;

    // current result for the game
    private String gameResult;

    private List<String> analysisHistory;

    // create a game state with infos to save the game
    public GameState(Board board,
                     int whiteMilliSec,
                     int blackMilliSec,
                     BoardStateList boardStateList,
                     String gameResult,
                     List<String> analysisHistory,
                     ) {

        if (board == null) {
            throw new IllegalArgumentException("Board can't be null");
        }

        if (whiteMilliSec < 0 || blackMilliSec < 0) {
            throw new IllegalArgumentException(
                    "remaining time can't be negative"
            );
        }

        if (boardStateList == null) {
            throw new IllegalArgumentException(
                    "Board state list can't be null"
            );
        }

        if (gameResult == null) {
            throw new IllegalArgumentException(
                    "Game result can not be empty"
            );
        }

        this.board = board.Copy();
        this.whiteMilliSec = whiteMilliSec;
        this.blackMilliSec = blackMilliSec;
        this.boardStateList = boardStateList.Copy();
        this.gameResult = gameResult;
        this.analysisHistory = analysisHistory;
    }

    public Board getBoardCopy() {
        return this.board.Copy();
    }

    public BoardStateList getBoardStateListCopy() {
        return this.boardStateList.Copy();
    }

    public List<String> getAnalysisHistory() {
        return analysisHistory;
    }

    public int getWhiteMilliSec() {
        return this.whiteMilliSec;
    }

    public int getBlackMilliSec() {
        return this.blackMilliSec;
    }

    public String getGameResult() {
        return this.gameResult;
    }

    public Board getBoard() {
        return board;
    }

    public BoardStateList getBoardStateList() {
        return boardStateList;
    }
}