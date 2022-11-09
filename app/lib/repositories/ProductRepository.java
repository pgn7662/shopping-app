package repositories;

import java.util.List;

import Models.Delivery;
import Models.Product;

public interface ProductRepository {

    public List<Product> getAllProducts();

    public List<Product> getProductsBySearch(String searchText);

    public void updateProduct(Product product);

    public Product addProduct(Product product);

    public List<Delivery> getPendingDeliveries();

    public boolean updateDeliveryStatus(Delivery delivery);

}
