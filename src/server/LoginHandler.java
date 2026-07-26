package server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.DatabaseInteract;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoginHandler implements HttpHandler {
    public static Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        ServerUtil.addCORSHeaders(exchange);
        if (ServerUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("POST".equalsIgnoreCase(requestMethod)) {
            InputStream requestBody = exchange.getRequestBody();
            byte[] bytes = requestBody.readAllBytes();
            String requestString = new String(bytes, StandardCharsets.UTF_8);
            JsonObject loginAttempt = gson.fromJson(requestString, JsonObject.class);

            String identifier = loginAttempt.get("identifier").getAsString();
            String password = loginAttempt.get("password").getAsString();
            
            //test if valid credentials, return: id, username, email, role
            String sqlQuery;
            if(identifier.contains("@")){
                sqlQuery = "SELECT accountNumber, username, email, userType FROM users WHERE email = ? AND password = ?";
            }else{
                sqlQuery = "SELECT accountNumber, username, email, userType FROM users WHERE username = ? AND password = ?";
            }

            try(DatabaseInteract db = new DatabaseInteract()){
                List<Map<String, Object>> queryResults = db.runCustomQuery(sqlQuery, identifier, password);
                if (queryResults.size() == 1){
                    Map<String, Object> values = queryResults.get(0);
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("id", (int) values.get("accountNumber"));
                    jsonResponse.addProperty("username", (String) values.get("username"));
                    jsonResponse.addProperty("email", (String) values.get("email"));
                    jsonResponse.addProperty("role", (String) values.get("userType"));
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());

                } else {
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_UNAUTHORIZED, "{\"status\": \"error\", \"message\": \"Invalid Email/Username or Password.\"}");
                }
            }catch(SQLException e){
                System.err.println("Error connecting to database: " + e.getMessage());
                e.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to connect to database.\"}");
            }catch(Exception e){
                System.err.println("Error occured while authenticating login: " + e.getMessage());
                e.printStackTrace();
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Server Error: Failed to authenticate login.\"}");
            }
           
        }
    }
}