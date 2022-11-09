package Models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Delivery {
    private UUID uuid;
    private List<CartProduct> cartProducts;
    private Customer customer;
    private Date date;
    private DeliveryStatus deliveryStatus;

    public Delivery(List<CartProduct> cartProducts, Customer customer) {
        this(UUID.randomUUID(), cartProducts, customer, new Date(), DeliveryStatus.PENDING);
    }

    public Delivery(UUID uuid, List<CartProduct> cartProducts, Customer customer, Date date, DeliveryStatus deliveryStatus) {
        this.uuid = uuid;
        this.cartProducts = cartProducts;
        this.customer = customer;
        this.date = date;
        this.deliveryStatus = deliveryStatus;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

}
