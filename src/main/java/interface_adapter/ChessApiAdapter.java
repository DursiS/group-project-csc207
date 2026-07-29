package interface_adapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import use_case.ChessApiInterface;

public class ChessApiAdapter implements ChessApiInterface {
    private HttpResponse<String> ping(String fen) throws IOException {
        final String body = "{ \"fen\": \"" + fen + "\" }";
        // perfect body formatting

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chess-api.com/v1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (InterruptedException ex) {
            // closes the thread
            Thread.currentThread().interrupt();
            throw new IOException("The API request was interrupted", ex);
        }
    }

    /**
     * Requests analysis for the given position.
     * @param fen the position as a FEN string
     * @return the API response as a JSON object
     * @throws IOException if the request fails
     */
    public JsonObject request(String fen) throws IOException {
        final HttpResponse<String> response = this.ping(fen);
        return JsonParser.parseString(response.body())
                .getAsJsonObject();
    }
}
