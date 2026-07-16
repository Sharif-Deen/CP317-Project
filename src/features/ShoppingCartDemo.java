package features;
import java.util.ArrayList;
import java.util.Scanner;

// Just a tester class for the shoppingcart, i made it give you a main menu with options to remove and add items and all that.
// The only thing to keep in mind is you cannot type a string in when in the main menu or the program will end
//

public class ShoppingCartDemo {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        ArrayList<Product> products = new ArrayList<>();

        products.add(new Product(
                1,
                "Bulk Roma Tomatoes",
                24.50,
                "food",
                "FreshFields",
                "vegetable,produce,bulk",
                "Ripe Roma tomatoes packed for restaurants and catering.",
                "Toronto",
                10
        ));

        products.add(new Product(
                2, 
                "Frozen Chicken Breast 10kg",
                79.99,
                "food",
                "ChefPro",
                "meat,frozen,bulk,protein",
                "Lean boneless chicken breasts.",
                "Waterloo",
                0
        ));

        products.add(new Product(
                3,
                "Organic White Rice 20kg",
                39.99,
                "food",
                "GrainHouse",
                "rice,bulk,grain",
                "Large bag of organic white rice for restaurants.",
                "Cambridge",
                8
        ));

        products.add(new Product(
                4,
                "Olive Oil 5L",
                32.75,
                "food",
                "PureHarvest",
                "oil,cooking,bulk",
                "Large bottle of olive oil for cooking.",
                "Waterloo",
                12
        ));

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("Shopping Cart Menu");
            System.out.println("-------------------------");
            System.out.println("1. View products");
            System.out.println("2. Add product to cart");
            System.out.println("3. Remove product from cart");
            System.out.println("4. Update item quantity");
            System.out.println("5. View cart");
            System.out.println("6. Clear cart");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                viewProducts(products);
            } else if (choice == 2) {
                viewProducts(products);

                System.out.print("Enter product number to add: ");
                int productNumber = scanner.nextInt();

                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();
                scanner.nextLine();

                if (productNumber >= 1 && productNumber <= products.size()) {
                    Product selectedProduct = products.get(productNumber - 1);
                    cart.addProduct(selectedProduct, quantity);
                } else {
                    System.out.println("Invalid product number.");
                }

            } else if (choice == 3) {
                System.out.print("Enter product name to remove: ");
                String productName = scanner.nextLine();

                cart.removeProduct(productName);

            } else if (choice == 4) {
                System.out.print("Enter product name to update: ");
                String productName = scanner.nextLine();

                System.out.print("Enter new quantity: ");
                int newQuantity = scanner.nextInt();
                scanner.nextLine();

                cart.updateQuantity(productName, newQuantity);

            } else if (choice == 5) {
                cart.viewCart();

            } else if (choice == 6) {
                cart.clearCart();

            } else if (choice == 7) {
                running = false;
                System.out.println("Exiting shopping cart demo.");

            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    public static void viewProducts(ArrayList<Product> products) {
        System.out.println();
        System.out.println("Available Products");
        System.out.println("-------------------------");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            System.out.println((i + 1) + ". " + product.getName());
            System.out.println("   Price: $" + product.getPrice());
            System.out.println();
        }
    }
}