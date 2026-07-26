package features;

import database.DatabaseInteract;
import java.util.List;
import java.util.Scanner;

public class CustomerOrders {

    private DatabaseInteract database;
    private Scanner scanner;

    public CustomerOrders(Scanner scanner) {
        this.database = new DatabaseInteract();
        this.scanner = scanner;
    }

    public void viewOrdersByEmail() {
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine().trim();

        List<Order> orders = database.findOrdersByEmail(email);

        if (orders.isEmpty()) {
            System.out.println("No orders found for: " + email);
            return;
        }

        System.out.println();
        System.out.println("Orders for " + email);
        System.out.println("-------------------------");

        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            System.out.println((i + 1) + ". Order #" + o.getOrderId());
            System.out.println("   Status: " + o.getOrderStatus());
            System.out.println("   Total: $" + o.getTotalPrice());
            System.out.println("   Order Date: " + o.getOrderDate());
            System.out.println("   Delivery Date: " + (o.getDeliveryDate() != null ? o.getDeliveryDate() : "Not set"));
            System.out.println();
        }
    }

    public void viewAllOrders() {
        List<Order> orders = database.findAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println();
        System.out.println("All Orders");
        System.out.println("-------------------------");

        for (Order o : orders) {
            System.out.println(o);
        }
        System.out.println();
    }

    public void updateOrderStatus() {
        System.out.print("Enter order number to update: ");
        int orderId;
        try {
            orderId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid order number.");
            return;
        }

        Order existing = database.findOrderByNumber(orderId);
        if (existing == null) {
            System.out.println("No order found with that number.");
            return;
        }

        System.out.println("Current status: " + existing.getOrderStatus());
        System.out.println("Available statuses: PROCESSING, SHIPPED, DELIVERED, CANCELLED, RETURNED");
        System.out.print("Enter new status: ");
        String input = scanner.nextLine().trim().toUpperCase();

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status. Please enter one of the options listed.");
            return;
        }

        boolean success = database.updateOrderStatus(orderId, newStatus);

        if (success) {
            System.out.println("Order #" + orderId + " status updated to " + newStatus + ".");
        } else {
            System.out.println("Failed to update order status.");
        }
    }

    public void close() {
        try {
            database.close();
        } catch (Exception e) {
            System.out.println("Could not close database connection.");
        }
    }
}