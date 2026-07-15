package interface_adapter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import use_case.ChessApiInterface;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChessApiAdapter implements ChessApiInterface {
    private HttpResponse<String> ping(String fen) throws Exception {
        String body = "{ \"fen\": \"" + fen + "\" }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chess-api.com/v1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public JsonObject request(String fen) throws Exception {
        HttpResponse<String> response = this.ping(fen);
        return JsonParser.parseString(response.body())
                .getAsJsonObject();
    }


}
