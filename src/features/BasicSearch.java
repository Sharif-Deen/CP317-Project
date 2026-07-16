package features;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BasicSearch {

    private List<Product> products = new ArrayList<>();


    public void loadProducts(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int id = 1;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length < 8) continue;

                String name = parts[0].trim();
                double price = Double.parseDouble(parts[1].trim());
                String type = parts[2].trim();
                String brand = parts[3].trim();
                String tags = parts[4].trim();
                String description = parts[5].trim();
                String location = parts[6].trim();
                int stock = Integer.parseInt(parts[7].trim());

                products.add(new Product(id, name, price, type, brand, tags, description, location, stock));
                id++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Could not load products file: " + e.getMessage());
        }
    }

    public List<Product> search(String keyword) {
        List<Product> results = new ArrayList<>();
        String kw = keyword.toLowerCase().trim();

        for (Product p : products) {
            boolean matchesName = p.getName().toLowerCase().contains(kw);
            boolean matchesBrand = p.getBrand().toLowerCase().contains(kw);
            boolean matchesType = p.getType().toLowerCase().contains(kw);
            boolean matchesDesc = p.getDescription().toLowerCase().contains(kw);
            boolean matchesTag = false;
            boolean matchesLoc = p.getLocation().toLowerCase().contains(kw);

            for (String tag : p.getTags()) {
                if (tag.toLowerCase().contains(kw)) {
                    matchesTag = true;
                    break;
                }
            }

            if (matchesName || matchesBrand || matchesType || matchesDesc || matchesTag || matchesLoc) {
                results.add(p);
            }
        }

        return results;
    }

    public static void main(String[] args) {
        BasicSearch bs = new BasicSearch();
        bs.loadProducts("sampleProducts.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Laurier Food Services - Product Search");
        System.out.println("Type a keyword to search or 'exit' to quit.");

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

            List<Product> results = bs.search(input);

            if (results.isEmpty()) {
                System.out.println("No results found for: " + input);
            } else {
                System.out.println(results.size() + " result(s) found:");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println((i + 1) + ". " + results.get(i));
                }
            }
        }

        scanner.close();
    }
}
