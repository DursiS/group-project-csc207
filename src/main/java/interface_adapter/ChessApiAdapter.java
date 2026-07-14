package interface_adapter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import use_case.EngineGateway;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChessApiEngineGateway implements EngineGateway {
    public JsonObject ping(String fen) throws Exception{
        String body = "{ \"fen\": \"" + fen + "\" }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chess-api.com/v1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser.parseString(response.body())
                .getAsJsonObject();
    }

    @Override
    public double evaluate(String fen) throws Exception{
        JsonObject ping = this.ping(fen);
        return ping.get("eval").getAsDouble();
    }

    @Override
    public String bestMoveMessage(String fen) throws Exception {
        JsonObject ping = this.ping(fen);
        return ping.get("text").getAsString();
    }

    @Override
    public String bestMove(String fen) throws Exception {
        JsonObject ping = this.ping(fen);
        return ping.get("move").getAsString();
    }

}
