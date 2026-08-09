package Analysis;

/**
 * Bundles the raw analysis data points for the presenter to format.
 * @param winChance the raw win chance from the API
 * @param eval the raw evaluation from the API
 * @param from the best move's origin square
 * @param to the best move's destination square
 * @param isWhiteTurn whether it is White's turn
 * @param messageNum which number move of the game this is
 */
public record AnalyzeOutputData(double winChance, double eval, String from,
                                String to, boolean isWhiteTurn, int messageNum) {
}
