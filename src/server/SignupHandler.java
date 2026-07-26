package server;
import features.User;
import features.PasswordManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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


            //test if username already taken
            boolean userSuccess = false;
            if(!userSuccess){
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_DATABASE_CONFLICT, "{\"status\": \"error\", \"message\": \"Username is already taken.\"}");
                return;
            }
            //test if email already taken 
            boolean emailSuccess = false;
            if(!emailSuccess){
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_DATABASE_CONFLICT, "{\"status\": \"error\", \"message\": \"Email address is already in use.\"}");
                return;
            }
            
            //insert into USERS table and return id
            int id = 1;

            if(id>0){
                //respond with new id, username, email, role/category
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("id", id);
                jsonResponse.addProperty("username", username);
                jsonResponse.addProperty("email", email);
                jsonResponse.addProperty("role", category);
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_OK, jsonResponse.toString());
            }else{
                ServerUtil.sendJson(exchange, ServerUtil.STATUS_SERVER_ERR, "{\"status\": \"error\", \"message\": \"An unexpected error occurred. Please try again later.\"}");
            }
            
        }
    }
}