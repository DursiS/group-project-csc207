
package MakeMove;

/**
 * //this class is a data type, which contains all information about a board state (all pieces and the current turn)
 * //it is used by the main game logic, and should be saveable and loadable to the computer memory,
 * //and the move validator makes deep copies of the current game board to apply imaginary moves the next turn to see if moves are valid
 *
 * //it also contains the topology information of the board
 */
public class Board {
    //turn is even: white's turn to move, turn is odd: black's turn to move
    private int turn;

    //squares[y,x] = the square at row y and column x.
    //row 0 is at black's side (top) and row 7 is at white's side (bottom)
    //column 0 is at the left and column 7 is at the right
    // condition: squares is an 8*8 array, entries are in the range -10 to 10.
    //0: empty
    //1: white pawn 2: white pawn (moved) 3: white pawn (en passant-able) 4: white rook 5: white rook (moved) 6: white knight 7: white bishop 8: white queen 9: white king 10: white king (moved)
    //-1: black pawn -2: black pawn (moved) -3: black pawn (en passant-able) -4: black rook -5: black rook (moved) -6: black knight -7: black bishop -8: black queen -9: black king -10: black king (moved)
    //The board is stored in this manner rather than using classes, because the pieces used in chess
    //are fixed (no reason to change/modify) and we want to optimize the process of copying
    //the board in order to easily save/load the game to database, or
    //create copies of boards for the MoveValidator to simulate imaginary moves on
    private int[][] squares;

    //topology information for each edge:
    //0: impassible wall (chess default), 1: passible and identified with the opposite side, 2: passible and identified with the opposite side with reversed orientation (this one is less interesting so we don't have to implement it).
    private int verticalEdgeType;
    private int horizontalEdgeType;

    public int getVerticalEdgeType() {
        return verticalEdgeType;
    }

    public int getHorizontalEdgeType() {
        return horizontalEdgeType;
    }

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
    public int getSquare(int[] position){
        return getSquare(position[0], position[1]);
    }

    /**
     * Sets the square at (x,y) on the board
     *
     * @param x the x position
     * @param y the y position
     * @param value the value to assign the square at (x,y)
     * @throws IllegalArgumentException
     */
    public void setSquare(int x, int y, int value) throws IllegalArgumentException {
        if( value < -10 || value > 10){
            throw new IllegalArgumentException();
        }
        squares[y][x] = value;
    }

    /**
     * Sets the square at (x,y) on the board
     * @param position the [x,y] position
     * @param value the value to assign
     * @throws IllegalArgumentException
     */
    public void setSquare(int[] position, int value) throws IllegalArgumentException{
        setSquare(position[0], position[1], value);
    }

    //these methods are used by the move validator

    /**
     * get if the square at (x,y) is empty
     * @param x the x position
     * @param y the y position
     * @return true iff the square at (x,y) is empty
     */
    public boolean isSquareEmpty(int x, int y){
        return squares[y][x] == 0;
    }

    /**
     * get if the square at (x,y) is empty with int[] input
     * @param vec the [x,y] coordinates
     * @return true iff the square is empty
     */
    public boolean isSquareEmpty(int[] vec){
        return isSquareEmpty(vec[0], vec[1]);
    }

    /**
     * return whether the piece on tile (x,y) can move on current turn (it's colour is that of the player whose turn it is currenlty)
     * @param x x coord
     * @param y y coord
     * @return true iff the piece can move at this turn
     */
    public boolean isPiecesTurn(int x, int y){
        return !isSquareEmptyOrEnemy(x,y);
    }

    /**
     * alt version for int[] input
     * @param vec vector
     * @return true iff the piece can move at this turn
     */
    public boolean isPiecesTurn(int[] vec){//return whether the piece on tile (x,y) can move on current turn
        return isPiecesTurn(vec[0], vec[1]);
    }

    /**
     * return whether the piece on tile (x,y) is an enemy piece of the player whose turn it currently is
     * @param x coord
     * @param y coord
     * @return true iff enemy of current player
     */
    public boolean isSquareEnemy(int x, int y){
        if(turn %2 ==0){
            return(squares[y][x] < 0);
        }
        return(squares[y][x] > 0);
    }

    /**
     * alt version for int[] input
     * @param vec vector
     * @return true iff enemy of current player
     */
    public boolean isSquareEnemy(int[] vec){
        return isSquareEnemy(vec[0], vec[1]);
    }

    /**
     * check if the square is occupied by enemy or empty
     * @param x coord
     * @param y coord
     * @return true iff the square is empty or enemy
     */
    public boolean isSquareEmptyOrEnemy(int x, int y) {
        if(turn %2 ==0){
            return(squares[y][x] <= 0);
        }
        return(squares[y][x] >= 0);
    }

    /**
     * detailed constructor for Board
     * @param squares an 8*8 int[] with values in the range -10 to 10, denoting the board
     * @param turn current turn
     * @param verticalEdgeType edge type on the left and right sides of the board
     * @param horizontalEdgeType edge type on the top and bottom sides of the board
     */
    public Board(int[][] squares, int turn, int verticalEdgeType, int horizontalEdgeType){
        this.squares = squares;
        this.turn = turn;
        this.verticalEdgeType = verticalEdgeType;
        this.horizontalEdgeType = horizontalEdgeType;
    }

    /**
     * constructor for default board (default pieces locations)
     * @param verticalEdgeType the topology type of the vertical edges
     * @param horizontalEdgeType the topology type of the horizontal edges
     */
    public Board(int verticalEdgeType, int horizontalEdgeType){
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
        this.verticalEdgeType = verticalEdgeType;
        this.horizontalEdgeType = horizontalEdgeType;
    }

    /**
     * constructor for default topology with default board layout
     */
    public Board(){
        this(0, 0);
    }

    /**
     * Creates a deep copy of the board. So, moves can be applied without affecting the actual
     * ongoing game. This is important for the move validator, when it has to simulate imaginary
     * moves to determine if the player is in check.
     * @return the copy.
     */
    public Board Copy(){
        int[][] newSquares = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newSquares[i][j] = squares[i][j];
            }
        }
        return new Board(newSquares, turn, verticalEdgeType, horizontalEdgeType);
    }

    /**
     * method for debugging
     * @return string representation of the board
     */
    public String toString(){
        String s = "turn: " + turn;
        for (int y = 0; y < 8; y++) {
            s+="\n";
            for (int x = 0; x < 8; x++) {
                s += getSquare(x,y);
                if(x<7){
                    s += ",";
                }
            }
        }
        return s;
    }

}
