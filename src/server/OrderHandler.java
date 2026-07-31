package server;


//  =================
//   IMPORTS:
//  =================


import database.DatabaseInteract;
import features.Order;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//  =================
//   ORDER HANDLER:
//  =================


public class OrderHandler implements HttpHandler {
    // Gson instance for JSON serialization/deserialization.
    private static Gson gsonInstance = new Gson();


    // =================
    //  HELPER METHODS:
    // =================

    // Helper to read request body as a UTF-8 string.
    // paramaters: requestBody - InputStream containing request body.
    // returns: String containing request body as a UTF-8 string.
    private static String readRequestBody(InputStream requestBody) throws IOException {
        return new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
    }


    // =================
    //  HANDLE METHOD:
    // =================


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Add CORS headers to response and handle preflight requests.
        ServerUtil.addCORSHeaders(exchange);
        if (ServerUtil.handlePreflight(exchange)) return;

        // Get the HTTP request method.
        String method = exchange.getRequestMethod();

        // Handle GET requests to retrieve orders.
        if ("GET".equalsIgnoreCase(method)) {
            List<Order> orderList = new ArrayList<>();

            // Interact with database to fetch orders based on query parameters.
            try (DatabaseInteract database = new DatabaseInteract()) {
                // Parse query parameters if present.
                Map<String, String> queryParameters = ServerUtil.parseQueryParameters(exchange.getRequestURI());

                // Fetch orders based on query parameters.
                if (queryParameters.containsKey("email")) {
                    // If "email" parameter, fetch orders by email.
                    orderList = database.findOrdersByEmail(queryParameters.get("email"));

                } else if (queryParameters.containsKey("brand")) {
                    // If "brand" parameter, fetch orders by brand.
                    orderList = database.findOrdersByBrand(queryParameters.get("brand"));

                } else if (queryParameters.containsKey("username")){
                    orderList = database.findOrdersByUsername(queryParameters.get("username"));

                } else {
                    // If no specific parameter, fetch all orders.
                    orderList = database.findAllOrders();

                }

                // Convert list of orders to JSON and send response.
                String jsonResponse = gsonInstance.toJson(orderList);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse);
    
            // Handle exceptions that occur during database interaction.
            } catch (Exception exception) {
                System.err.println("Error fetching orders: " + exception.getMessage());
                
                // Print stack trace.
                exception.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\",\"message\": \"Failed to retrieve orders from the database.\"}");
            }

        }

        // Handle POST requests to add new order.
        else if ("POST".equalsIgnoreCase(method)) {
            
            // handle adding an order to database.
            try (DatabaseInteract database = new DatabaseInteract()) {

                // Read request body and deserialize into Order object.
                String requestBodyString = readRequestBody(exchange.getRequestBody());
                Order orderToInsert = gsonInstance.fromJson(requestBodyString, Order.class);

                if (orderToInsert == null) {
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_BAD_REQUEST, "{\"status\": \"error\", \"message\": \"Order payload is invalid\"}");
                    return;
                }
                
                if (orderToInsert.getProducts() == null || orderToInsert.getProducts().isEmpty()) {
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_BAD_REQUEST, "{\"status\": \"error\", \"message\": \"Order must include at least one product\"}");
                    return;
                }

                // Update price
                orderToInsert.recalculateTotalPrice();

                // Add order to database and get new order number.
                int newOrderNumber = database.addOrder(orderToInsert);

                // Prepare JSON response based on success or failure of adding order.
                JsonObject jsonResponse = new JsonObject();

                // If new order number is positive, addition was successful.
                if (newOrderNumber > 0) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("orderNumber", newOrderNumber);
                    jsonResponse.addProperty("message", "Order added successfully");
                    
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());

                // If new order number is not positive, addition failed.
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Failed to add order");
                    
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, jsonResponse.toString());
                }

            // Handle exceptions that occur during database interaction.
            } catch (JsonSyntaxException exception) {
                System.err.println("Invalid order payload: " + exception.getMessage());
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_BAD_REQUEST, "{\"status\": \"error\", \"message\": \"Invalid order payload\"}");
            } catch (Exception exception) {
                System.err.println("Error adding order: " + exception.getMessage());

                // Print stack trace.
                exception.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to add order to the database.\"}");
            }
        }
        return;
    }
}