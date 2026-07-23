package entity;

import entity.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MoveValidatorTest {
    @Test
    void basicMoveValidatorTest(){
        Board b = new Board();//default board;
        MoveValidator v = new MoveValidator(new BoardTopology(0,0));

        ArrayList<Move> moves = v.getAllMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        assertEquals(moves.size(), 20);
    }

    static String vectorToString(int[] vector){
        return  "("+ Integer.toString(vector[0] ) + "," + Integer.toString(vector[1]) + ")";
    }

}
