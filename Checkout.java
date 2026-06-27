import java.util.Scanner;

public class Checkout {


    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, CASH
    }

    public static class ShippingDetails {
        public String fullName;
        public String address;
        public String city;
        public String postalCode;
        public String phone;

        @Override
        public String toString() {
            return fullName + ", " + address + ", " + city + ", " + postalCode + " | Phone: " + phone;
        }
    }

    
    private static final double TAX_RATE = 0.13; // Ontario HST

    // Main Checkout Flow 
    public static void processCheckout(ShoppingCart cart, Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Please add items before checking out.");
            return;
        }

        // Step 1: Review cart
        cart.viewCart();

        // Step 2: Confirm order
        System.out.print("\nProceed to checkout? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("yes") && !confirm.equals("y")) {
            System.out.println("Checkout cancelled. Your cart has been kept.");
            return;
        }

        // Step 3: Collect shipping details
        ShippingDetails shipping = collectShippingDetails(scanner);

        // Step 4: Select payment method
        PaymentMethod method = selectPaymentMethod(scanner);

        // Step 5: Process payment
        boolean success = processPayment(cart, method, scanner);

        // Step 6: Finalize
        if (success) {
            printReceipt(cart, method, shipping);
            cart.clearCart();
        } else {
            System.out.println("Payment failed. Your cart has been kept.");
        }
    }

    // Shipping Details Collection
    private static ShippingDetails collectShippingDetails(Scanner scanner) {
        ShippingDetails details = new ShippingDetails();

        System.out.println("\n-- Shipping Details --");

        System.out.print("Full Name: ");
        details.fullName = scanner.nextLine().trim();

        System.out.print("Delivery Address: ");
        details.address = scanner.nextLine().trim();

        System.out.print("City: ");
        details.city = scanner.nextLine().trim();

        System.out.print("Postal Code (e.g. N2L 3C5): ");
        details.postalCode = scanner.nextLine().trim().toUpperCase();
        if (!details.postalCode.matches("[A-Z]\\d[A-Z] \\d[A-Z]\\d")) {
            System.out.println("Warning: Postal code format may be invalid.");
        }

        System.out.print("Phone Number: ");
        details.phone = scanner.nextLine().trim();

        return details;
    }

    // Payment Method Selection 
    private static PaymentMethod selectPaymentMethod(Scanner scanner) {
        System.out.println("\nSelect Payment Method:");
        System.out.println("  1. Credit Card");
        System.out.println("  2. Debit Card");
        System.out.println("  3. Cash");
        System.out.print("Enter choice (1-3): ");

        while (true) {
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return PaymentMethod.CREDIT_CARD;
                case "2": return PaymentMethod.DEBIT_CARD;
                case "3": return PaymentMethod.CASH;
                default:
                    System.out.print("Invalid choice. Enter 1, 2, or 3: ");
            }
        }
    }

    // Payment Dispatcher 
    private static boolean processPayment(ShoppingCart cart, PaymentMethod method, Scanner scanner) {
        double subtotal = cart.getTotal();
        double tax      = subtotal * TAX_RATE;
        double total    = subtotal + tax;

        System.out.printf("%nSubtotal : $%.2f%n", subtotal);
        System.out.printf("HST (13%%): $%.2f%n", tax);
        System.out.printf("Total    : $%.2f%n", total);

        switch (method) {
            case CREDIT_CARD:
            case DEBIT_CARD:
                return processCardPayment(method, total, scanner);
            case CASH:
                return processCashPayment(total, scanner);
            default:
                System.out.println("Unknown payment method.");
                return false;
        }
    }

    // Card Payment 
    private static boolean processCardPayment(PaymentMethod type, double total, Scanner scanner) {
        String label = (type == PaymentMethod.CREDIT_CARD) ? "Credit" : "Debit";
        System.out.println("\n-- " + label + " Card Payment --");

        System.out.print("Enter card number (16 digits): ");
        String cardNum = scanner.nextLine().trim().replaceAll("\\s+", "");
        if (!cardNum.matches("\\d{16}")) {
            System.out.println("Invalid card number.");
            return false;
        }

        System.out.print("Enter card holder name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Card holder name cannot be empty.");
            return false;
        }

        System.out.print("Enter expiry date (MM/YY): ");
        String expiry = scanner.nextLine().trim();
        if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            System.out.println("Invalid expiry format.");
            return false;
        }

        System.out.print("Enter CVV (3 digits): ");
        String cvv = scanner.nextLine().trim();
        if (!cvv.matches("\\d{3}")) {
            System.out.println("Invalid CVV.");
            return false;
        }

        System.out.printf("Processing $%.2f on %s card ending in %s...%n",
                total, label, cardNum.substring(12));
        System.out.println("Payment approved.");
        return true;
    }

    // Cash Payment 
    private static boolean processCashPayment(double total, Scanner scanner) {
        System.out.println("\n-- Cash Payment --");
        System.out.printf("Amount due: $%.2f%n", total);
        System.out.print("Enter cash tendered: $");

        double tendered;
        try {
            tendered = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount entered.");
            return false;
        }

        if (tendered < total) {
            System.out.printf("Insufficient cash. You are $%.2f short.%n", total - tendered);
            return false;
        }

        System.out.printf("Change due: $%.2f%n", tendered - total);
        return true;
    }

    // Receipt
    private static void printReceipt(ShoppingCart cart, PaymentMethod method, ShippingDetails shipping) {
        double subtotal = cart.getTotal();
        double tax      = subtotal * TAX_RATE;
        double total    = subtotal + tax;

        System.out.println("\n=============================");
        System.out.println("    LAURIER FOOD SERVICES");
        System.out.println("          RECEIPT");
        System.out.println("=============================");
        cart.viewCart();
        System.out.printf("HST (13%%):     $%.2f%n", tax);
        System.out.printf("Total:         $%.2f%n", total);
        System.out.println("Payment:       " + formatPaymentMethod(method));
        System.out.println("-----------------------------");
        System.out.println("Deliver to:    " + shipping.fullName);
        System.out.println("               " + shipping.address);
        System.out.println("               " + shipping.city + "  " + shipping.postalCode);
        System.out.println("               " + shipping.phone);
        System.out.println("=============================");
        System.out.println("   Thank you for your order!");
        System.out.println("=============================\n");
    }

    // Helper 
    private static String formatPaymentMethod(PaymentMethod method) {
        switch (method) {
            case CREDIT_CARD: return "Credit Card";
            case DEBIT_CARD:  return "Debit Card";
            case CASH:        return "Cash";
            default:          return "Unknown";
        }
    }
}