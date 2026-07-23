//this class is a data type, which contains all information about a board state (all pieces and the current turn)
//it is used by the main game logic, and should be saveable and loadable to the computer memory,
//and the move validator makes deep copies of the current game board to apply imaginary moves the next turn to see if moves are valid

package entity;
public class Board {
    private int turn;
    //turn is even: white's turn to move, turn is odd: black's turn to move
    private int[][] squares;
    //squares[y,x] = the square at row y and column x.
    //row 0 is at black's side (top) and row 7 is at white's side (bottom)
    //column 0 is at the left and column 7 is at the right
    // condition: squares is an 8*8 array, entries are in the range -10 to 10.
    //0: empty
    //1: white pawn 2: white pawn (moved) 3: white pawn (en passant-able) 4: white rook 5: white rook (moved) 6: white knight 7: white bishop 8: white queen 9: white king 10: white king (moved)
    //-1: black pawn -2: black pawn (moved) -3: black pawn (en passant-able) -4: black rook -5: black rook (moved) -6: black knight -7: black bishop -8: black queen -9: black king -10: black king (moved)

    public int getTurn() {
        return turn;
    }
    public void incrementTurn() {
        turn += 1;
    }
    public void setTurn(int turn) {
        this.turn = turn;
    }




    public int getSquare(int x, int y) {
        return squares[y][x];
    }
    public void setSquare(int x, int y, int value) throws IllegalArgumentException {
        if( value < -10 || value > 10){
            throw new IllegalArgumentException();
        }
        squares[y][x] = value;
    }

    //these methods are used by the move validator
    public boolean isSquareEmpty(int x, int y){
        return squares[y][x] == 0;
    }
    public boolean canPieceMove(int x, int y){//return whether the piece on tile (x,y) can move on current turn
        return !isSquareEmptyOrEnemy(x,y);
    }
    public boolean isSquareEnemy(int x, int y){//return whether the piece on tile (x,y) is an enemy piece of the player whose turn it currently is
        if(turn %2 ==0){
            return(squares[y][x] < 0);
        }
        return(squares[y][x] > 0);
    }
    public boolean isSquareEmptyOrEnemy(int x, int y) {//this method will be called many times...
        if(turn %2 ==0){
            return(squares[y][x] <= 0);
        }
        return(squares[y][x] >= 0);
    }

    //constructor
    public Board(int[][] squares, int turn){
        this.squares = squares;
        this.turn = turn;
    }
    //constructor with no arguments gives initial chessboard
    public Board(){
        this.squares = new int[][]{
                {-4,-6,-7,-8,-9,-7,-6,-4},
                {-1,-1,-1,-1,-1,-1,-1,-1},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1},
                {4,6,7,8,9,7,6,4}
        };
        turn = 0;
    }

    //creates deep copy of current board (will be used by the valid move checker)
    public Board Copy(){
        int[][] newSquares = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newSquares[i][j] = squares[i][j];
            }
        }
        return new Board(newSquares, turn);
    }
}
