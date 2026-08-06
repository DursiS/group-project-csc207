//the purpose of this class is to create a move validator
//Since this allows the move validator to use the dependency injection design pattern,
//which allows creating move validators that follow different rules of chess,
//This in turn increases the amount of SRP and OCP, rather than having a fixed constructor for the Move validator.



package entity;

import java.util.ArrayList;
import java.util.HashMap;

public class MoveValidatorBuilder {
    private HashMap<Integer, ArrayList<MoveType>> moveTypesLists;

    public void addMove(int pieceType, MoveType moveType){
        if(moveTypesLists.containsKey(pieceType)){
            moveTypesLists.get(pieceType).add(moveType);
        }else{
            moveTypesLists.put(pieceType, new ArrayList<MoveType>());
            moveTypesLists.get(pieceType).add(moveType);
        }
    }

    public void addMove(int[] pieceTypes, MoveType moveType){
        for (int i = 0; i < pieceTypes.length; i++) {
            addMove(pieceTypes[i], moveType);
        }
    }

    public MoveValidatorBuilder() {
        moveTypesLists = new HashMap<Integer, ArrayList<MoveType>>();
    }

    public void addNormalMoves(){
        //Pawn moves
        addMove(new int[]{-1}, new NormalMoveType(new int[]{0,1}, 1, false, true));
        addMove(new int[]{-2,-3}, new NormalMoveType(new int[]{0,1}, 0, false, true));
        addMove(new int[]{-1,-2,-3}, new NormalMoveType(new int[]{1,1}, 0, true, false));
        addMove(new int[]{-1,-2,-3}, new NormalMoveType(new int[]{-1,1}, 0, true, false));
        //rook and queen moves
        addMove(new int[]{-4,-5,-8}, new NormalMoveType(new int[]{0,1},62));
        addMove(new int[]{-4,-5,-8}, new NormalMoveType(new int[]{0,-1},62));
        addMove(new int[]{-4,-5,-8}, new NormalMoveType(new int[]{1,0},62));
        addMove(new int[]{-4,-5,-8}, new NormalMoveType(new int[]{-1,0},62));
        //bishop and queen moves
        addMove(new int[]{-7,-8}, new NormalMoveType(new int[]{1,1},62));
        addMove(new int[]{-7,-8}, new NormalMoveType(new int[]{1,-1},62));
        addMove(new int[]{-7,-8}, new NormalMoveType(new int[]{1,-1},62));
        addMove(new int[]{-7,-8}, new NormalMoveType(new int[]{-1,1},62));
        //horse
        addMove(-6, new NormalMoveType(new int[]{1, 2}, 0));
        addMove(-6, new NormalMoveType(new int[]{1, -2}, 0));
        addMove(-6, new NormalMoveType(new int[]{-1, 2}, 0));
        addMove(-6, new NormalMoveType(new int[]{-1, -2}, 0));
        addMove(-6, new NormalMoveType(new int[]{2, 1}, 0));
        addMove(-6, new NormalMoveType(new int[]{2, -1}, 0));
        addMove(-6, new NormalMoveType(new int[]{-2, -1}, 0));
        addMove(-6, new NormalMoveType(new int[]{-2, 1}, 0));
        //king
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{0,1},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{0,-1},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{1,0},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{-1,0},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{1,1},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{1,-1},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{-1,1},0));
        addMove(new int[]{-9,-10}, new NormalMoveType(new int[]{-1,-1},0));
    }


    public void addEnPassants(){
        addMove(new int[]{-1,-2,-3}, new EnPassantMoveType(new int[]{1,1}, new int[]{1,0}));
        addMove(new int[]{-1,-2,-3}, new EnPassantMoveType(new int[]{-1,1}, new int[]{-1,0}));
    }

    public void addCastles(){

    }

    public void duplicateAndMirrorMoves(){
        ArrayList<Integer> copySet = new ArrayList<>(moveTypesLists.keySet());
        //create a copy of the key set because it will cause an error if you modify it while iterating over it
        for (int key: copySet){
            ArrayList<MoveType> m = moveTypesLists.get(key);
            ArrayList<MoveType> m2 = new ArrayList<MoveType>();
            for (int i = 0; i < m.size(); i++) {
                m2.add(m.get(i).createMirroredMove());
            }
            moveTypesLists.put(-key, m2);
        }
    }

    public MoveValidatorBuilder doDefaultSetup(){
        addNormalMoves();
        addEnPassants();
        addCastles();
        duplicateAndMirrorMoves();
        return this;
    }

    public MoveValidator build(){
        //convert map of lists into map of arrays.
        HashMap<Integer, MoveType[]> moveTypes = new HashMap<Integer, MoveType[]>();
        for (int key: moveTypesLists.keySet()){
            moveTypes.put(key, moveTypesLists.get(key).toArray(new MoveType[0]));
        }
        return new MoveValidator(moveTypes);
    }
}
