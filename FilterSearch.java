import java.util.ArrayList;
// ProductTags are delimited with a comma.
// Products are of the format productName|productPrice|productType|productBrand|productTags|productDescription

public class FilterSearch {
    private String category;
    private String type;

    public FilterSearch(String category, String type) {
        this.category = category;
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }
}