package features;

import java.util.Scanner;

public class CustomerOrdersDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerOrders co = new CustomerOrders(scanner);

        System.out.println("Laurier Food Services - Customer Orders");

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. View all orders");
            System.out.println("2. View orders by customer email");
            System.out.println("3. Update order status");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                co.viewAllOrders();
            } else if (input.equals("2")) {
                co.viewOrdersByEmail();
            } else if (input.equals("3")) {
                co.updateOrderStatus();
            } else if (input.equals("4")) {
                running = false;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }

        co.close();
        scanner.close();
    }
}