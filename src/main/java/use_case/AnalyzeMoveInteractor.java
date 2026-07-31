package use_case;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import entity.Board;
import interface_adapter.AnalyzeInputBoundary;

public class AnalyzeMoveInteractor implements AnalyzeInputBoundary {

    // To avoid "magic" numbers or strings
    private static final String WHITE_FEN_BY_CODE = "0PPPRRNBQKK";
    private static final Map<Integer, Character> PIECE_TO_FEN = buildPieceToFen();
    private static final int BOARD_SIZE = 8;
    private static final int LAST_INDEX = BOARD_SIZE - 1;
    private static final int KING_FILE = 4;
    private static final int KING = 9;
    private static final int ROOK = 4;
    private static final int EN_PASSANT_PAWN = 3;
    private static final double NUM_TO_ROUND = 100.0;

    private final ChessApiInterface apiInterface;
    private final AnalyzeOutputBoundary analyzeOutputBoundary;
    private final GameStateDataAccessInterface gameStateDataAccess;

    /**
     * Constructs the interactor with its dependencies.
     * Uses dependencies injecting instead of hard dependencies.
     * @param apiInterface the chess API to use
     * @param analyzeOutputBoundary the output boundary to present results
     * @param gameStateDataAccessInterface the source of the current board
     */
    public AnalyzeMoveInteractor(ChessApiInterface apiInterface,
                                 AnalyzeOutputBoundary analyzeOutputBoundary,
                                 GameStateDataAccessInterface gameStateDataAccessInterface) {
        // Need to make sure the retrieval of recent board states works when
        // finally implemented.
        this.apiInterface = apiInterface;
        this.analyzeOutputBoundary = analyzeOutputBoundary;
        this.gameStateDataAccess = gameStateDataAccessInterface;
    }

    @Override
    public void executeTurnAnalysis() throws IOException {
        final Board board = this.gameStateDataAccess.getRecentBoard();
        final String fen = convertToFen(board);
        this.analyzeOutputBoundary.addMessage(getAnalysisMessage(fen));
    }

    /**
     * Builds the piece code to FEN-character lookup map for translation.
     * @return the piece-to-FEN map
     */
    private static Map<Integer, Character> buildPieceToFen() {
        final Map<Integer, Character> map = new HashMap<>();
        for (int code = 0; code < WHITE_FEN_BY_CODE.length(); code++) {
            final char white = WHITE_FEN_BY_CODE.charAt(code);
            map.put(code, white);
            if (code > 0) {
                map.put(-code, Character.toLowerCase(white));
            }
        }
        return map;
    }

    /**
     * Builds the full analysis message for the given position.
     * @param fen the position as a FEN string
     * @return the analysis text
     * @throws IOException if the request fails
     */
    public String getAnalysisMessage(String fen) throws IOException {
        final String resultTail = this.boardEval(fen) + "\n" + this.bestMove(fen);
        final String header;
        if (isWhiteTurn(fen)) {
            header = "== WHITE'S TURN == \n\nWhite's Metrics: \n";
        }
        else {
            header = "== BLACK'S TURN == \n\nBlack's Metrics: \n";
        }
        return header + resultTail;
    }

    /**
     * Checks whether it is White's turn, read from the fen.
     * @param fen the position as a FEN string
     * @return true if it is White's turn
     */
    private boolean isWhiteTurn(String fen) {
        return "w".equals(fen.split(" ")[1]);
    }

    /**
     * Sends a request and fails fast if the API rejects the position.
     * @param fen the position as a FEN string
     * @return the API response
     * @throws IOException if the request fails
     * @throws IllegalStateException if the API rejects the position
     */
    private JsonObject requestOrThrow(String fen) throws IOException {
        final JsonObject response = this.apiInterface.request(fen);
        if (response.has("type") && "error".equals(response.get("type").getAsString())) {
            throw new IllegalStateException("Chess API rejected the position: "
                    + response.get("text"));
        }
        return response;
    }

    /**
     * Returns the evaluation for whoever's turn it is.
     * @param fen the position as a FEN string
     * @return the eval text
     * @throws IOException if the request fails
     */
    private String boardEval(String fen) throws IOException {
        final String result;
        if (isWhiteTurn(fen)) {
            result = this.whiteEval(fen);
        }
        else {
            result = this.blackEval(fen);
        }
        return result;
    }

