package Analysis;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ApiDemo {

    static JsonObject demoRawRequest() throws Exception {
        final String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        final String body = "{ \"fen\": \"" + fen + "\" }";

        final HttpClient client = HttpClient.newHttpClient();
        final HttpRequest request = HttpRequest.newBuilder()

                // Set the api url to request from
                .uri(URI.create("https://chess-api.com/v1"))

                // Metadata about the request I am sending to be in json
                .header("Content-Type", "application/json")

                // Sending data to process, by wrapping feeds into bytes
                .POST(HttpRequest.BodyPublishers.ofString(body))

                // Construct a HttpRequest
                .build();

        final HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Just a raw JsonObject to see its formatting
        System.out.println(response.body());
        return JsonParser.parseString(response.body())
                .getAsJsonObject();
    }

    static void demoRawEvaluation() throws Exception {
        final JsonObject response = demoRawRequest();
        System.out.println("White Eval: " + response.get("eval"));
        System.out.println("Black Eval:  " + (-1) * response.get("eval").getAsDouble());
        System.out.println("White WinChance: " + response.get("winChance"));
        System.out.println("Black WinChance: " + (-1) * (1 - response.get("winChance").getAsDouble()));
    }

    /**
     * Runs the raw API evaluation demo.
     * @param args the command-line arguments
     * @throws Exception if the request fails
     */
    public static void main(String[] args) throws Exception {
        ApiDemo.demoRawEvaluation();
    }
}
