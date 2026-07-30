package entity;

public class GameState {
    //current chess board
    private Board board;

    //remaining time for both players in millisecond
    private int whiteMilliSec;
    private int blackMilliSec;

    //previous move
    private Move move;

    //stores previous board states
    private BoardStateList boardStateList;

    //current result for the game, use null for the initial game state
    private String gameResult;

    //create a game state with infos to save the game
    public GameState(Board board, Move move, int whiteMilliSec, int blackMilliSec,
                     BoardStateList boardStateList,
                     String gameResult){
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
        this.move = move;
        this.whiteMilliSec = whiteMilliSec;
        this.blackMilliSec = blackMilliSec;
        this.boardStateList = boardStateList.Copy();
        this.gameResult = gameResult;
    }
    public Board getBoardCopy(){return this.board.Copy();}
    public BoardStateList getBoardStateListCopy(){return this.boardStateList.Copy();}
    public int getWhiteMilliSec(){return this.whiteMilliSec;}
    public int getBlackMilliSec(){return this.blackMilliSec;}
    public String getGameResult(){return this.gameResult;}

    public Move getMove(){
        return move;
    }
}
