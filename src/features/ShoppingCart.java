package features;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    private List<CartItem> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            System.out.println("Product cannot be added because it does not exist.");
            return;
        }

        if (quantity <= 0) {
            System.out.println("Quantity must be greater than 0.");
            return;
        }

        for (CartItem item : items) {
            if (item.getProduct().getName().equalsIgnoreCase(product.getName())) {
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println(product.getName() + " quantity was updated in the cart.");
                return;
            }
        }

        items.add(new CartItem(product, quantity));
        System.out.println(product.getName() + " was added to the cart.");
    }

    public void removeProduct(String productName) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);

            if (item.getProduct().getName().equalsIgnoreCase(productName)) {
                items.remove(i);
                System.out.println(productName + " was removed from the cart.");
                return;
            }
        }

        System.out.println(productName + " was not found in the cart.");
    }

    public void updateQuantity(String productName, int newQuantity) {
        if (newQuantity <= 0) {
            removeProduct(productName);
            return;
        }

        for (CartItem item : items) {
            if (item.getProduct().getName().equalsIgnoreCase(productName)) {
                item.setQuantity(newQuantity);
                System.out.println(productName + " quantity was updated.");
                return;
            }
        }

        System.out.println(productName + " was not found in the cart.");
    }

    public double getTotal() {
        double total = 0;

        for (CartItem item : items) {
            if (item.getQuantity() >= 50) {
                double initialTotal = item.getProduct().getPrice() * item.getQuantity();
                total += initialTotal - (initialTotal * 0.10); 
            } else {
                total += item.getProduct().getPrice() * item.getQuantity();
            }
        }

        return total;
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println();
        System.out.println("Shopping Cart");
        System.out.println("-------------------------");

        for (CartItem item : items) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            double itemTotal = product.getPrice() * quantity;

            System.out.println("Product: " + product.getName());
            System.out.println("Price: $" + product.getPrice());
            System.out.println("Quantity: " + quantity);
            System.out.println("Item Total: $" + String.format("%.2f", itemTotal));
            System.out.println();
        }

        System.out.println("-------------------------");
        System.out.println("Cart Total: $" + String.format("%.2f", getTotal()));
    }

    public void clearCart() {
        items.clear();
        System.out.println("Cart has been cleared.");
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
