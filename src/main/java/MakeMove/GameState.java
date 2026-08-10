package MakeMove;

public class GameState {
    //current chess board
    private Board board;

    //increment
    private int increment;

    //remaining time for both players in millisecond
    private int whiteMilliSec;
    private int blackMilliSec;

    //stores previous board states
    private BoardStateList boardStateList;

    //current result for the game
    private String gameResult;

    //create a game state with infos to save the game
    public GameState(Board board, int whiteMilliSec, int blackMilliSec,
                     BoardStateList boardStateList,
                     String gameResult, int increment){
        if (board == null){
            throw new IllegalArgumentException("Board can't be null");
        }
        if (whiteMilliSec < 0 || blackMilliSec< 0){
            throw new IllegalArgumentException("remaining time can't be negative");
        }
        if (gameResult == null){
            throw new IllegalArgumentException("Game result can not be empty");
        }
        this.board = board.Copy();
        this.whiteMilliSec = whiteMilliSec;
        this.blackMilliSec = blackMilliSec;
        this.boardStateList = boardStateList.Copy();
        this.gameResult = gameResult;
        this.increment = increment;
    }
    public Board getBoardCopy(){return this.board.Copy();}
    public BoardStateList getBoardStateListCopy(){return this.boardStateList.Copy();}
    public int getIncrement(){return this.increment;}
    public int getWhiteMilliSec(){return this.whiteMilliSec;}
    public int getBlackMilliSec(){return this.blackMilliSec;}
    public void setWhiteMilliSec(int whiteMilliSec){this.whiteMilliSec = whiteMilliSec;}
    public void setBlackMilliSec(int blackMilliSec){this.blackMilliSec = blackMilliSec;}
    public String getGameResult(){return this.gameResult;}

    public Board getBoard(){
        return board;
    }
    public BoardStateList getBoardStateList(){
        return boardStateList;
    }
}
