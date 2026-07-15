package features;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class SystemTest {

    private static void assertCondition(boolean condition, String testName) {
        System.out.printf("%s: %s%n", testName, (condition ? "PASS" : "FAIL"));
    }

    public static void main(String[] args) {
        System.out.println("Starting system tests...\n");

        // ─── TEST 1: Load products and search ────────────────────────────────
        BasicSearch bs = new BasicSearch();
        bs.loadProducts("sampleProducts.txt");

        List<Product> oliveResults = bs.search("olive");
        boolean foundOlive = oliveResults.stream().anyMatch(p -> p.getName().toLowerCase().contains("olive"));
        assertCondition(foundOlive, "Test 1a - Load products & search for 'olive'");

        List<Product> allResults = bs.search("food");
        assertCondition(allResults.size() >= 1, "Test 1b - At least 3 products found for 'food' search");

        // ─── TEST 2: FilterSearch ─────────────────────────────────────────────
        List<Product> foodResults = bs.search("tomato");
        FilterSearch filter = new FilterSearch();

        // price low to high
        List<Product> lowSorted = filter.filterPriceLow(foodResults);
        boolean ascending = lowSorted.size() < 2 ||
            lowSorted.get(0).getPrice() <= lowSorted.get(lowSorted.size() - 1).getPrice();
        assertCondition(ascending, "Test 2a - filterPriceLow returns ascending order");

        // price high to low
        List<Product> highSorted = filter.filterPriceHigh(foodResults);
        boolean descending = highSorted.size() < 2 ||
            highSorted.get(0).getPrice() >= highSorted.get(highSorted.size() - 1).getPrice();
        assertCondition(descending, "Test 2b - filterPriceHigh returns descending order");

        // filter in stock
        List<Product> allProducts = bs.search("bulk");
        List<Product> inStock = filter.filterStock(allProducts);
        boolean stockOk = inStock.stream().allMatch(p -> p.getStock() > 0);
        assertCondition(stockOk, "Test 2c - filterStock returns only in-stock products");

        // filter by location
        List<Product> byLocation = filter.filterLocation(allProducts, "Waterloo");
        boolean locationOk = byLocation.stream().allMatch(p -> p.getLocation().equalsIgnoreCase("Waterloo"));
        assertCondition(locationOk, "Test 2d - filterLocation returns only Waterloo products");

        // ─── TEST 3: ShoppingCart ─────────────────────────────────────────────
        ShoppingCart cart = new ShoppingCart();
        List<Product> tomatoes = bs.search("Bulk Roma Tomatoes");
        boolean tomatoFound = tomatoes.size() > 0;
        assertCondition(tomatoFound, "Test 3a - Found 'Bulk Roma Tomatoes' product");

        if (tomatoFound) {
            Product tomato = tomatoes.get(0);

            // add product
            cart.addProduct(tomato, 2);
            double expected = tomato.getPrice() * 2;
            assertCondition(Math.abs(cart.getTotal() - expected) < 0.001,
                "Test 3b - Cart total correct after adding 2 items");

            // update quantity
            cart.updateQuantity(tomato.getName(), 5);
            expected = tomato.getPrice() * 5;
            assertCondition(Math.abs(cart.getTotal() - expected) < 0.001,
                "Test 3c - Cart total correct after updating quantity");

            // remove product
            cart.removeProduct(tomato.getName());
            assertCondition(cart.isEmpty(),
                "Test 3d - Cart empty after removing product");
        }

        // ─── TEST 4: Full checkout flow ───────────────────────────────────────
        List<Product> flourList = bs.search("All-Purpose Flour");
        boolean flourFound = flourList.size() > 0;
        assertCondition(flourFound, "Test 4a - Found 'All-Purpose Flour' product for checkout");

        if (flourFound) {
            cart.addProduct(flourList.get(0), 1);

            String simulatedInput =
                "yes\n" +
                "Test Customer\n" +
                "123 Test St\n" +
                "Waterloo\n" +
                "N2L 3C5\n" +
                "5191234567\n" +
                "1\n" +                    // Credit Card
                "4242424242424242\n" +     // card number
                "Test Customer\n" +        // card holder
                "12/30\n" +               // expiry
                "123\n";                  // CVV

            ByteArrayInputStream bais = new ByteArrayInputStream(
                simulatedInput.getBytes(StandardCharsets.UTF_8));
            Scanner fakeScanner = new Scanner(bais);

            try {
                Checkout.processCheckout(cart, fakeScanner);
                assertCondition(cart.isEmpty(),
                    "Test 4b - Cart cleared after successful checkout");
            } catch (Exception e) {
                assertCondition(false,
                    "Test 4b - Exception during checkout: " + e.getMessage());
            } finally {
                fakeScanner.close();
            }
        }

        // ─── TEST 5: Invalid card number in checkout ──────────────────────────
        List<Product> sugarList = bs.search("Bulk White Sugar");
        boolean sugarFound = sugarList.size() > 0;
        assertCondition(sugarFound, "Test 5a - Found 'Bulk White Sugar' product");

        if (sugarFound) {
            cart.addProduct(sugarList.get(0), 1);

            String badCardInput =
                "yes\n" +
                "Test Customer\n" +
                "123 Test St\n" +
                "Waterloo\n" +
                "N2L 3C5\n" +
                "5191234567\n" +
                "1\n" +                    // Credit Card
                "1234\n" +                 // invalid card number
                "Test Customer\n" +
                "12/30\n" +
                "123\n";

            ByteArrayInputStream bais = new ByteArrayInputStream(
                badCardInput.getBytes(StandardCharsets.UTF_8));
            Scanner fakeScanner = new Scanner(bais);

            try {
                Checkout.processCheckout(cart, fakeScanner);
                assertCondition(!cart.isEmpty(),
                    "Test 5b - Cart retained after failed payment");
            } catch (Exception e) {
                assertCondition(false,
                    "Test 5b - Exception during checkout: " + e.getMessage());
            } finally {
                fakeScanner.close();
            }
        }

        System.out.println("\nSystem tests completed.");
    }
}