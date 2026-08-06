package entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MoveValidatorTest {
    @Test
    void basicMoveValidatorTest(){
        //start with default board
        Board b = new Board();
        MoveValidator v = new MoveValidatorBuilder().doDefaultSetup().build();

        ArrayList<Move> moves = v.getAllValidMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        //there should be 8*2 pawn moves + 2*2 knight moves
        assertEquals(moves.size(), 20);
    }

    @Test
    void capturingMoveValidatorTest(){
        //create board with enemy that should be able to be captured and see if it can be captured
        int[][] squares = new int[8][8];
        squares[0][0] = 4;
        squares[0][3] = -4;
        Board b = new Board(squares,  0,0,0);
        MoveValidator v = new MoveValidatorBuilder().doDefaultSetup().build();


        ArrayList<Move> moves = v.getAllValidMoves(b);
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
        MoveValidator v = new MoveValidatorBuilder().doDefaultSetup().build();

        ArrayList<Move> moves = v.getAllValidMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        assertEquals(moves.size(), 20);
    }

    @Test
    void kingVulnerabilityDisallowanceMoveValidatorTest(){
        //create board where white is in check and it's white's turn to move
        int[][] squares = new int[8][8];
        squares[0][0] = 9;
        squares[7][0] = -4;
        squares[7][7] = 4;
        squares[4][7] = 4;

        Board b = new Board(squares, 0,0,0);//default board;
        MoveValidator v = new MoveValidatorBuilder().doDefaultSetup().build();

        ArrayList<Move> moves = v.getAllValidMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        //there should be 4 allowed move:
        //1,2: move king out of the way of the enemy rook
        //3: obstruct king with rook
        //4: capture enemy rook
        assertEquals(moves.size(), 4);
    }

    @Test
    void cylinderMoveValidatorTest(){
        //create cylinder board
        int [][] squares = new int[8][8];
        squares[0][0] = 9;


        Board b = new Board(squares, 0,1,0);
        MoveValidator v = new MoveValidatorBuilder().doDefaultSetup().build();

        ArrayList<Move> moves = v.getAllValidMoves(b);
        System.out.println(moves.size());
        for (int i = 0; i < moves.size(); i++) {
            System.out.println( vectorToString(moves.get(i).getOrigin()) + " -> " + vectorToString(moves.get(i).getDestination()));
        }
        assertEquals(moves.size(), 5);
    }



    static String vectorToString(int[] vector){
        return  "("+ Integer.toString(vector[0] ) + "," + Integer.toString(vector[1]) + ")";
    }

}
