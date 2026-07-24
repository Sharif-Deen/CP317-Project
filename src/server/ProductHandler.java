package server;
import database.DatabaseInteract;
import features.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductHandler implements HttpHandler {
    private static Gson gson = new Gson();

    private static void sendJson(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes(StandardCharsets.UTF_8).length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CORSUtil.addCORSHeaders(exchange);
        if (CORSUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("GET".equalsIgnoreCase(requestMethod)) {
            //get and send all products from database
            try (DatabaseInteract db = new DatabaseInteract()) {
                List<Product> products = db.findAllProducts();

                //convert products to json objects array
                String jsonResponse = gson.toJson(products);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(CORSUtil.STATUS_OK, jsonResponse.getBytes().length);

                try(OutputStream os = exchange.getResponseBody()){
                    os.write(jsonResponse.getBytes());
                }

            } catch (Exception e){ //return error message
                System.err.println("Error fetching products: " + e.getMessage());
                e.printStackTrace();
                String jsonResponse = "{\"error\": \"Failed to retrieve products from the database.\"}";

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(CORSUtil.STATUS_SERVER_ERR, jsonResponse.getBytes().length);
            }
            
        }
        else if ("POST".equalsIgnoreCase(requestMethod)){
            //add product to db
            try (DatabaseInteract db = new DatabaseInteract()) {
                InputStream requestBody = exchange.getRequestBody();
                String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                Product newProduct = gson.fromJson(requestString, Product.class);

                boolean success = db.addProduct(newProduct);

                JsonObject jsonResponse = new JsonObject();
                if (success) {
                    jsonResponse.addProperty("status", "success");
                    jsonResponse.addProperty("message", "Product added successfully");
                    sendJson(exchange, CORSUtil.STATUS_OK, jsonResponse.toString());
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Failed to add product");
                    sendJson(exchange, CORSUtil.STATUS_SERVER_ERR, jsonResponse.toString());
                }
            } catch (Exception e) {
                System.err.println("Error adding product: " + e.getMessage());
                e.printStackTrace();
                sendJson(exchange, CORSUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to add product to the database.\"}");
            }

        } else if ("DELETE".equalsIgnoreCase(requestMethod)){
            //delete product from db
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
                    sendJson(exchange, CORSUtil.STATUS_OK, jsonResponse.toString());
                } else {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Product not found");
                    sendJson(exchange, 404, jsonResponse.toString());
                }
            } catch (Exception e) {
                System.err.println("Error deleting product: " + e.getMessage());
                e.printStackTrace();
                sendJson(exchange, CORSUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to remove product from the database.\"}");
            }
        }

    }

}