package Models;

import java.util.LinkedList;
import java.util.List;

public class WishList {
    private List<Product> products;    

    public WishList() {
        this.products = new LinkedList<>();
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product removeProduct(int index) {
        return products.remove(index);
    }

}
