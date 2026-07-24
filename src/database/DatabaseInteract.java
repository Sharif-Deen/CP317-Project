package database;

//  =================
//   IMPORTS:
//  =================

import features.Product;        // For Product objects.
import features.User;           // For User objects.
import features.Order;          // For Order objects.
import features.OrderStatus;    // For OrderStatus enum.
import java.nio.file.Path;          // For file paths.
import java.nio.file.Paths;         // For file Path Objects.
import java.sql.Connection;         // For active links to the database.
import java.sql.DriverManager;      // For opening database connections.
import java.sql.PreparedStatement;  // For SQL statements with ? placeholders.
import java.sql.ResultSet;          // For rows returned from SELECT.
import java.sql.ResultSetMetaData;  // For resultSet metadata.
import java.sql.SQLException;       // For error handling.
import java.sql.Statement;          // For the RETURN_GENERATED_KEYS flag.
import java.util.LinkedHashMap;     // For column order when reading rows.
import java.util.Map;               // For key/value pairs.
import java.util.ArrayList;
import java.util.List;

//  =================
//  DATABASE INTERACT:
//  =================

public class DatabaseInteract implements AutoCloseable {

    // SQLite database file that is used.
    private static final String DATABASE_FILE = "src/database/laurierFS.db";
    // Live JDBC connection shared by every method.
    private final Connection connection;
    // Database path.
    private final Path databasePath;

    // Constructor for the DatabaseInteract class.
    public DatabaseInteract() {
        this.databasePath = Paths.get(DATABASE_FILE).toAbsolutePath().normalize();

        // Try to open connection to database file.
        try {
            Class.forName("org.sqlite.JDBC");
            // Build JDBC URL and open connection.
            String connectionUrl = "jdbc:sqlite:" + this.databasePath;
            this.connection = DriverManager.getConnection(connectionUrl);
            // Each statement commits immediately.
            this.connection.setAutoCommit(true);
        
        // If SQLite JDBC driver not found, throw exception.
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("SQLite JDBC driver not found. Add lib/sqlite-jdbc-*.jar to the classpath.", exception);
        
        // If connection fails, throw exception.
        } catch (SQLException sqlException) {
            throw new IllegalStateException(
                    "Unable to connect to database: " + sqlException.getMessage(),sqlException);
        }
    return;
    }


    // Returns path of database file.
    public Path getDatabasePath() {
        return databasePath;
    }


