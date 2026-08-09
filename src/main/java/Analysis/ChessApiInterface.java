package Analysis;

import java.io.IOException;

import com.google.gson.JsonObject;

public interface ChessApiInterface {
    /**
     * Requests analysis for the given position.
     * @param fen the position as a FEN string
     * @return the API response as a JSON object
     * @throws IOException if the request fails
     */
    JsonObject request(String fen) throws IOException;
}
