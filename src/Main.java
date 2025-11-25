import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String csvFile = "sample.csv";
        String apiUrl = "http://localhost:8080/api/items";
        HttpClient client = HttpClient.newHttpClient();

        try {
            List<String[]> rows = parseCsv(new FileReader(csvFile));

            for (String[] row : rows) {
                String json = toJson(row);
                HttpResponse<String> response = sendRequest(apiUrl, json, client);
                System.out.println(response.statusCode() + ": " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String[]> parseCsv(Reader reader) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",", -1);
                rows.add(values);
            }
        }
        return rows;
    }

    public static String toJson(String[] values) {
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

    public static HttpResponse<String> sendRequest(String apiUrl, String json, HttpClient client)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Better logging needed
        System.out.println(response.statusCode());
        System.out.println(response.body());
        return response;
    }
}
