package server;
import database.DatabaseInteract;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class OrderHandler implements HttpHandler {
    private static Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CORSUtil.addCORSHeaders(exchange);
        if (CORSUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("GET".equalsIgnoreCase(requestMethod)) {
            //TODO: Create Order class. Handle different GET requests (get by user, brand). Respond with data or error message
            
            // ArrayList<Order> orders = new ArrayList<Order>();

            //convert orders to json objects array

        }
        else if ("POST".equalsIgnoreCase(requestMethod)){
            // handle adding an order to the database

            // return a success message to the frontend or error message if inserting into database failed

        }
    }

}