import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    private static final String API_KEY = "bd5e378503939ddaee76f12ad7a97608"; 
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a location:");
        String location = scanner.nextLine();
        try {
            getWeather(location);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void getWeather(String location) throws IOException {
        String urlString = BASE_URL + location + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON data manually
            String responseBody = response.toString();
            parseWeatherData(responseBody);
        }
    }

    public static void parseWeatherData(String responseBody) {
        String temp = extractValue(responseBody, "\"temp\":", ",");
        String humidity = extractValue(responseBody, "\"humidity\":", ",");
        String description = extractValue(responseBody, "\"description\":\"", "\"");

        System.out.println("Current Temperature: " + temp + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Description: " + description);
    }

    public static String extractValue(String json, String key, String delimiter) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(delimiter, startIndex);
        return json.substring(startIndex, endIndex).replaceAll("\"", "");
    }
}


