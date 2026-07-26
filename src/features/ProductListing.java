package features;

import database.DatabaseInteract;
import java.util.Scanner;

public class ProductListing {

    private DatabaseInteract database;
    private Scanner scanner;

    public ProductListing(Scanner scanner) {
        this.database = new DatabaseInteract();
        this.scanner = scanner;
    }

    public void createProduct() {
        System.out.println();
        System.out.println("Create New Product Listing");
        System.out.println("-------------------------");

        System.out.print("Product name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Cancelling.");
            return;
        }

        System.out.print("Category (e.g. food, cookware): ");
        String type = scanner.nextLine().trim();

        System.out.print("Brand: ");
        String brand = scanner.nextLine().trim();

        System.out.print("Tags (comma separated, e.g. bulk,frozen): ");
        String tags = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Location (e.g. Waterloo): ");
        String location = scanner.nextLine().trim();

        System.out.print("Stock quantity: ");
        int stock;
        try {
            stock = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid stock quantity. Cancelling.");
            return;
        }

        Product newProduct = new Product(0, name, price, type, brand, tags, description, location, stock);
        boolean success = database.addProduct(newProduct) > 0;

        if (success) {
            System.out.println("Product \"" + name + "\" was added successfully.");
        } else {
            System.out.println("Failed to add product. Please try again.");
        }
    }

    public void editProduct() {
        System.out.println();
        System.out.println("Edit Existing Product Listing");
        System.out.println("-------------------------");

        System.out.print("Enter the name of the product you want to edit: ");
        String searchName = scanner.nextLine().trim();

        Product existing = database.findProductByName(searchName);

        if (existing == null) {
            System.out.println("No product found with that name.");
            return;
        }

        System.out.println("Found: " + existing);
        System.out.println("Press enter to keep the current value, or type a new one.");
        System.out.println();

        System.out.print("Name [" + existing.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = existing.getName();

        System.out.print("Price [" + existing.getPrice() + "]: ");
        String priceInput = scanner.nextLine().trim();
        double price = existing.getPrice();
        if (!priceInput.isEmpty()) {
            try {
                price = Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price, keeping original.");
            }
        }

        System.out.print("Category [" + existing.getType() + "]: ");
        String type = scanner.nextLine().trim();
        if (type.isEmpty()) type = existing.getType();

        System.out.print("Brand [" + existing.getBrand() + "]: ");
        String brand = scanner.nextLine().trim();
        if (brand.isEmpty()) brand = existing.getBrand();

        System.out.print("Tags [" + existing.getTags() + "]: ");
        String tags = scanner.nextLine().trim();
        if (tags.isEmpty()) tags = String.join(",", existing.getTags());

        System.out.print("Description [" + existing.getDescription() + "]: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) description = existing.getDescription();

        System.out.print("Location [" + existing.getLocation() + "]: ");
        String location = scanner.nextLine().trim();
        if (location.isEmpty()) location = existing.getLocation();

        System.out.print("Stock [" + existing.getStock() + "]: ");
        String stockInput = scanner.nextLine().trim();
        int stock = existing.getStock();
        if (!stockInput.isEmpty()) {
            try {
                stock = Integer.parseInt(stockInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid stock, keeping original.");
            }
        }

        Product updated = new Product(existing.getId(), name, price, type, brand, tags, description, location, stock);
        boolean success = database.updateProduct(updated);

        if (success) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println("Failed to update product. Please try again.");
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