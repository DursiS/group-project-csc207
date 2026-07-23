//this class has the responsibility of generating a list of all valid moves for each piece each turn
//it doesn't modify the game board

package entity;

import java.util.ArrayList;

public class MoveValidator {
    //default constructor creates all movement rules corresponding to each piece type
    //i suppose this could instead be stored in a special data storage object....
    MoveType[][] moveTypes = new MoveType[10][];

    public MoveValidator(){
        moveTypes[0] = new MoveType[]{}; //empty tile has no moves
        moveTypes[1] = new MoveType[]{//unmoved pawn
                new MoveType(new int[]{0,1}, 1, false, true, false, false),
                new MoveType(new int[]{1,1}, 0, true, false, false, false),
                new MoveType(new int[]{-1,1}, 0, true, false, false, false),
                new MoveType(new int[]{1,1}, 0, false, true, true, false),
                new MoveType(new int[]{-1,1}, 0, false, true, true, false)
        };
        moveTypes[2] = moveTypes[1];//moved pawn (
        moveTypes[2][0] = new MoveType(new int[]{0,1}, 0, false, true, false, false)
        moveTypes[3] = moveTypes[2];//moved pawn (enpassantable)

        moveTypes[4] = new MoveType[]{//unmoved rook
                new MoveType(new int[]{0, 1}, 6),
                new MoveType(new int[]{0, -1}, 6),
                new MoveType(new int[]{1, 0}, 6),
                new MoveType(new int[]{-1, 0}, 6),
        };
        moveTypes[5] = moveTypes[4];//moved rook (castle is only considered a king move, so it's not added here)
        moveTypes[6] = new MoveType[]{//🐴
                new MoveType(new int[]{1, 2}, 0),
                new MoveType(new int[]{1, -2}, 0),
                new MoveType(new int[]{-1, 2}, 0),
                new MoveType(new int[]{-1, -2}, 0),
                new MoveType(new int[]{2, 1}, 0),
                new MoveType(new int[]{2, -1}, 0),
                new MoveType(new int[]{-2, 1}, 0),
                new MoveType(new int[]{-2, -1}, 0),
        };
        moveTypes[7] = new MoveType[]{//bishop
                new MoveType(new int[]{1, 1}, 30),
                new MoveType(new int[]{-1, -1}, 30),
                new MoveType(new int[]{1, -1}, 30),
                new MoveType(new int[]{-1, 1}, 30),
        };
        moveTypes[8] = new MoveType[]{//queen
                new MoveType(new int[]{0, 1}, 6),
                new MoveType(new int[]{0, -1}, 6),
                new MoveType(new int[]{1, 0}, 6),
                new MoveType(new int[]{-1, 0}, 6),
                new MoveType(new int[]{1, 1}, 30),
                new MoveType(new int[]{-1, -1}, 30),
                new MoveType(new int[]{1, -1}, 30),
                new MoveType(new int[]{-1, 1}, 30),
        };
        moveTypes[10] = new MoveType[]{//moved king (can't castle)
                new MoveType(new int[]{0, 1}, 0),
                new MoveType(new int[]{0, -1}, 0),
                new MoveType(new int[]{1, 0}, 0),
                new MoveType(new int[]{-1, 0}, 0),
                new MoveType(new int[]{1, 1}, 0),
                new MoveType(new int[]{-1, -1}, 0),
                new MoveType(new int[]{1, -1}, 0),
                new MoveType(new int[]{-1, 1}, 0),
        };
        moveTypes[9] = new MoveType[10];//unmoved king (can castle)
        for (int i = 0; i < 8; i++) {
            moveTypes[9][i] = moveTypes[10][i];
        }
        moveTypes[9][8] = new MoveType(new int[]{3, 0}, 0, false, false, false, true);
        moveTypes[9][9] = new MoveType(new int[]{-4, 0}, 0, false, false, false, true);
    }

    private MoveType[] getMoveTypes(int pieceType){
        //maybe check for valid pieceType...

        //white pieces move in the opposite direction
        if (pieceType < 0){
            return this.moveTypes[pieceType];
        }
        else{
            MoveType[] mirroredMoveTypes = new MoveType[moveTypes[-pieceType].length];
            for (int i = 0; i < mirroredMoveTypes.length; i++) {
                mirroredMoveTypes[i] = MoveType.createMirroredMove(moveTypes[-pieceType][i]);
            }
            return mirroredMoveTypes;
        }
    }

    //it generates all moves for every piece belonging to the player who's currently is (in board).
    public ArrayList<Move> getMoves(Board board){
        ArrayList<Move> moves = new ArrayList<Move>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

            }
        }
    }
}
