package use_case;

import com.google.gson.JsonObject;
import entity.Board;

public class AnalyzeMoveInteractor{
    private final ChessApiInterface ApiInterface;

    /**
     * Constructs the interactor with the chess API dependency.
     * @param ApiInterface the chess API to use
     */
    public AnalyzeMoveInteractor(ChessApiInterface ApiInterface) {
        this.ApiInterface = ApiInterface;  // Dependency-injection (DIP)
    }

    /**
     * Analyzes the current turn and returns the eval and best move as text.
     * @param board the current board
     * @return the analysis text
     */
    public String newTurnAnalysis(Board board) throws Exception{
        String fen = convertToFen(board);
        JsonObject response = this.ApiInterface.request(fen);

        String result = this.boardEval(fen) + "\n" + this.bestMove(fen);
        if (response.get("turn").getAsString().equals("w")) {
            result = "White:\n" + result;
        } else { result = "Black:\n" + result; }
        return result;
    }

    /**
     * Returns the evaluation for whoever's turn it is.
     * @param fen the position as a FEN string
     * @return the eval text
     */
    private String boardEval(String fen) throws Exception {
        JsonObject response = this.ApiInterface.request(fen);
        if (response.get("turn").getAsString().equals("w")){
            return this.whiteEval(fen);
        } else { return this.blackEval(fen); }

    }

    /**
     * Returns the best move for the given position as "from -> to".
     * @param fen the position as a FEN string
     * @return the best move text
     */
    private String bestMove(String fen) throws Exception {
        JsonObject response = this.ApiInterface.request(fen);
        return response.get("from").getAsString() + " -> " + response.get("to").getAsString();
    }

    /**
     * Returns White's win chance and evaluation.
     * @param fen the position as a FEN string
     * @return the white eval text
     */
    private String whiteEval(String fen) throws Exception{
        JsonObject response = this.ApiInterface.request(fen);
        String result = "";
        result += "White WinChance: " + response.get("winChance") + "\n";
        result += "White Eval: " + response.get("eval");
        return result;
    }

    /**
     * Returns Black's win chance and evaluation.
     * @param fen the position as a FEN string
     * @return the black eval text
     */
    private String blackEval(String fen) throws Exception{
        JsonObject response = this.ApiInterface.request(fen);
        String result = "";
        result += "Black WinChance: " + (-1) * (1 - response.get("winChance").getAsDouble()) + "\n";
        result += "Black Eval: " + (-1) * response.get("eval").getAsDouble();
        return result;
    }

    /**
     * Converts a Board into its FEN string.
     * @param board the board to convert
     * @return the FEN string
     */
    private String convertToFen(Board board) {
        return ""; // Implement after Board is done
    }


}
