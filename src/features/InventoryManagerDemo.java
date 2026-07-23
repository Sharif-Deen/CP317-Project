package features;

import java.util.List;
import java.util.Scanner;

public class InventoryManagerDemo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        InventoryManager inventoryManager = new InventoryManager();

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Inventory Management Menu");
            System.out.println("-------------------------");
            System.out.println("1. View inventory");
            System.out.println("2. Search product");
            System.out.println("3. Update product stock");
            System.out.println("4. Mark product out of stock");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt(scanner);

            if (choice == 1) {
                inventoryManager.viewInventory();

            } else if (choice == 2) {
                Product selectedProduct = selectProduct(scanner, inventoryManager);

                if (selectedProduct != null) {
                    System.out.println();
                    System.out.println("Selected Product");
                    System.out.println("-------------------------");
                    inventoryManager.printShortProduct(selectedProduct);
                }

            } else if (choice == 3) {
                Product selectedProduct = selectProduct(scanner, inventoryManager);

                if (selectedProduct != null) {
                    System.out.print("Enter new stock amount: ");
                    int newStock = readInt(scanner);

                    inventoryManager.updateStock(selectedProduct, newStock);
                }

            } else if (choice == 4) {
                Product selectedProduct = selectProduct(scanner, inventoryManager);

                if (selectedProduct != null) {
                    inventoryManager.markOutOfStock(selectedProduct);
                }

            } else if (choice == 5) {
                running = false;
                System.out.println("Exiting inventory manager demo.");

            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        inventoryManager.close();
        scanner.close();
    }

    private static Product selectProduct(Scanner scanner, InventoryManager inventoryManager) {
        System.out.print("Enter product keyword: ");
        String keyword = scanner.nextLine().trim();

        if (keyword.isEmpty()) {
            System.out.println("Please enter a product keyword.");
            return null;
        }

        List<Product> results = inventoryManager.searchProducts(keyword);

        if (results.isEmpty()) {
            System.out.println("No products found for: " + keyword);
            return null;
        }

        inventoryManager.printSearchResults(results);

        System.out.print("Choose a product number: ");
        int productNumber = readInt(scanner);

        if (productNumber < 1 || productNumber > results.size()) {
            System.out.println("Invalid product number.");
            return null;
        }

        return results.get(productNumber - 1);
    }

    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number.");
            scanner.nextLine();
            System.out.print("Choose an option: ");
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        return number;
    }
}