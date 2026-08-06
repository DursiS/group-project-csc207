package entity;
import java.util.ArrayList;
import java.util.List;

public class BoardStateList {
    private List<Board> boardstates;

    //create a empty boardstatelist
    public BoardStateList(){
        this.boardstates = new ArrayList<>();
    }

    //create a boardstatelist by copying every board in a list
    public BoardStateList(List<Board> otherboards){
        this();
        if (otherboards != null){
            for(int i = 0; i < otherboards.size(); i++){
                boardstates.add(otherboards.get(i).Copy());
            }
        }
    }

    //add a copy of the voard into boardstate
    public void addBoardCopy(Board board){
        Board boardCopy = board.Copy();
        this.boardstates.add(boardCopy);
    }
    //return the size of the boardstatelist
    public int size(){return this.boardstates.size();}

    //return a copy of the board at index i
    public Board getBoardCopy(int i){
        Board board = boardstates.get(i);
        return board.Copy();
    }

    //return a deep copy of boardstatelist
    public BoardStateList Copy(){
        BoardStateList boardStateListCopy = new BoardStateList();
        for(int i = 0; i < this.size(); i++){
            boardStateListCopy.addBoardCopy(this.boardstates.get(i));
        }
        return boardStateListCopy;
    }
}
