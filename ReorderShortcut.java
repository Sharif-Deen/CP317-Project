import java.util.ArrayList;
import java.util.List;

public class ReorderShortcut {

    private List<PreviousOrder> previousOrders;

    public ReorderShortcut() {
        previousOrders = new ArrayList<>();
        loadSampleOrders();
    }

    private void loadSampleOrders() {
        PreviousOrder order1 = new PreviousOrder("1001");

        order1.addItem(new Product(
                "Bulk Roma Tomatoes",
                24.50,
                "food",
                "FreshFields",
                "vegetable,produce,bulk",
                "Ripe Roma tomatoes packed for restaurants and catering.",
                "Waterloo",
                200
        ), 2);

        order1.addItem(new Product(
                "Organic White Rice 20kg",
                39.99,
                "food",
                "GrainHouse",
                "rice,bulk,grain",
                "Large bag of organic white rice for restaurants.",
                "Cambridge",
                80
        ), 1);

        PreviousOrder order2 = new PreviousOrder("1002");

        order2.addItem(new Product(
                "Frozen Chicken Breast 10kg",
                79.99,
                "food",
                "ChefPro",
                "meat,frozen,bulk,protein",
                "Lean boneless chicken breasts.",
                "Kitchener",
                150
        ), 3);

        order2.addItem(new Product(
                "Olive Oil 5L",
                32.75,
                "food",
                "PureHarvest",
                "oil,cooking,bulk",
                "Large bottle of olive oil for cooking.",
                "Waterloo",
                50
        ), 1);

        previousOrders.add(order1);
        previousOrders.add(order2);
    }

    public void viewPreviousOrders() {
        if (previousOrders.isEmpty()) {
            System.out.println("No previous orders found.");
            return;
        }

        System.out.println();
        System.out.println("Previous Orders");
        System.out.println("-------------------------");

        for (int i = 0; i < previousOrders.size(); i++) {
            PreviousOrder order = previousOrders.get(i);

            System.out.println((i + 1) + ". Order #" + order.getOrderNumber());

            for (OrderItem item : order.getItems()) {
                System.out.println("   " + item.getProduct().getName() + " x" + item.getQuantity());
            }

            System.out.println();
        }
    }

    public void reorder(int orderNumber, ShoppingCart cart) {
        if (orderNumber < 1 || orderNumber > previousOrders.size()) {
            System.out.println("Invalid order number.");
            return;
        }

        PreviousOrder selectedOrder = previousOrders.get(orderNumber - 1);

        for (OrderItem item : selectedOrder.getItems()) {
            cart.addProduct(item.getProduct(), item.getQuantity());
        }

        System.out.println("Order #" + selectedOrder.getOrderNumber() + " was added to the shopping cart.");
    }

    private class PreviousOrder {
        private String orderNumber;
        private List<OrderItem> items;

        public PreviousOrder(String orderNumber) {
            this.orderNumber = orderNumber;
            this.items = new ArrayList<>();
        }

        public void addItem(Product product, int quantity) {
            items.add(new OrderItem(product, quantity));
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public List<OrderItem> getItems() {
            return items;
        }
    }

    private class OrderItem {
        private Product product;
        private int quantity;

        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}