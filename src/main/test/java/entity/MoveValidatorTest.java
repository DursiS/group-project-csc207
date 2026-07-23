package entity;

import entity.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MoveValidatorTest {
    @Test
    void basicMoveValidatorTest(){
        Board b = new Board();//default board;
        MoveValidator v = new MoveValidator();

        ArrayList<Move> moves = v.getAllMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        assertEquals(moves.size(), 20);
    }

    @Test
    void capturingMoveValidatorTest(){
        int[][] squares = new int[8][8];
        squares[0][0] = 4;
        squares[0][3] = -4;
        Board b = new Board(squares,  0,0,0);//default board;
        MoveValidator v = new MoveValidator();

        ArrayList<Move> moves = v.getAllMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        assertEquals(moves.size(), 10);
    }

    @Test
    void allyObstructionMoveValidatorTest(){
        int[][] squares = new int[8][8];
        squares[0][4] = 4;
        squares[0][3] = 4;
        Board b = new Board(squares, 0,0,0);//default board;
        MoveValidator v = new MoveValidator();

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
