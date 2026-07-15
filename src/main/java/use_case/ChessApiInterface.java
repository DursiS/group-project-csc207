package use_case;

import com.google.gson.JsonObject;

public interface ChessApiInterface {
    public JsonObject request(String fen) throws Exception;
}
