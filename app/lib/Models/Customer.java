package Models;

import java.util.List;

public class Customer extends User {
    private Cart cart;
    private WishList wishList;
    private List<Product> viewedProducts, purchasedProducts;
    // TODO coupons
    private List<String> cards, upis;

    public Customer(String name, String email, String password, long phone, Address address) {
        super(name, email, password, phone, address);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public List<Product> getViewedProducts() {
        return viewedProducts;
    }

    public void setViewedProducts(List<Product> viewedProducts) {
        this.viewedProducts = viewedProducts;
    }

    public void addViewedProducts(Product product) {
        if (!viewedProducts.contains(product))
            viewedProducts.add(0, product);
    }

    public List<Product> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(List<Product> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public void addNewPurchasedProducts(Product product) {
        for (Product p : purchasedProducts)
            if (p.equals(product)) {
                p.setQuantity(p.getQuantity() + product.getQuantity());
                return;
            }
        purchasedProducts.add(product);
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public void addNewCardNumber(String cardNo) {
        if (!cards.contains(cardNo))
            cards.add(cardNo);
    }

    public List<String> getUpis() {
        return upis;
    }

    public void setUpis(List<String> upis) {
        this.upis = upis;
    }

    public void addNewUpiId(String upiId) {
        if (!upis.contains(upiId))
            upis.add(upiId);
    }

}