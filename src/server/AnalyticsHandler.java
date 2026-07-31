package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseInteract;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AnalyticsHandler implements HttpHandler {
    
    private static Gson gsonInstance = new Gson();

   @Override
    public void handle(HttpExchange exchange) throws IOException {
        
        ServerUtil.addCORSHeaders(exchange);
        if (ServerUtil.handlePreflight(exchange)) return;

        if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            try (DatabaseInteract database = new DatabaseInteract()) {
                
                Map<String, String> parameters = ServerUtil.parseQueryParameters(exchange.getRequestURI());

                // get by brand
                List<Map<String, Object>> analytics = database.getBrandSalesAnalytics(parameters.get("brand"));
                
                String jsonResponse = gsonInstance.toJson(analytics);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse);
                
            } catch (Exception exception) {
                Server.printError("Error fetching analytics: ", exception);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"error\": \"Failed to retrieve analytics.\"}");
            }
        }
    }
}