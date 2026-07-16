package server;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        server.createContext("/api/products", new ProductHandler());
        server.createContext("/api/login", new LoginHandler());
        // server.createContext("/api/signup", new SignUpHandler());
        // server.createContext("/api/orders", new OrdersHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server running on port 8080");
    }
}