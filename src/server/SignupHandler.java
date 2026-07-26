package server;
import features.User;
import features.Hash;
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

public class SignupHandler implements HttpHandler {
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
            User signupAttempt = gson.fromJson(requestString, User.class);

            String username = signupAttempt.getUsername();
            String email = signupAttempt.getEmail();
            String password = signupAttempt.getPassword();
            String category = signupAttempt.getCategory();

            try(DatabaseInteract db = new DatabaseInteract()){
                //Test if email/username already taken
                List<Map<String, Object>> queryResults = db.runCustomQuery("SELECT email, username FROM users WHERE email = ? OR username = ?", email, username);
                boolean emailSuccess = true;
                boolean userSuccess = true;
                for(int i=0;i<queryResults.size();i++){
                    Map<String, Object>  row = queryResults.get(i);
                    if(row.get("email").equals(email)) { emailSuccess=false; break; }
                    if(row.get("username").equals(username)) { userSuccess=false; break; }
                }

                //test if email already taken 
                if(!emailSuccess){
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_DATABASE_CONFLICT, "{\"status\": \"error\", \"message\": \"Email address is already in use.\"}");
                    return;
                }
                //test if username already taken
                if(!userSuccess){
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_DATABASE_CONFLICT, "{\"status\": \"error\", \"message\": \"Username is already taken.\"}");
                    return;
                }
                

                //insert into USERS table and return id
                int id = db.runCustomUpdate("INSERT INTO users (email, username, userPassword, phone, userType) VALUES (?, ?, ?, ?, ?)", email, username, Hash.hashPassword(password), "", category);
                
                if(id>-1){
                    //respond with new id, username, email, role/category
                    JsonObject jsonResponse = new JsonObject();
                    jsonResponse.addProperty("id", id);
                    jsonResponse.addProperty("username", username);
                    jsonResponse.addProperty("email", email);
                    jsonResponse.addProperty("category", category);
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
                }else{
                    ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"An unexpected error occurred. Please try again later.\"}");
                }
            }catch (SQLException e){
                Server.printError("Error connecting to database: ", e);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Failed to connect to database.\"}");
            }catch (Exception e){
                Server.printError("Server Error: ", e);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"Server Error: " + e.getMessage() + "\"}");
            }

        }
    }
}