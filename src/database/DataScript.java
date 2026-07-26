package database;

import features.Order;
import features.Product;
import features.User;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DataScript {
    private static final String PRODUCTS_FILE = "src/database/products.txt";
    private static final String USERS_FILE = "src/database/users.txt";
    private static final String ORDERS_FILE = "src/database/orders.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void main(String[] args) {
        try (DatabaseInteract db = new DatabaseInteract()) {
            List<Product> products = loadProducts(db);
            loadUsers(db);
            loadOrders(db, products);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static List<Product> loadProducts(DatabaseInteract db) throws IOException, SQLException {
        List<Product> insertedProducts = new ArrayList<>();
        Path productsPath = Path.of(PRODUCTS_FILE).toAbsolutePath().normalize();

        if (!Files.exists(productsPath)) {
            throw new IOException("Product file not found: " + productsPath);
        }

        List<String> lines = Files.readAllLines(productsPath);
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);
            if (parts.length < 8) {
                continue;
            }

            String productName = parts[0].trim();
            double price = Double.parseDouble(parts[1].trim());
            String category = parts[2].trim();
            String brand = parts[3].trim();
            String tags = parts[4].trim();
            String description = parts[5].trim();
            String location = parts[6].trim();
            int stock = Integer.parseInt(parts[7].trim());

            Product product = new Product(0, productName, price, category, brand, tags, description, location, stock);
            if (db.addProduct(product)>0) {
                insertedProducts.add(product);
            }
        }

        return insertedProducts;
    }

    private static void loadUsers(DatabaseInteract db) throws IOException, SQLException {
        Path productsPath = Path.of(PRODUCTS_FILE).toAbsolutePath().normalize();
        if (!Files.exists(productsPath)) {
            throw new IOException("Product file not found: " + productsPath);
        }

        Set<String> brands = new LinkedHashSet<>();
        List<String> lines = Files.readAllLines(productsPath);
        for (String line : lines) {
            if (line == null || line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\|", -1);
            if (parts.length >= 4) {
                String brand = parts[3].trim();
                if (!brand.isBlank()) {
                    brands.add(brand);
                }
            }
        }

        List<String> userLines = new ArrayList<>();
        for (String brand : brands) {
            String username = brand;
            String email = username + "@laurierfs.com";
            String password = "pass123";

            if (db.findUserByUsername(username) == null) {
                db.addUser(new User(0, username, password, "distributor", email));
            }

            userLines.add(email + "|" + username + "|" + password + "|distributor");
        }

        String[][] customers = {
                {"cust1", "pw1234"},
                {"cust2", "abc1234"},
                {"cust3", "mypass1"}
        };

        for (String[] customer : customers) {
            String username = customer[0];
            String password = customer[1];
            String email = username + "@laurierfs.com";

            if (db.findUserByUsername(username) == null) {
                db.addUser(new User(0, username, password, "customer", email));
            }

            userLines.add(email + "|" + username + "|" + password + "|customer");
        }

        Path usersPath = Path.of(USERS_FILE).toAbsolutePath().normalize();
        Files.createDirectories(usersPath.getParent());
        Files.write(usersPath, userLines);
    }

    private static void loadOrders(DatabaseInteract db, List<Product> products) throws IOException, SQLException {
        if (products == null || products.isEmpty()) {
            return;
        }

        List<String> orderLines = new ArrayList<>();
        String[][] customerOrders = {
                {"cust1@laurierfs.com", "cust1", "555-0101", "PROCESSING"},
                {"cust2@laurierfs.com", "cust2", "555-0102", "SHIPPED"},
                {"cust3@laurierfs.com", "cust3", "555-0103", "DELIVERED"}
        };

        for (int index = 0; index < customerOrders.length; index++) {
            String[] customerOrder = customerOrders[index];
            String email = customerOrder[0];
            String phone = customerOrder[2];
            String status = customerOrder[3];

            LocalDate orderDate = LocalDate.now().minusDays(3 - index);
            LocalDate deliveryDate = orderDate.plusDays(2 + index);

            List<Order.OrderItem> orderItems = new ArrayList<>();
            for (int itemIndex = 0; itemIndex < 3; itemIndex++) {
                Product product = products.get((index + itemIndex) % products.size());
                orderItems.add(new Order.OrderItem(0, product.getName(), 1, product.getPrice()));
            }

            Order order = new Order(0, email, phone, orderDate.format(DATE_FORMATTER), status, deliveryDate.format(DATE_FORMATTER), orderItems);
            int orderNumber = db.addOrder(order);
            if (orderNumber > 0) {
                StringBuilder productNames = new StringBuilder();
                for (int itemIndex = 0; itemIndex < orderItems.size(); itemIndex++) {
                    if (itemIndex > 0) {
                        productNames.append(",");
                    }
                    productNames.append(orderItems.get(itemIndex).getProductName());
                }

                orderLines.add(orderNumber + "|" + email + "|" + phone + "|" + String.format(Locale.US, "%.2f", order.getTotalPrice())
                        + "|" + orderDate.format(DATE_FORMATTER) + "|" + status + "|" + deliveryDate.format(DATE_FORMATTER)
                        + "|" + productNames);
            }
        }

        Path ordersPath = Path.of(ORDERS_FILE).toAbsolutePath().normalize();
        Files.createDirectories(ordersPath.getParent());
        Files.write(ordersPath, orderLines);
    }
}
