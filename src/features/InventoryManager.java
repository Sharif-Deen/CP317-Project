package features;

import database.DatabaseInteract;
import java.sql.SQLException;
import java.util.List;

public class InventoryManager {

    private DatabaseInteract database;

    public InventoryManager() {
        database = new DatabaseInteract();
    }

    public void viewInventory() {
        List<Product> products = database.findAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }

        System.out.println();
        System.out.println("Inventory List");
        System.out.println("-------------------------");

        for (Product product : products) {
            printShortProduct(product);
        }
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> products = database.findAllProducts();

        return products.stream()
                .filter(product ->
                        product.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                                product.getBrand().toLowerCase().contains(keyword.toLowerCase()) ||
                                product.getType().toLowerCase().contains(keyword.toLowerCase()) ||
                                product.getLocation().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public void printSearchResults(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No matching products found.");
            return;
        }

        System.out.println();
        System.out.println("Search Results");
        System.out.println("-------------------------");

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            System.out.println((i + 1) + ". " + product.getName());
            System.out.println("   Price: $" + product.getPrice());
            System.out.println("   Type: " + product.getType());
            System.out.println("   Brand: " + product.getBrand());
            System.out.println("   Location: " + product.getLocation());
            System.out.println("   Stock: " + product.getStock());
            System.out.println();
        }
    }

    public boolean updateStock(Product product, int newStock) {
        if (product == null) {
            System.out.println("Product does not exist.");
            return false;
        }

        if (newStock < 0) {
            System.out.println("Stock cannot be negative.");
            return false;
        }

        Product updatedProduct = new Product(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getType(),
                product.getBrand(),
                String.join(",", product.getTags()),
                product.getDescription(),
                product.getLocation(),
                newStock
        );

        boolean updated = database.updateProduct(updatedProduct);

        if (updated) {
            System.out.println(product.getName() + " stock was updated to " + newStock + ".");
        } else {
            System.out.println("Stock update failed.");
        }

        return updated;
    }

    public boolean markOutOfStock(Product product) {
        return updateStock(product, 0);
    }

    public void printShortProduct(Product product) {
        System.out.println("Product: " + product.getName());
        System.out.println("Price: $" + product.getPrice());
        System.out.println("Type: " + product.getType());
        System.out.println("Brand: " + product.getBrand());
        System.out.println("Location: " + product.getLocation());
        System.out.println("Stock: " + product.getStock());

        if (product.getStock() == 0) {
            System.out.println("Status: Out of stock");
        } else {
            System.out.println("Status: In stock");
        }

        System.out.println();
    }

    public void close() {
        try {
            database.close();
        } catch (SQLException e) {
            System.out.println("Could not close database connection.");
        }
    }
}