
package features;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private String email;
    private String phone;
    private double totalPrice;
    private String orderDate;    // stored as text in DB 
    private String orderStatus;
    private String deliveryDate;
    private List<OrderItem> products = new ArrayList<>();

    public Order() {
    }

    public Order(int orderId, String email, String phone, double totalPrice, String orderDate, String orderStatus, String deliveryDate) {
        this(orderId, email, phone, orderDate, orderStatus, deliveryDate, new ArrayList<>());
        this.totalPrice = totalPrice;
    }

    public Order(int orderId, String email, String phone, String orderDate, String orderStatus, String deliveryDate, List<OrderItem> products) {
        this.orderId = orderId;
        this.email = email;
        this.phone = phone;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.products = products == null ? new ArrayList<>() : new ArrayList<>(products);
        recalculateTotalPrice();
    }

    // Getters
    public int getOrderId() {
        return orderId;
    }
    
    public String getEmail() { 
        return email; 
    }

    public String getPhone() { 
        return phone; 
    }

    public double getTotalPrice() {
        return totalPrice; 
    }

    public String getOrderDate() { 
        return orderDate; 
    }

    public String getOrderStatus() { 
        return orderStatus; 
    }

    public String getDeliveryDate() { 
        return deliveryDate; 
    }

    public List<OrderItem> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products == null ? new ArrayList<>() : new ArrayList<>(products);
        recalculateTotalPrice();
    }

    private void recalculateTotalPrice() {
        double calculatedTotal = 0.0;
        for (OrderItem productItem : products) {
            if (productItem != null) {
                calculatedTotal += productItem.getLineTotal();
            }
        }
        this.totalPrice = calculatedTotal;
    }

    public long getPhoneAsNumber() {
        long result = 0L;
        String phoneString;

        if (phone == null) {
            phoneString = "";
        } else {
            phoneString = phone;
        }

        String digitsOnly = phoneString.replaceAll("\\D", "");

        if (!digitsOnly.isEmpty()) {
            result = Long.parseLong(digitsOnly);
        }

        return result;
    }

    // Override toString method for easy printing of order details.
    @Override
    public String toString() {
        return "Order #" + orderId + " | " + email + " | " + phone + " | $" + totalPrice + " | " + orderStatus;
    }

    public static class OrderItem {
        private Product product;
        private int quantity;
        private int productId;
        private String productName;
        private double price;

        public OrderItem() {
        }

        public OrderItem(int productId, String productName, int quantity, double price) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }

        public int getProductId() {
            syncFromProduct();
            return productId;
        }

        public String getProductName() {
            syncFromProduct();
            return productName;
        }

        public double getPrice() {
            syncFromProduct();
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
            syncFromProduct();
        }

        public double getLineTotal() {
            return Math.max(quantity, 0) * getPrice();
        }

        private void syncFromProduct() {
            if (product != null) {
                if (productId <= 0 && product.getId() > 0) {
                    productId = product.getId();
                }
                if ((productName == null || productName.isEmpty()) && product.getName() != null) {
                    productName = product.getName();
                }
                if (price <= 0.0 && product.getPrice() > 0.0) {
                    price = product.getPrice();
                }
            }
        }
    }
}
