package entity;

public class GameState {

    private final Board currentBoard;
//    private Move previousMove;

    // time remaining in seconds for player 1 and 2
    private final int timeRemaining1;
    private final int timeRemaining2;
    private final int DEFAULT_TIME = 600;

    public GameState() {
        currentBoard = new Board();
        timeRemaining1 = DEFAULT_TIME;
        timeRemaining2 = DEFAULT_TIME;
    }

//    public GameState(Board board, Move move, int time1, int time2) {
//        currentBoard = board;
//        previousMove = move;
//        timeRemaining1 = time1;
//        timeRemaining2 = time2;
//    }
}
