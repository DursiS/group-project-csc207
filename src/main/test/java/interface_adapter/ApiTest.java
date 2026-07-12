package interface_adapter;

import java.net.URI;
import java.net.http.*;

public class ApiTest {
    public static void main(String[] args) throws Exception {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String body = "{ \"fen\": \"" + fen + "\" }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://chess-api.com/v1"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());   // just a raw JSON dict
    }
}
