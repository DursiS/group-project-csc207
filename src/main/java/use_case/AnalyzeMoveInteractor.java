package use_case;

import com.google.gson.JsonObject;
import entity.Board;

public class AnalyzeMoveInteractor{
    private final ChessApiInterface ApiInterface;

    public AnalyzeMoveInteractor(ChessApiInterface ApiInterface) {
        this.ApiInterface = ApiInterface;
    }

    /**
     * Suggest the next best Move
     * @param board, a Board object of the current board state
     * @return a Move object of what should be done next
     */
    public String bestMove(Board board) throws Exception {
        String fen = convertToFen(board);
        JsonObject response = this.ApiInterface.request(fen);
        return response.get("from").getAsString() + " -> " + response.get("to").getAsString();
    }

    /**
     * Get the current Board evaluation for whoever's turn it is
     * @param board, a Board; snapshot of the current game
     * @return a string displaying the appropriate player's Eval & WinChance
     * @throws Exception maybe whe requesting api data
     */
    public String BoardEval(Board board) throws Exception {
        String fen = convertToFen(board);
        JsonObject response = this.ApiInterface.request(fen);
        if (response.get("turn").getAsString().equals("w")){
            return this.whiteEval(board);
        } else { return this.blackEval(board); }

    }

    private String convertToFen(Board board) {
        return "";
    }

    private String whiteEval(Board board) throws Exception{
        String fen = convertToFen(board);
        JsonObject response = this.ApiInterface.request(fen);
        String result = "";
        result += "White WinChance: " + response.get("winChance") + "\n";
        result += "White Eval: " + response.get("eval");
        return result;
    }

    private String blackEval(Board board) throws Exception{
        String fen = convertToFen(board);
        JsonObject response = this.ApiInterface.request(fen);
        String result = "";
        result += "Black WinChance: " + (-1) * (1 - response.get("winChance").getAsDouble()) + "\n";
        result += "Black Eval: " + (-1) * response.get("eval").getAsDouble();
        return result;
    }


}
