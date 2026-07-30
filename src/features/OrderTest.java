package features;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OrderTest {
    public static void main(String[] args) {


        Order order = new Order(100, "user@example.com", "519-123-4567", "2026-07-26", "confirmed", null, null);
        order.addItem(new Product(1, "Milk", 3.50, "FreshFields"), 2);
        order.addItem(new Product(2,"Bread", 2.25, "FreshFields"), 1);

        if (order.getTotalPrice() != 9.25) {
            throw new AssertionError("Expected total price to be 9.25, got " + order.getTotalPrice());
        }

        if (order.getProducts().size() != 2) {
            throw new AssertionError("Expected 2 products in the order, got " + order.getProducts().size());
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(order, Order.class));
        System.out.println("Order test passed");
    }
}
