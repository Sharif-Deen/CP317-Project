package features;
import java.util.ArrayList;
import java.util.List;

public class FilterSearch {
    public List<Product> filterPriceLow(List<Product> searchResult) {
        List<Product> filteredResult = new ArrayList<>(searchResult);
        for (int index = 0; index < filteredResult.size() - 1; index++) {
            for (int index2 = 0; index2 < filteredResult.size() - index - 1; index2++) {
                Product firstProduct = filteredResult.get(index2 + 1);
                Product secondProduct = filteredResult.get(index2);
                if (firstProduct.getPrice() < secondProduct.getPrice()) {
                    Product saved = firstProduct;
                    filteredResult.set(index2 + 1, secondProduct);
                    filteredResult.set(index2, saved);
                }
            }
        }
        return filteredResult;
    }

    public List<Product> filterPriceHigh(List<Product> searchResult) {
        List<Product> filteredResult = new ArrayList<>(searchResult);
        for (int index = 0; index < filteredResult.size() - 1; index++) {
            for (int index2 = 0; index2 < filteredResult.size() - index - 1; index2++) {
                Product firstProduct = filteredResult.get(index2 + 1);
                Product secondProduct = filteredResult.get(index2);
                if (firstProduct.getPrice() > secondProduct.getPrice()) {
                    Product saved = firstProduct;
                    filteredResult.set(index2 + 1, secondProduct);
                    filteredResult.set(index2, saved);
                }
            }
        }
        return filteredResult;
    }

    public List<Product> filterStock(List<Product> searchResult) {
        List<Product> filteredResult = new ArrayList<>();
        for (int index = 0; index < searchResult.size(); index++) {
            if (searchResult.get(index).getStock() > 0) {
                filteredResult.add(searchResult.get(index));
            }
        }
        return filteredResult;
    }

    public List<Product> filterLocation(List<Product> searchResult, String location) {
        List<Product> filteredResult = new ArrayList<>();
        for (int index = 0; index < searchResult.size(); index++) {
            String lowercaseLocation = searchResult.get(index).getLocation().toLowerCase();
            String lowercaseUserLocation = location.toLowerCase();
            if (lowercaseLocation.equals(lowercaseUserLocation.trim())) {
                filteredResult.add(searchResult.get(index));
            }
        }
        return filteredResult;
    }
}
