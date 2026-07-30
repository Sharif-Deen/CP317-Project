package server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

public class Server {

    public static void main(String[] args) throws IOException { 
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Your route handlers
        server.createContext("/api/products", new ProductHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/signup", new SignupHandler());
        server.createContext("/api/orders", new OrderHandler());
        server.createContext("/api/analytics", new AnalyticsHandler());
        
        server.setExecutor(null);
        server.start();
        System.out.println("Server running on port 8080");
        
    }

    public static void printError(String context, Exception e){
        System.err.println(context + e.getMessage());
        e.printStackTrace();
    }
}