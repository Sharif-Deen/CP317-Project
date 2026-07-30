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
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(int orderId, String email, String phone, double totalPrice, String orderDate, String orderStatus, String deliveryDate) {
        this(orderId, email, phone, orderDate, orderStatus, deliveryDate, new ArrayList<>());
        this.totalPrice = totalPrice;
    }

    public Order(int orderId, String email, String phone, String orderDate, String orderStatus, String deliveryDate, List<OrderItem> items) {
        this.orderId = orderId;
        this.email = email;
        this.phone = phone;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
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
        recalculateTotalPrice();
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
        return items;
    }

    public void addItem(Product product, int quantity){
        OrderItem newItem = new OrderItem(product, quantity);
        if(this.items==null) this.items = new ArrayList<>();
        this.items.add(newItem);
        recalculateTotalPrice();
    }

    public void setProducts(List<OrderItem> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        recalculateTotalPrice();
    }

    public void recalculateTotalPrice() {
        double calculatedTotal = 0.0;
        for (OrderItem productItem : items) {
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
        String orderString = "Order #" + orderId + " | " + email + " | " + phone + " | $" + totalPrice + " | " + orderStatus + " | ";
        for(OrderItem item : this.items){
            orderString += item.product.getName() + ":" + item.quantity + ",";
        }
        return orderString;
    }

    public class OrderItem {
        private Product product;
        private int quantity;

        public OrderItem() {
        }

        public OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() { return product; }
        public int getProductId() { return product.getId(); }
        public String getProductName() { return product.getName(); }
        public double getPrice() { return product.getPrice(); }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }


        public void setProduct(Product product) {
            this.product = product;
        }

        public double getLineTotal() {
            return Math.max(quantity, 0) * this.product.getPrice();
        }

    }
}
