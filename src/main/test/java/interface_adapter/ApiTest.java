package interface_adapter;

import java.net.URI;
import java.net.http.*;

import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    void test_raw_request() throws Exception{
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String body = "{ \"fen\": \"" + fen + "\" }";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()

                // Set the api url to request from
                .uri(URI.create("https://chess-api.com/v1"))

                // Metadata about the request I am sending to be in json
                .header("Content-Type", "application/json")

                // Sending data to process, by wrapping feeds into bytes
                .POST(HttpRequest.BodyPublishers.ofString(body))

                // Construct a HttpRequest
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Just a raw JsonObject to see its formatting
        System.out.println(response.body());
    }

}
