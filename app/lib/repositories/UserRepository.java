package repositories;

import Models.CartProduct;
import Models.Customer;
import Models.User;

public interface UserRepository {

    public boolean isUserEmailPresent(String email);

    public User getUser(String email, String password);

    public User addUser(Customer customer);

    public boolean updateUser(User user);

    public boolean updateCart(Customer customer);

    public boolean updateWishlist(Customer customer);

    public boolean updateCustomerPaymentDetails(Customer customer);
    
    public boolean updateCartProduct(Customer customer, CartProduct cartProduct);

    public boolean updatePurchasedProducts(Customer customer);

}
