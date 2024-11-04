import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class PropertyServer {

    private static final List<Property> properties = List.of(
            new Property("Charming 3 Bedroom House", "1234 University St", "House", 30000),
            new Property("Modern Apartment Near Campus", "5678 Student Lane", "Apartment", 35000),
            new Property("Spacious Dormitory Room", "9101 Dorm Blvd", "Dormitory", 25000));

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/search", new SearchHandler());
        server.createContext("/filter", new FilterHandler());
        server.setExecutor(null);
        System.out.println("Server started on http://localhost:8080/");
        server.start();
    }

    static class SearchHandler implements HttpHandler {
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            String name = query != null && query.contains("name=") ? query.split("=")[1] : "";

            List<Property> results = properties.stream()
                    .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());

            String response = mapper.writeValueAsString(results);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    static class FilterHandler implements HttpHandler {
        private final ObjectMapper mapper = new ObjectMapper();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> params = queryToMap(query);

            String type = params.getOrDefault("type", "");
            int maxPrice = Integer.parseInt(params.getOrDefault("maxPrice", "0"));

            List<Property> results = properties.stream()
                    .filter(p -> (type.isEmpty() || p.getPropertyType().equalsIgnoreCase(type))
                            && p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());

            String response = mapper.writeValueAsString(results);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private Map<String, String> queryToMap(String query) {
            return query == null ? Map.of() : Map.of(query.split("=")[0], query.split("=")[1]);
        }
    }

    static class Property {
        private String name;
        private String location;
        private String propertyType;
        private int price;

        public Property(String name, String location, String propertyType, int price) {
            this.name = name;
            this.location = location;
            this.propertyType = propertyType;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getPropertyType() {
            return propertyType;
        }

        public int getPrice() {
            return price;
        }
    }
}
