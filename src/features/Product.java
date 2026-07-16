package features;
import java.util.Arrays;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private double price;
    private String type;
    private String brand;
    private List<String> tags;
    private String description;
    private String location;
    private int stock;

    public Product(int id, String name, double price, String type, String brand, String tags, String description, String location, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.brand = brand;
        this.tags = Arrays.asList(tags.split(","));
        this.description = description;
        this.location = location;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getType() { return type; }
    public String getBrand() { return brand; }
    public List<String> getTags() { return tags; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public int getStock() { return stock; }

    public void setid(int id) { this.id = id; }

    @Override
    public String toString() {
        return name + " | $" + price + " | " + type + " | " + brand;
    }
}
