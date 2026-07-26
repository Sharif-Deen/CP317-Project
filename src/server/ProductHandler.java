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
                Server.printError("Error fetching products: ",e);
                String jsonResponse = "{\"error\": \"Failed to retrieve products from the database.\"}";
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, jsonResponse);
            }
            
        } else if ("POST".equalsIgnoreCase(requestMethod)) {
            // Add product to db
            try (DatabaseInteract db = new DatabaseInteract()) {
                InputStream requestBody = exchange.getRequestBody();
                String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                Product newProduct = gson.fromJson(requestString, Product.class);

                int productId = db.addProduct(newProduct);

                JsonObject jsonResponse = new JsonObject();
                if (productId > 0) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Product added successfully");
                    jsonResponse.addProperty("productId", productId);
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Failed to add product");
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, jsonResponse.toString());
                }
            } catch (Exception e) {
                Server.printError("Error adding product: ", e);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to add product to the database.\"}");
            }

        } else if ("DELETE".equalsIgnoreCase(requestMethod)) {
            // Delete product from db
            try (DatabaseInteract db = new DatabaseInteract()) {
                String path = exchange.getRequestURI().getPath();
                String[] pathParts = path.split("/");
                int productId = Integer.parseInt(pathParts[3]);

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
                Server.printError("Error deleting product: ",e);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to remove product from the database.\"}");
            }
            
        } else if ("PUT".equalsIgnoreCase(requestMethod)) {
            try (DatabaseInteract db = new DatabaseInteract()) {
                String path = exchange.getRequestURI().getPath();
                String[] pathParts = path.split("/");

                InputStream requestBody = exchange.getRequestBody();
                String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

                // /api/products/{id}/stock -> stock update only
                // /api/products/{id}       -> full product edit
                if (pathParts.length >= 5 && pathParts[4].equals("stock")) {
                    int productId = Integer.parseInt(pathParts[3]);
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

                } else {
                    // full product edit
                    Product updatedProduct = gson.fromJson(requestString, Product.class);
                    boolean success = db.updateProduct(updatedProduct);

                    JsonObject jsonResponse = new JsonObject();
                    if (success) {
                        jsonResponse.addProperty("status", "success");
                        jsonResponse.addProperty("message", "Product updated successfully");
                        ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
                    } else {
                        jsonResponse.addProperty("status", "error");
                        jsonResponse.addProperty("message", "Failed to update product");
                        ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, jsonResponse.toString());
                    }
                }
            } catch (Exception e) {
                Server.printError("Error updating product: ", e);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to update product in database.\"}");
            }
        }
    }
}
