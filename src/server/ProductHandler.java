package server;
import database.DatabaseInteract;
import features.Product;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ProductHandler implements HttpHandler {
    private static Gson gson = new Gson();

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
        //TODO: handle POST request (add product to db), DELETE request (delete product from db)
        else if ("POST".equalsIgnoreCase(requestMethod)){

        } else if ("DELETE".equalsIgnoreCase(requestMethod)){
            
        }

    }

}