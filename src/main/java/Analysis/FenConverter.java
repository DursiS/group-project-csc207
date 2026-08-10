package Analysis;

import MakeMove.Board;

public interface FenConverter {
    /**
     * Converts a board position into its FEN string.
     * @param board the board to convert
     * @param isWhiteTurn whether it is white's move
     * @return the position as a FEN string
     */
    String convertToFen(Board board, boolean isWhiteTurn);
}
