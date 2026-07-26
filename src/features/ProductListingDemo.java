package features;

import java.util.Scanner;

public class ProductListingDemo {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductListing pl = new ProductListing(scanner);

        System.out.println("Laurier Food Services - Product Listing Manager");

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1. Create new product listing");
            System.out.println("2. Edit existing product listing");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                pl.createProduct();
            } else if (input.equals("2")) {
                pl.editProduct();
            } else if (input.equals("3")) {
                running = false;
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }

        pl.close();
        scanner.close();
    }
}