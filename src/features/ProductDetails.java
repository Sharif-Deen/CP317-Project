package features;
import java.util.List;
import java.util.Scanner;

public class ProductDetails {

    private BasicSearch search;

    public ProductDetails() {
        search = new BasicSearch();
        search.loadProducts("sampleProducts.txt");
    }

    public void printProduct(Product p) {
        System.out.println();
        System.out.println("Product Details");
        System.out.println("-------------------------");
        System.out.println("Name: " + p.getName());
        System.out.println("Price: $" + p.getPrice());
        System.out.println("Category: " + p.getType());
        System.out.println("Brand: " + p.getBrand());
        System.out.println("Tags: " + p.getTags());
        System.out.println("Description: " + p.getDescription());
        System.out.println("Location: " + p.getLocation());
        System.out.println("Stock: " + p.getStock() + " units");
        System.out.println("-------------------------");
    }

    public static void main(String[] args) {
        ProductDetails pd = new ProductDetails();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Laurier Food Services - View Product Details");
        System.out.println("Type a product name to search or 'exit' to quit.");

        while (true) {
            System.out.print("\nSearch: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.isEmpty()) {
                System.out.println("Please enter something to search.");
                continue;
            }

            List<Product> results = pd.search.search(input);

            if (results.isEmpty()) {
                System.out.println("No products found for: " + input);
                continue;
            }

            if (results.size() > 1) {
                System.out.println(results.size() + " results found:");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println((i + 1) + ". " + results.get(i).getName());
                }

                System.out.print("Enter the number of the product you want to view: ");
                String pick = scanner.nextLine().trim();

                int index;
                try {
                    index = Integer.parseInt(pick) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    continue;
                }

                if (index < 0 || index >= results.size()) {
                    System.out.println("Invalid selection.");
                    continue;
                }

                pd.printProduct(results.get(index));

            } else {
                pd.printProduct(results.get(0));
            }
        }

        scanner.close();
    }
}
