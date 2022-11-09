package Models;

import java.util.LinkedList;
import java.util.List;

public class Cart {
    private List<CartProduct> cartProducts;

    public Cart() {
        this.cartProducts = new LinkedList<>();
    }

    public void addCartProduct(CartProduct product) {
        this.cartProducts.add(product);
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public boolean removeCartProduct(CartProduct cartProduct) {
        return cartProducts.remove(cartProduct);
    }

    public CartProduct removeCartProduct(int index) {
        return cartProducts.remove(index);
    }

    public void clearCart() {
        cartProducts.clear();
    }

}