   // Returns true when JDBC connection is open.
    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }


    // Closing the connection when the object is no longer needed.
    // parameters: none
    // returns: none
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        return;
    }


    // =================
    //  HELPER METHODS:
    // =================

    // Inserts parameters into PreparedStatement.
    // Parameters: preparedStatement - PreparedStatement to fill.
    // Parameters: parameters - Parameters to insert into statement.
    private void setQueryParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        // Go through each parameter and insert it into PreparedStatement.
        for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
            
            preparedStatement.setObject(parameterIndex + 1, parameters[parameterIndex]);
        }
        return;
    }


    // Builds a Product object from a database row, including tags and stock.
    // Parameters: productNumber - Product number to look up tags and stock.
    // Parameters: resultRow - Map of column names to values for the product row.
    // Returns: Product object built from row and related data.
    private Product buildProductFromRow(int productNumber, Map<String, Object> resultRow) throws SQLException {
        // Product object to return.
        Product product = null;

        // Load tags, location, and stock for the product.
        String commaSeparatedTags = loadTagsAsCommaSeparatedString(productNumber);
        String productLocation = loadProductLocation(productNumber);
        int stockQuantity = loadProductStock(productNumber);
        
        // Build Product object from row and related data.
        product = new Product((int) resultRow.get("productNumber") ,(String) resultRow.get("productName"), ((Number) resultRow.get("price")).doubleValue(), (String) resultRow.get("productCategory"), (String) resultRow.get("productBrand"), commaSeparatedTags, (String) resultRow.get("productShortDescription"), productLocation, stockQuantity);
        
        return product;
    }


    // Writes stock information for one product when location is provided.
    // Parameters: productNumber - Product number to link stock to.
    // Parameters: product - Product object containing location and stock.
    // Returns: none
    private void saveProductStock(int productNumber, Product product) throws SQLException {
        // If location is not null or blank, find or create location row and link to product.
        if (!(product.getLocation() == null || product.getLocation().isBlank())) {
            // Find or create location row and get locationNumber.
            int locationNumber = findOrCreateLocation(product.getLocation());

            // Insert stock row into productStock table.
            runCustomUpdate("INSERT OR IGNORE INTO productStock " + "(productNumber, locationNumber, locationName, stockQuantity) " + "VALUES (?, ?, ?, ?)", productNumber, locationNumber, product.getLocation(), product.getStock());
        }
        return;
    }


    // Writes tag information for one product when tags are provided.
    // Parameters: productNumber - Product number to link tags to.
    // Parameters: tagNameList - List of tag names to link to product.
    // Returns: none
    private void saveProductTags(int productNumber, List<String> tagNameList) throws SQLException {
        // For each tag name, find or create tag row, then link to product.
        for (String tagName : tagNameList) {
            
            // If tag name is not null or blank, find or create tag row and link to product.
            if (!(tagName == null || tagName.isBlank())) {
                // Find or create tag row and get tagNumber.
                int tagNumber = findOrCreateTag(tagName.trim());
                
                runCustomUpdate("INSERT OR IGNORE INTO productTag (productNumber, tagNumber) VALUES (?, ?)", productNumber, tagNumber);
            }
        }
        return;
    }


    // Loads all tag names for product as comma-separated string.
    // Parameters: productNumber - Product number to look up.
    // Returns: Comma-separated string of tag names, empty string when none found.
    private String loadTagsAsCommaSeparatedString(int productNumber) throws SQLException {
        // String to return.
        String commaSeparatedTags = "";
        
        // Query database for tag names for product.
        List<Map<String, Object>> queryResults = runCustomQuery("SELECT tag.tagName FROM productTag productTagLink " + "JOIN tag ON productTagLink.tagNumber = tag.tagNumber " + "WHERE productTagLink.productNumber = ? ORDER BY tag.tagName", productNumber);

        // Build list of tag names from query results.
        List<String> tagNameList = new ArrayList<>();
        
        // For each row in query results, get tag name and add to list.
        for (Map<String, Object> resultRow : queryResults) {
            tagNameList.add((String) resultRow.get("tagName"));
        }

        // Join tag names into comma-separated string.
        commaSeparatedTags = String.join(",", tagNameList);

        return commaSeparatedTags;
    }


    // Loads location name for a product.
    // Parameters: productNumber - Product number to look up.
    // Returns: Location name when found, empty string when not found.
    private String loadProductLocation(int productNumber) throws SQLException {
        // Location name to return.
        String locationName = "";

        // Query database for location name for product.
        List<Map<String, Object>> queryResults = runCustomQuery("SELECT locationName FROM productStock WHERE productNumber = ? LIMIT 1", productNumber);

        // If product row found, get location name.
        if (!queryResults.isEmpty()) {
            locationName = (String) queryResults.get(0).get("locationName");
        }
        return locationName;
    }


    // Finds stock quantity for a product.
    // Parameters: productNumber - Product number to look up.
    // Returns: Stock quantity when found, 0 when not found.
    private int loadProductStock(int productNumber) throws SQLException {
        // Stock quantity to return.
        int stockQuantity = 0;

        // Query database for stock quantity for product.
        List<Map<String, Object>> queryResults = runCustomQuery("SELECT stockQuantity FROM productStock WHERE productNumber = ? LIMIT 1", productNumber);

        // If product row found, get stock quantity.
        if (!queryResults.isEmpty()) {
            stockQuantity = ((Number) queryResults.get(0).get("stockQuantity")).intValue();
        }
        return stockQuantity;
    }


    // Finds a location row by name.
    // Parameters: locationName - Name of location to find or create.
    // Returns: locationNumber when found or created.
    private int findOrCreateLocation(String locationName) throws SQLException {
        // Location number to return.
        int locationNumber = -1;

        // Query database for location row matching name.
        List<Map<String, Object>> queryResults = runCustomQuery("SELECT locationNumber FROM locations WHERE city = ? LIMIT 1", locationName);
        
        // If location row found, get locationNumber.
        if (!queryResults.isEmpty()) {
            locationNumber = ((Number) queryResults.get(0).get("locationNumber")).intValue();
        
        // If location row was not found, create new locationNumber.
        } else {
            locationNumber = runCustomUpdate("INSERT INTO locations (city, country, province) VALUES (?, ?, ?)", locationName, "Canada", "");
        }
        return locationNumber;
    }


    // Finds a tag row by name.
    // Parameters: tagName - Name of tag to find or create.
    // Returns: tagNumber when found or created.
    private int findOrCreateTag(String tagName) throws SQLException {
        // Tag number to return.
        int tagNumber = -1;

        // Query database for tag row matching name.
        List<Map<String, Object>> queryResults = runCustomQuery("SELECT tagNumber FROM tag WHERE tagName = ? LIMIT 1", tagName);

        // If tag row found, get tagNumber.
        if (!queryResults.isEmpty()) {
            tagNumber = ((Number) queryResults.get(0).get("tagNumber")).intValue();
        
        // If tag row not found, create new tagNumber.
        } else {
            tagNumber = runCustomUpdate("INSERT INTO tag (tagName, tagDescription) VALUES (?, ?)", tagName, "");
        }
        return tagNumber;
    }


    // Finds a product row by name.
    // Parameters: productName - Name of product to find.
    // Returns: productNumber when found, null when not found.
    private Integer findProductNumberByName(String productName) throws SQLException {
        // Product number to return, null if not found.
        Integer productNumber = null;

        // Query database for product row matching name.
        List<Map<String, Object>> queryResults = runCustomQuery("SELECT productNumber FROM product WHERE productName = ? LIMIT 1", productName);

        // If product row found, get productNumber.
        if (!queryResults.isEmpty()) {
            productNumber = ((Number) queryResults.get(0).get("productNumber")).intValue();
        }
        return productNumber;
    }


    // =================
    //  GENERAL METHODS:
    // =================


    // Runs custom SELECT query that returns rows.
    // Example: database.runCustomQuery("SELECT * FROM product WHERE price < ?", 25.0);
    // Parameters: sqlQuery - SQL query to run.
    // Parameters: parameters - Optional parameters to pass to query.
    // Returns: List of maps, one for each row. Keys = column names, values = cell data.
    public List<Map<String, Object>> runCustomQuery(String sqlQuery, Object... parameters) throws SQLException {
        // List to store query results.
        List<Map<String, Object>> queryResults = new ArrayList<>();

        // PreparedStatement inserts parameters correctly into query.
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            // Insert parameters into query.
            setQueryParameters(preparedStatement, parameters);

            // Execute query and get result set.
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Get metadata from result set.
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                // Get number of columns in result set.
                int columnCount = resultSetMetaData.getColumnCount();

                // Walk through every row the database returned.
                while (resultSet.next()) {
                    Map<String, Object> resultRow = new LinkedHashMap<>();

                    // Copy each column in this row into the map.
                    for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                        String columnName = resultSetMetaData.getColumnName(columnIndex);
                        Object columnValue = resultSet.getObject(columnIndex);
                        resultRow.put(columnName, columnValue);
                    }
                    // Add the row to the list of results.
                    queryResults.add(resultRow);
                }
            }
        }
        return queryResults;
    }


    // Runs custom INSERT, UPDATE, or DELETE statement.
    // Example: database.runCustomUpdate("UPDATE product SET price = ? WHERE productName = ?", 9.99, "Milk");
    // Parameters: sqlQuery - SQL query to run.
    // Parameters: parameters - Optional parameters to pass to query.
    // Returns: For INSERT: new auto-generated key when available.
    //          For UPDATE/DELETE: number of rows changed.
    public int runCustomUpdate(String sqlQuery, Object... parameters) throws SQLException {
        // Number of rows affected by the query.
        int affectedRowCount;

        // PreparedStatement inserts parameters correctly into query.
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            // Insert parameters into query.
            setQueryParameters(preparedStatement, parameters);

            // Execute and get number of affected rows.
            affectedRowCount = preparedStatement.executeUpdate();

            // If rows were affected.
            if (affectedRowCount != 0) {
                
            // If SQLite generated new primary key, return ID.
            try (ResultSet generatedKeysResultSet = preparedStatement.getGeneratedKeys()) {
                
                // If a new key was generated, return it.
                if (generatedKeysResultSet.next()) {
                    affectedRowCount = generatedKeysResultSet.getInt(1);
                    }
                }
            }
        }
        return affectedRowCount;
    }


    // =================
    //  PRODUCT METHODS:
    // =================


    // Inserts new product row, along with tags and stock.
    // Parameters: product - Product object to insert into database.
    // Returns true when successful, false when failed.
    public boolean addProduct(Product product) {
        // Flag to indicate if product was added successfully.
        boolean success = false;
        
        // Insert product row into database.
        try {String insertProductQuery = "INSERT INTO product " + "(productName, price, productCategory, productBrand, productShortDescription) " + "VALUES (?, ?, ?, ?, ?)";

            // Get new productNumber for inserted product.
            int productNumber = runCustomUpdate(insertProductQuery, product.getName(), product.getPrice(), product.getType(), product.getBrand(), product.getDescription());

            // If product inserted successfully, save stock and tags.
            if (productNumber > 0) {
                saveProductStock(productNumber, product);
                saveProductTags(productNumber, product.getTags());

                success = true;
            }
        
        // If product insertion failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to add product: " + sqlException.getMessage());
        }
        return success;
    }


    // Looks up one product by name, or returns null when not found.
    // Parameters: productName - Name of product to find.
    // Returns: Product object when found, null when not found.
    public Product findProductByName(String productName) {
        // Product object to return, null if not found.
        Product foundProduct = null;

        // Query database for product row matching name.
        try {List<Map<String, Object>> queryResults = runCustomQuery("SELECT productNumber, productName, price, productCategory, " + "productBrand, productShortDescription " + "FROM product WHERE productName = ? LIMIT 1", productName);

            // If product row was found, build Product object from it.
            if (!queryResults.isEmpty()) {
            
            // Get the first row from the query results.
            Map<String, Object> resultRow = queryResults.get(0);
            int productNumber = ((Number) resultRow.get("productNumber")).intValue();
            
            // Build Product object from row and assign to foundProduct.
            foundProduct = buildProductFromRow(productNumber, resultRow);
            }
        
        // If product lookup failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to find product: " + sqlException.getMessage());
        }
        return foundProduct;
    }


    // Finds list of all products in database, by name.
    // Parameters: none
    // Returns: List of Product objects, empty list if none found.
    public List<Product> findAllProducts() {
        // List to store all products found in database.
        List<Product> productList = new ArrayList<>();

        // Query database for all product rows, ordered by name.
        try {List<Map<String, Object>> queryResults = runCustomQuery("SELECT productNumber, productName, price, productCategory, " + "productBrand, productShortDescription " + "FROM product ORDER BY productName");

            // For each row, build Product object and add to list.
            for (Map<String, Object> resultRow : queryResults) {
                int productNumber = ((Number) resultRow.get("productNumber")).intValue();
                
                productList.add(buildProductFromRow(productNumber, resultRow));
            }

        // If product lookup failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to load products: " + sqlException.getMessage());
            productList = new ArrayList<>();
        }
        return productList;
    }


    // Updates existing product in database, along with tags and stock.
    // Parameters: product - Product object with updated values.
    // Returns: true when successful, false when failed.
    public boolean updateProduct(Product product) {
        // Flag to indicate if product was updated successfully.
        boolean success = false;
        
        // Update product row in database.
        try {int updatedRowCount = runCustomUpdate("UPDATE product SET price = ?, productCategory = ?, " + "productBrand = ?, productShortDescription = ? " + "WHERE productName = ?", product.getPrice(), product.getType(), product.getBrand(), product.getDescription(), product.getName());
            // If product updated successfully, update stock and tags.
            if (updatedRowCount > 0) {
            
                // Get productNumber for updated product.
                Integer productNumber = findProductNumberByName(product.getName());
                
                // If productNumber found, update stock and tags.
                if (productNumber != null) {

                    // Remove old links, then write the fresh tag/stock data.
                    runCustomUpdate("DELETE FROM productTag WHERE productNumber = ?", productNumber);
                    runCustomUpdate("DELETE FROM productStock WHERE productNumber = ?", productNumber);

                    saveProductStock(productNumber, product);
                    saveProductTags(productNumber, product.getTags());
                }
            success = true;
            }
        
        // If product update failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to update product: " + sqlException.getMessage());
        }
        return success;
    }


    // Removes product from database by name, along with tags and stock.
    // Parameters: productName - Name of product to remove.
    // Returns: true when successful, false when failed.
    public boolean removeProductByName(String productName) {
        // Flag to indicate if product was removed successfully.
        boolean success = false;
        
        // Find productNumber for product to remove, then delete all related rows.
        try {Integer productNumber = findProductNumberByName(productName);
            // If productNumber found, delete product row and related tags/stock.
            if (productNumber != null) {
                
                // Delete product row, along with tags and stock.
                runCustomUpdate("DELETE FROM productTag WHERE productNumber = ?", productNumber);
                runCustomUpdate("DELETE FROM productStock WHERE productNumber = ?", productNumber);
                runCustomUpdate("DELETE FROM product WHERE productNumber = ?", productNumber);
                
                success = true;
          }
        // If product removal failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to remove product: " + sqlException.getMessage());
        }
        return success;
    }


    // =================
    //   USER METHODS:
    // =================

    // Inserts new user row into database.
    // Parameters: user - User object to insert into database.
    // Returns: true when successful, false when failed.
    public boolean addUser(User user) {
        // flag to indicate if user was added successfully.
        boolean success = false;

        // Insert user row into database.
        try {String insertUserQuery = "INSERT INTO users " + "(email, username, userPassword, phone, userType) " + "VALUES (?, ?, ?, ?, ?)";

            // Get new account number for inserted user.
            // Phone value does not exist in user object but I have it in database schema.
            int insertedAccountNumber = runCustomUpdate(insertUserQuery, user.getEmail(), user.getUsername(), user.getPassword(), "", user.getCategory());

            success = insertedAccountNumber > 0;

        // If user insertion failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to add user: " + sqlException.getMessage());
        }
        return success;
    }


    // Finds one user by username.
    // Parameters: username - Username of user to find.
    // Returns: User object when found, null when not found.
    public User findUserByUsername(String username) {
        // User object to return, null if not found.
        User user = null;
        
        // Query database fo matching user row.
        try {List<Map<String, Object>> queryResults = runCustomQuery("SELECT username, userPassword, userType, email " + "FROM users WHERE username = ? LIMIT 1", username);

            // If user row found, build User object.
            if (!queryResults.isEmpty()) {            
                // Get the first row from the query results.
                Map<String, Object> resultRow = queryResults.get(0);
                
                // Get userType from row, default to "customer" if null.
                String userType = resultRow.get("userType") == null ? "customer" : String.valueOf(resultRow.get("userType"));
                
                // Build User object from row and assign to user.
                user = new User((int) resultRow.get("accountNumber"), (String) resultRow.get("username"), (String) resultRow.get("userPassword"), userType, (String) resultRow.get("email"));
            }

        // If user lookup failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to find user: " + sqlException.getMessage());
        }
        return user;
    }


    // =================
    //   ORDER METHODS:
    // =================

    // Builds Order object from database row.
    // Parameters: resultRow - Map of column names to values for order row.
    // Returns: Order object built from the row.
    private Order buildOrderFromRow(Map<String, Object> resultRow) throws SQLException {
        
        // Order object to return.
        Order order = null;

        // Build Order object from row.
        order = new Order((int) resultRow.get("orderNumber"), (String) resultRow.get("email"), (String) resultRow.get("phone"), ((Number) resultRow.get("totalPrice")).doubleValue(), (String) resultRow.get("orderDate"), (String) resultRow.get("orderStatus"), (String) resultRow.get("deliveryDate"));
        
        return order;
    }


    // Inserts new order row into database.
    // Parameters: order - Order object to insert into database.
    // Returns: new orderNumber when successful, -1 when failed.
    public int addOrder(Order order) {
        // Order number to return.
        int insertedOrderNumber = -1;

        // Insert order row into database.
        try { String insertOrderQuery = "INSERT INTO \"order\" " + "(email, phone, totalPrice, orderDate, orderStatus, deliveryDate) " + "VALUES (?, ?, ?, ?, ?, ?)";
            
            // Get new orderNumber for inserted order.
            insertedOrderNumber = runCustomUpdate(insertOrderQuery, order.getEmail(), order.getPhone(), order.getTotalPrice(), order.getOrderDate(), order.getOrderStatus(), order.getDeliveryDate());
        
        // If order insertion failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to add order: " + sqlException.getMessage());
        }
        return insertedOrderNumber;
    }


    // Finds one order by orderNumber.
    // Parameters: orderNumber - order number to find.
    // Returns: Order object when found, null when not found or on error.
    public Order findOrderByNumber(int orderNumber) {
        // Order object to return.
        Order foundOrder = null;

        // Query database for matching order row.
        try { List<Map<String, Object>> queryResults = runCustomQuery("SELECT orderNumber, email, phone, totalPrice, orderDate, orderStatus, deliveryDate FROM \"order\" WHERE orderNumber = ? LIMIT 1", orderNumber);

            // If order row found, build Order object.
            if (!queryResults.isEmpty()) {
                // Get the first row from the query results.
                Map<String, Object> resultRow = queryResults.get(0);
                // Build Order object from row and assign to foundOrder.
                foundOrder = buildOrderFromRow(resultRow);
            }

        // If order lookup failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to find order: " + sqlException.getMessage());
        }
        return foundOrder;
    }


    // Finds orders by customer email.
    // Parameters: email - Email address to look up orders for.
    // Returns: List of Order objects, empty list if none found.
    public List<Order> findOrdersByEmail(String email) {
        // List to store orders found for the email.
        List<Order> orderList = new ArrayList<>();
        // currentOrder object to hold each order as its built.
        Order currentOrder = null;

        // Query database for matching order rows.
        try {List<Map<String, Object>> queryResults = runCustomQuery("SELECT orderNumber, email, phone, totalPrice, orderDate, orderStatus, deliveryDate FROM \"order\" WHERE email = ? ORDER BY orderDate DESC", email);
            
            // For each row in query results, build Order object and add to list.
            for (Map<String, Object> resultRow : queryResults) {
                currentOrder = buildOrderFromRow(resultRow);
                orderList.add(currentOrder);
            }

        // If order lookup failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to load orders for email: " + sqlException.getMessage());
            orderList = new ArrayList<>();
        }
        return orderList;
    }


    // Finds all orders in database.
    // Parameters: none
    // Returns: List of Order objects, empty list if none found.
    public List<Order> findAllOrders() {
        // List to store all orders found in database.
        List<Order> orderList = new ArrayList<>();
        // currentOrder object to hold each order as its built.
        Order currentOrder = null;

        // Query database for all order rows, ordered by orderDate descending.
        try {List<Map<String, Object>> queryResults = runCustomQuery("SELECT orderNumber, email, phone, totalPrice, orderDate, orderStatus, deliveryDate FROM \"order\" ORDER BY orderDate DESC");
            
            // For each row in query results, build Order object and add to list.
            for (Map<String, Object> resultRow : queryResults) {
                currentOrder = buildOrderFromRow(resultRow);
                orderList.add(currentOrder);
            }

        // If order lookup failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to load orders: " + sqlException.getMessage());
            orderList = new ArrayList<>();
        }
        return orderList;
    }


    // Updates the status of an existing order.
    // Parameters: orderNumber - The order number to update.
    // Parameters: newStatus - New status enum value to set.
    // Returns: true when successful, false when failed.
    public boolean updateOrderStatus(int orderNumber, OrderStatus newStatus) {
        // Flag to indicate if order status was updated successfully.
        boolean success = false;

        // Update order status in database.
        try { int updatedRows = runCustomUpdate("UPDATE \"order\" SET orderStatus = ? WHERE orderNumber = ?", newStatus.name(), orderNumber);
            success = updatedRows > 0;

        // If order status update failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to update order status: " + sqlException.getMessage());
        }
        return success;
    }


    // Removes order from database by orderNumber.
    // Parameters: orderNumber - Order number to remove.
    // Returns: true when successful, false when failed.
    public boolean removeOrderByNumber(int orderNumber) {
        // Flag to indicate if order was removed successfully.
        boolean success = false;

        // Delete order row from database.
        try { int deleted = runCustomUpdate("DELETE FROM \"order\" WHERE orderNumber = ?", orderNumber);
            success = deleted > 0;

        // If order removal failed, print error message.
        } catch (SQLException sqlException) {
            System.out.println("Failed to remove order: " + sqlException.getMessage());
        }
        return success;
    }


    // Finds orders by product brands.
    public List<Order> findOrdersByBrand(String email) {

        //Will do this soon -Sharif.
        List<Order> tempList = new ArrayList<Order>(); //just added this to remove the error. delete when implementing this function.
        return tempList;
    }

}