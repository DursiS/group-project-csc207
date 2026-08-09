package Analysis;

import entity.Board;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates a Board into its FEN string representation.
 */
public class BoardToFenAdapter {

    private static final String WHITE_FEN_BY_CODE = "0PPPRRNBQKK";
    private static final Map<Integer, Character> PIECE_TO_FEN = buildPieceToFen();
    private static final int BOARD_SIZE = 8;
    private static final int LAST_INDEX = BOARD_SIZE - 1;
    private static final int KING_FILE = 4;
    private static final int KING = 9;
    private static final int ROOK = 4;
    private static boolean isWhiteTurn;

    /**
     * Converts a Board into its FEN string.
     * @param board the board to convert
     * @param isWhiteTurn is the turn white's
     * @return the FEN string
     */
    public String convertToFen(Board board, boolean isWhiteTurn) {
        this.isWhiteTurn = isWhiteTurn;
        return buildFenGrid(board)
                + buildFenTail(board);
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
     * Builds the fen ending portion.
     * @param board the board to convert
     * @return the FEN tail string
     */
    private String buildFenTail(Board board) {
        final StringBuilder result = new StringBuilder();

        if (isWhiteTurn) {
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

        // en passant is always "-": api rejects any FEN with en passant squares
        result.append(" - 0 1");
        return result.toString();
    }
}
