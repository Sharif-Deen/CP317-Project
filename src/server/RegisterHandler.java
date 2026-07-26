package server;

import database.DatabaseInteract;
import features.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RegisterHandler implements HttpHandler {
    private static Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CORSUtil.addCORSHeaders(exchange);
        if (CORSUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("POST".equalsIgnoreCase(requestMethod)) {
            InputStream requestBody = exchange.getRequestBody();
            String requestString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            
            // Convert JSON to User object
            User newUser = gson.fromJson(requestString, User.class);

            boolean success = false;
            // Use your DatabaseInteract to add the user
            try (DatabaseInteract db = new DatabaseInteract()) {
                success = db.addUser(newUser);
            } catch (Exception e) {
                System.out.println("Database error during registration: " + e.getMessage());
            }

            JsonObject jsonResponse = new JsonObject();
            int statusCode;

            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Account created successfully");
                statusCode = 200;
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Registration failed. Username or email may already exist.");
                statusCode = 400; // Bad request
            }
           
            String jsonString = jsonResponse.toString();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, jsonString.getBytes(StandardCharsets.UTF_8).length);

            // Send response back to React
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}