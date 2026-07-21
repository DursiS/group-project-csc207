package use_case;

import com.google.gson.JsonObject;
import entity.Board;

import java.util.*;

public class AnalyzeMoveInteractor{
    private final ChessApiInterface ApiInterface;
    private final AnalyzeOutputBoundary AnalyzeOutputBoundary;
    private static final Map<Integer, Character> PIECE_TO_FEN = buildPieceToFen();

    /**
     * Builds the piece-code to FEN-character lookup map.
     * @return the piece-to-FEN map
     */
    // Builder Design Pattern into a method for encapsulation
    private static Map<Integer, Character> buildPieceToFen() {
        Map<Integer, Character> map = new HashMap<>();

        map.put(0, '0');    // empty-square

        // White Pieces
        map.put( 1, 'P');   // pawn
        map.put( 2, 'P');   // pawn, moved
        map.put( 3, 'P');   // pawn, en-passant
        map.put( 4, 'R');   // rook
        map.put( 5, 'R');   // rook, moved
        map.put( 6, 'N');   // knight
        map.put( 7, 'B');   // bishop
        map.put( 8, 'Q');   // queen
        map.put( 9, 'K');   // king
        map.put(10, 'K');   // king, moved

        // Black Pieces (lowercase and negative)
        map.put(-1, 'p');
        map.put(-2, 'p');
        map.put(-3, 'p');
        map.put(-4, 'r');
        map.put(-5, 'r');
        map.put(-6, 'n');
        map.put(-7, 'b');
        map.put(-8, 'q');
        map.put(-9, 'k');
        map.put(-10, 'k');

        return map;
    }

    /**
     * Constructs the interactor with the chess API dependency.
     * @param ApiInterface the chess API to use
     */
    public AnalyzeMoveInteractor(ChessApiInterface ApiInterface,
                                 AnalyzeOutputBoundary analyzeOutputBoundary) {
        // Dependency-injections with interfaces (DIP)
        this.ApiInterface = ApiInterface;
        this.AnalyzeOutputBoundary = analyzeOutputBoundary;
    }

    /**
     * Analyzes the current turn and returns the eval and best move as text.
     * @param board the current board
     */
    public void executeTurnAnalysis(Board board) throws Exception{
        String fen = convertToFen(board);
        this.AnalyzeOutputBoundary.addMessage(getFinalMessage(fen));
    }

    /**
     * Builds the full analysis message for the given position.
     * @param fen the position as a FEN string
     * @return the analysis text
     */
    public String getFinalMessage(String fen) throws Exception{
        String result_tail = this.boardEval(fen) + "\n" + this.bestMove(fen);
        if (isWhiteTurn(fen)) {
            return "== White's Metrics: == \n" + result_tail;
        } else {
            return "== Black's Metrics: == \n" + result_tail;
        }
    }

    /**
     * Checks whether it is White's turn.
     * @param fen the position as a FEN string
     * @return true if it is White's turn
     */
    private boolean isWhiteTurn(String fen) throws Exception{
        JsonObject response = this.ApiInterface.request(fen);
        return response.get("turn").getAsString().equals("w");
        }

    /**
     * Returns the evaluation for whoever's turn it is.
     * @param fen the position as a FEN string
     * @return the eval text
     */
    private String boardEval(String fen) throws Exception {
        JsonObject response = this.ApiInterface.request(fen);
        if (isWhiteTurn(fen)){
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
        return "White WinChance: " + response.get("winChance") + "% \n"
                + "White Eval: " + response.get("eval");
    }

    /**
     * Returns Black's win chance and evaluation.
     * @param fen the position as a FEN string
     * @return the black eval text
     */
    private String blackEval(String fen) throws Exception{
        JsonObject response = this.ApiInterface.request(fen);
        return "Black WinChance: " + (-1) * (
                1 - response.get("winChance")
                .getAsDouble()) + "% \n"
                + "Black Eval: " + (-1) * response.get("eval").getAsDouble();
    }

    /**
     * Converts a Board into its FEN string.
     * @param board the board to convert
     * @return the FEN string
     */
    public String convertToFen(Board board) {
        // Example: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
        return buildFenGrid(board) +
                buildFenTail(board);
    }

    /**
     * Builds the FEN suffix: turn, castling, en passant, and counters.
     * @param board the board to convert
     * @return the FEN tail string
     */
    private String buildFenTail(Board board) {
        StringBuilder result = new StringBuilder();

        if ((board.getTurn() % 2) == 0){ result.append(" w "); }
        else { result.append(" b "); }

        if (board.getSquare(4, 7) == 9) {
            if (board.getSquare(7, 7) == 4) result.append('K');
            if (board.getSquare(0, 7) == 4) result.append('Q');
        }
        if (board.getSquare(4, 0) == -9) {
            if (board.getSquare(7, 0) == -4) result.append('k');
            if (board.getSquare(0, 0) == -4) result.append('q');
        }

        result.append(' ')
                .append(enPassantSquare(board))
                .append(" 0 1");
        return result.toString();
    }

    /**
     * Builds the FEN board grid, row by row.
     * @param board the board to convert
     * @return the FEN grid string
     */
    private static String buildFenGrid(Board board) {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < 8; row ++){
            int emptyCount = 0;
            for (int col = 0; col < 8; col ++){
                char piece = PIECE_TO_FEN.get(board.getSquare(col, row));
                if (piece == '0') {
                    emptyCount += 1;
                } else {
                    if (emptyCount > 0) { result.append(emptyCount); emptyCount = 0; }
                    result.append(piece);
                }
            }
            if (emptyCount > 0) { result.append(emptyCount); }

            if (!(row == 7)) {
                result.append("/");}
        }
        return result.toString();
    }

    /**
     * Finds the en passant target square, if any.
     * @param board the board to check
     * @return the target square in algebraic notation, or "-"
     */
    private String enPassantSquare(Board board) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int code = board.getSquare(x, y);
                if (code == 3) {
                    return enPassantSquareAlgebra(x, y + 1);
                }
                if (code == -3) {
                    return enPassantSquareAlgebra(x, y - 1);
                }
            }
        }
        return "-";
    }

    /**
     * Converts board coordinates into algebraic notation.
     * @param x the file index (0-7)
     * @param y the rank index (0-7)
     * @return the square in algebraic notation, e.g. "e3"
     */
    private String enPassantSquareAlgebra(int x, int y) {
        char file = (char) ('a' + x);
        int rank = 8 - y;
        return "" + file + rank;
    }
}
