package use_case;

import com.google.gson.JsonObject;

public interface EngineGateway {
    double evaluate(String fen) throws Exception;
    String bestMoveMessage(String fen) throws Exception;
    String bestMove(String fen) throws Exception;
}
