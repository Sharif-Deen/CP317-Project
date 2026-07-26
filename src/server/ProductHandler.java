package server;
import database.DatabaseInteract;
import features.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductHandler implements HttpHandler {
    private static Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ServerUtil.addCORSHeaders(exchange);
        if (ServerUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("GET".equalsIgnoreCase(requestMethod)) {
            // Get and send all products from database
            try (DatabaseInteract db = new DatabaseInteract()) {
                List<Product> products = db.findAllProducts();

                // Convert products to json objects array
                String jsonResponse = gson.toJson(products);

                //send
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse);

            } catch (Exception e){ // Return error message
                System.err.println("Error fetching products: " + e.getMessage());
                e.printStackTrace();
                String jsonResponse = "{\"error\": \"Failed to retrieve products from the database.\"}";
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, jsonResponse);
            }
            
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            // Add product to db
            try (DatabaseInteract db = new DatabaseInteract()) {
                InputStream requestBody = exchange.getRequestBody();
                String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                Product newProduct = gson.fromJson(requestString, Product.class);

                //TODO: needs to return product id
                boolean success = db.addProduct(newProduct);

                JsonObject jsonResponse = new JsonObject();
                if (success) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Product added successfully");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Failed to add product");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, jsonResponse.toString());
                }
            } catch (Exception e) {
                System.err.println("Error adding product: " + e.getMessage());
                e.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to add product to the database.\"}");
            }

        } else if ("DELETE".equalsIgnoreCase(requestMethod)) {
            // Delete product from db
            try (DatabaseInteract db = new DatabaseInteract()) {
                InputStream requestBody = exchange.getRequestBody();
                String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject deleteRequest = gson.fromJson(requestString, JsonObject.class);
                int productId = deleteRequest.get("id").getAsInt();

                boolean success = db.removeProductById(productId);

                JsonObject jsonResponse = new JsonObject();
                if (success) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Product removed successfully");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Product not found");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_NOT_FOUND, jsonResponse.toString());
                }
            } catch (Exception e) {
                System.err.println("Error deleting product: " + e.getMessage());
                e.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to remove product from the database.\"}");
            }
            
        } else if ("PUT".equalsIgnoreCase(requestMethod)) {
            // Update stock in db
            try (DatabaseInteract db = new DatabaseInteract()) {
                String path = exchange.getRequestURI().getPath();
                String[] pathParts = path.split("/");
                
                // Expects: /api/products/{id}/stock
                int productId = Integer.parseInt(pathParts[3]);

                InputStream requestBody = exchange.getRequestBody();
                String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                JsonObject putRequest = gson.fromJson(requestString, JsonObject.class);
                int newStock = putRequest.get("stock").getAsInt();

                boolean success = db.updateProductStock(productId, newStock);

                JsonObject jsonResponse = new JsonObject();
                if (success) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Stock updated successfully");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Product not found");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_NOT_FOUND, jsonResponse.toString());
                }
            } catch (Exception e) {
                System.err.println("Error updating stock: " + e.getMessage());
                e.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to update stock in database.\"}");
            }
        }
    }
}