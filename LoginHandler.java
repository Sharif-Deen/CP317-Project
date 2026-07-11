import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class LoginHandler implements HttpHandler {
    public static Gson gson = new Gson();
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CORSUtil.addCORSHeaders(exchange);
        if (CORSUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("POST".equalsIgnoreCase(requestMethod)) {
            InputStream requestBody = exchange.getRequestBody();
            byte[] bytes = requestBody.readAllBytes();
            String requestString = new String(bytes, StandardCharsets.UTF_8);
            User loginAttempt = gson.fromJson(requestString, User.class);

            String username = loginAttempt.getUsername();
            String password = loginAttempt.getPassword();
            String category = loginAttempt.getCategory();
            String email = loginAttempt.getEmail();
            
            //call password manager function to see if loginAttempt is valid. return boolean
            PasswordManager pm = new PasswordManager();
            boolean success = pm.userVerification(username, password, category, email);

            JsonObject jsonResponse = new JsonObject();
            int statusCode;

            if(success){
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Authentication successful");
                jsonResponse.addProperty("username", username);
                jsonResponse.addProperty("category", category);
                jsonResponse.addProperty("email", email);
                statusCode = 200;
            }else{
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Invalid username or password");
                statusCode = 401;
            }
           
            String jsonString = jsonResponse.toString();

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, jsonString.getBytes().length);

            // write to React
            OutputStream os = exchange.getResponseBody();
            os.write(jsonString.getBytes());
            os.close();
        }
    }
}