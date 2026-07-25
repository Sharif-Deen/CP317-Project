
package features;

//  =================
//   IMPORTS:
//  =================

import java.util.Arrays;
import java.util.List;

//  =================
//   ORDER:
//  =================

public class Order {
    private int orderId;
    private String email;
    private String phone;
    private double totalPrice;
    private String orderDate;    // stored as text in DB 
    private String orderStatus;
    private String deliveryDate; // may be null

    public Order(int orderId, String email, String phone, double totalPrice, String orderDate, String orderStatus, String deliveryDate) {
        this.orderId = orderId;
        this.email = email;
        this.phone = phone;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.deliveryDate = deliveryDate;

        return;
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


    //  =================
    //   METHODS:
    //  =================

    // Method to get phone number as a long (without dashes or parentheses).
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
}
