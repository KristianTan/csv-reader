import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) {
        // Hardcoded file location
        try (BufferedReader br = new BufferedReader(new FileReader("sample.csv"))) {
            String line;

            // Skip the headers
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String json = toJson(values);

                sendRequest(json);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toJson(String[] values) {
        return "{"
                + "\"customerRef\":\"" + values[0] + "\","
                + "\"customerName\":\"" + values[1] + "\","
                + "\"addressLine1\":\"" + values[2] + "\","
                + "\"addressLine2\":\"" + values[3] + "\","
                + "\"town\":\"" + values[4] + "\","
                + "\"county\":\"" + values[5] + "\","
                + "\"country\":\"" + values[6] + "\","
                + "\"postcode\":\"" + values[7] + "\""
                + "}";
    }


    private static void sendRequest(String json) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                // Hardcoded endpoint.  Better as an env var.
                .uri(URI.create("http://localhost:8080/api/items"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Better logging needed
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
