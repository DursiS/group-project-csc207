package interface_adapter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.*;

public class ApiDemo {

    static JsonObject demo_raw_request() throws Exception{
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
        return JsonParser.parseString(response.body())
                .getAsJsonObject();
    }

    static void demo_raw_evaluation() throws Exception{
        JsonObject response =  demo_raw_request();
        System.out.println("White Eval: " + response.get("eval"));
        System.out.println("Black Eval:  " + (-1) * response.get("eval").getAsDouble());
        System.out.println("White WinChance: " + response.get("winChance"));
        System.out.println("Black WinChance: " + (-1) * (1 - response.get("winChance").getAsDouble()));

    }

    public static void main(String[] args) throws Exception {
        ApiDemo.demo_raw_evaluation();
    }



}