    /**
     * Returns the best move for the given position as "from -> to".
     * @param fen the position as a FEN string
     * @return the best move text
     * @throws IOException if the request fails
     */
    private String bestMove(String fen) throws IOException {
        final JsonObject response = requestOrThrow(fen);
        return "Best Move: " + response.get("from").getAsString()
                + " -> " + response.get("to").getAsString();
    }

    /**
     * Returns White's win chance and evaluation.
     * @param fen the position as a FEN string
     * @return the white eval text
     * @throws IOException if the request fails
     */
    private String whiteEval(String fen) throws IOException {
        final JsonObject response = requestOrThrow(fen);
        final double winChance = roundTwo(response.get("winChance").getAsDouble());
        return "White WinChance: " + winChance + "% \n"
                + "White Eval: " + response.get("eval");
    }

    /**
     * Returns Black's win chance and evaluation.
     * @param fen the position as a FEN string
     * @return the black eval text
     * @throws IOException if the request fails
     */
    private String blackEval(String fen) throws IOException {
        final JsonObject response = requestOrThrow(fen);
        final double winChance = roundTwo((-1) * (1 - response.get("winChance").getAsDouble()));
        return "Black WinChance: " + winChance + "% \n"
                + "Black Eval: " + (-1) * response.get("eval").getAsDouble();
    }

    /**
     * Rounds a value to two decimal places.
     * @param value the value to round
     * @return the value rounded to two decimals
     */
    private static double roundTwo(double value) {
        return Math.round(value * NUM_TO_ROUND) / NUM_TO_ROUND;
    }

    /**
     * Converts a Board into its FEN string.
     * @param board the board to convert
     * @return the FEN string
     */
    public String convertToFen(Board board) {
        return buildFenGrid(board)
                + buildFenTail(board);
    }

    /**
     * Builds the fen ending portion.
     * @param board the board to convert
     * @return the FEN tail string
     */
    private String buildFenTail(Board board) {
        final StringBuilder result = new StringBuilder();

        if (board.getTurn() % 2 == 0) {
            result.append(" w ");
        }
        else {
            result.append(" b ");
        }

        if (board.getSquare(KING_FILE, LAST_INDEX) == KING) {
            if (board.getSquare(LAST_INDEX, LAST_INDEX) == ROOK) {
                result.append('K');
            }
            if (board.getSquare(0, LAST_INDEX) == ROOK) {
                result.append('Q');
            }
        }
        if (board.getSquare(KING_FILE, 0) == -KING) {
            if (board.getSquare(LAST_INDEX, 0) == -ROOK) {
                result.append('k');
            }
            if (board.getSquare(0, 0) == -ROOK) {
                result.append('q');
            }
        }

        result.append(' ')
                .append(enPassantSquare(board))
                .append(" 0 1");
        return result.toString();
    }

    /**
     * Builds the FEN board grid, row by row.
     * @param board the board to convert
     * @return the fen grid string
     */
    private static String buildFenGrid(Board board) {
        final StringBuilder result = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++) {
            int emptyCount = 0;
            for (int col = 0; col < BOARD_SIZE; col++) {
                final char piece = PIECE_TO_FEN.get(board.getSquare(col, row));
                if (piece == '0') {
                    emptyCount++;
                }
                else {
                    if (emptyCount > 0) {
                        result.append(emptyCount);
                        emptyCount = 0;
                    }
                    result.append(piece);
                }
            }
            if (emptyCount > 0) {
                result.append(emptyCount);
            }
            if (row != LAST_INDEX) {
                result.append("/");
            }
        }
        return result.toString();
    }

    /**
     * Finds the en passant target square, if any.
     * @param board the board to check
     * @return the target square in algebraic notation, or "-" otherwise
     */
    private String enPassantSquare(Board board) {
        String square = "-";
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                final int code = board.getSquare(x, y);
                if (code == EN_PASSANT_PAWN) {
                    square = enPassantSquareAlgebra(x, y + 1);
                }
                else if (code == -EN_PASSANT_PAWN) {
                    square = enPassantSquareAlgebra(x, y - 1);
                }
            }
        }
        return square;
    }

    /**
     * Converts board coordinates into algebraic notation.
     * @param fileIndex the row index (0-7)
     * @param rankIndex the column index (0-7)
     * @return the square in algebraic notation, like "e3"
     */
    private String enPassantSquareAlgebra(int fileIndex, int rankIndex) {
        final char file = (char) ('a' + fileIndex);
        final int rank = BOARD_SIZE - rankIndex;
        return "" + file + rank;
    }

}
