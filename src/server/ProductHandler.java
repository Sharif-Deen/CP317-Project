package server;
import features.Product;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.net.URLDecoder;

public class ProductHandler implements HttpHandler {
    private static Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CORSUtil.addCORSHeaders(exchange);
        if (CORSUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("GET".equalsIgnoreCase(requestMethod)) {
            String queryString = exchange.getRequestURI().getQuery();
            Map<String, String> params = parseQuery(queryString);

            String search = params.get("search");
            String minPrice = params.get("minPrice");
            String maxPrice = params.get("maxPrice");
            String location = params.get("location");
            String stock = params.get("stock");

            //retrieve matching products from database by calling basicSearch methods
            //TODO: write a method that takes `search`, `minPrice`, `maxPrice`, `location`, and `stock` and queries database to return matching product objects in a list
            //ArrayList<Product> products = BasicSearch.searchProducts(search, minPrice, maxPrice, location, stock);
            ArrayList<Product> products = new ArrayList<Product>();
            
            //convert products to json objects array here
            String jsonResponse = gson.toJson(products);
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            // write to React
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> result = new java.util.HashMap<>();
        if (query == null || query.isEmpty()) return result;

        for (String pair : query.split("&")) {
            String[] idx = pair.split("=");
            try {
                String key = URLDecoder.decode(idx[0], "UTF-8");
                String value = idx.length > 1 ? URLDecoder.decode(idx[1], "UTF-8") : "";
                result.put(key, value);
            } catch (java.io.UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}