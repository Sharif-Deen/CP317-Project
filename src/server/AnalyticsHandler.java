package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseInteract;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsHandler implements HttpHandler {
    
    private static Gson gsonInstance = new Gson();

    private static Map<String, String> parseQueryParameters(URI uri) {
        Map<String, String> queryParameters = new LinkedHashMap<>();
        String query = uri != null ? uri.getQuery() : null;
        if (query != null && !query.isEmpty()) {
            for (String pair : query.split("&")) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParameters;
    }

   @Override
    public void handle(HttpExchange exchange) throws IOException {
        
        ServerUtil.addCORSHeaders(exchange);
        if (ServerUtil.handlePreflight(exchange)) return;

       if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
    try (DatabaseInteract database = new DatabaseInteract()) {
        
        // We are ignoring the 'brand' parameter completely now.
        // It will fetch everything in the database.
        List<Map<String, Object>> analytics = database.getAllSalesAnalytics();
        
        String jsonResponse = gsonInstance.toJson(analytics);
        ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse);
        
    } catch (Exception exception) {
                Server.printError("Error fetching analytics: ", exception);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"error\": \"Failed to retrieve analytics.\"}");
            }
        }
    }
}