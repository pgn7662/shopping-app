package Models;

import java.util.List;

public class Admin extends User {
    private List<Product> addedProducts;
    private List<Category> addedCategories;

    public Admin(String name, String email, String password, long phone, Address address) {
        super(name, email, password, phone, address);
    }

    public List<Product> getAddedProducts() {
        return addedProducts;
    }

    public void setAddedProducts(List<Product> addedProducts) {
        this.addedProducts = addedProducts;
    }

    public List<Category> getAddedCategories() {
        return addedCategories;
    }

    public void setAddedCategories(List<Category> addedCategories) {
        this.addedCategories = addedCategories;
    }

    public void addNewAddedCategory(Category category) {
        addedCategories.add(category);
    }

    public void addNewAddedProduct(Product product) {
        addedProducts.add(product);
    }

}
