import java.util.Arrays;
import java.util.List;

public class Product {
    private String name;
    private double price;
    private String type;
    private String brand;
    private List<String> tags;
    private String description;

    public Product(String name, double price, String type, String brand, String tags, String description) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.brand = brand;
        this.tags = Arrays.asList(tags.split(","));
        this.description = description;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public List<String> getTags() { return tags; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name + " | $" + price + " | " + type + " | " + brand;
    }
}
