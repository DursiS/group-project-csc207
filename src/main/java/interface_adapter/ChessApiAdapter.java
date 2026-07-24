package interface_adapter;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import use_case.ChessApiInterface;

public class ChessApiAdapter implements ChessApiInterface {
    private HttpResponse<String> ping(String fen) throws Exception {
        final String body = "{ \"fen\": \"" + fen + "\" }";

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chess-api.com/v1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Requests analysis for the given position.
     * @param fen the position as a FEN string
     * @return the API response as a JSON object
     * @throws Exception if the request fails
     */
    public JsonObject request(String fen) throws Exception {
        final HttpResponse<String> response = this.ping(fen);
        return JsonParser.parseString(response.body())
                .getAsJsonObject();
    }
}
