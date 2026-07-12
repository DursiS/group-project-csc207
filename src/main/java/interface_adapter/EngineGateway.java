package interface_adapter;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class EngineGateway {
//    private record OutputData(String text, double value, boolean condition) {}

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

    public String BestMoveMessage(String fen) throws Exception{
        JsonObject ping = this.ping(fen);
        return ping.get("text").getAsString();
    }
}
