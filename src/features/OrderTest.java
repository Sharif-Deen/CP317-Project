package features;

import java.util.ArrayList;
import java.util.List;

public class OrderTest {
    public static void main(String[] args) {
        List<Order.OrderItem> items = new ArrayList<>();
        items.add(new Order.OrderItem(1, "Milk", 2, 3.50));
        items.add(new Order.OrderItem(2, "Bread", 1, 2.25));

        Order order = new Order(100, "user@example.com", "519-123-4567", "2026-07-26", "confirmed", null, items);

        if (order.getTotalPrice() != 9.25) {
            throw new AssertionError("Expected total price to be 9.25, got " + order.getTotalPrice());
        }

        if (order.getProducts().size() != 2) {
            throw new AssertionError("Expected 2 products in the order, got " + order.getProducts().size());
        }

        System.out.println("Order test passed");
    }
}
