package server;
import database.DatabaseInteract;
import features.Product;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductHandler implements HttpHandler {
    private static Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        CORSUtil.addCORSHeaders(exchange);
        if (CORSUtil.handlePreflight(exchange)) return;
        
        String requestMethod = exchange.getRequestMethod();
        
        if ("GET".equalsIgnoreCase(requestMethod)) {
            //get all products from database
            List<Product> products = new ArrayList<>();
            try (DatabaseInteract db = new DatabaseInteract()) {
                products = db.findAllProducts();
            } catch (Exception e){
                System.err.println(e);
            }
            //convert products to json objects array
            String jsonResponse = gson.toJson(products);
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            // write to React
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }

}