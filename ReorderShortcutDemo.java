import java.util.Scanner;

public class ReorderShortcutDemo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();
        ReorderShortcut reorderShortcut = new ReorderShortcut();

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Reorder Shortcut Menu");
            System.out.println("-------------------------");
            System.out.println("1. View previous orders");
            System.out.println("2. Reorder a previous order");
            System.out.println("3. View shopping cart");
            System.out.println("4. Clear shopping cart");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                reorderShortcut.viewPreviousOrders();

            } else if (choice == 2) {
                reorderShortcut.viewPreviousOrders();

                System.out.print("Enter previous order number to reorder: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a number.");
                    scanner.nextLine();
                    continue;
                }

                int orderNumber = scanner.nextInt();
                scanner.nextLine();

                reorderShortcut.reorder(orderNumber, cart);

            } else if (choice == 3) {
                cart.viewCart();

            } else if (choice == 4) {
                cart.clearCart();

            } else if (choice == 5) {
                running = false;
                System.out.println("Exiting reorder shortcut demo.");

            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}