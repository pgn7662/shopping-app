package pages;

import java.util.LinkedList;
import java.util.List;

import Models.Cart;
import Models.CartProduct;
import Models.Customer;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public class CartPage extends Page {
    private Customer customer;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public CartPage(Customer customer, UserRepository userRepository, ProductRepository productRepository) {
        this.customer = customer;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.SPACE_BETWEEN, "Cart", customer.getName()));
        body.add(new BodyBuilder(Body.FILL | Body.LEAVE_LINE_BELOW, "-"));
        Cart cart = customer.getCart();
        if (cart == null || cart.getCartProducts().isEmpty()) {
            body.add(new BodyBuilder("Oops, there are no products in cart! :("));
        } else {
            double totalAmount = 0;
            for (CartProduct cartProduct : cart.getCartProducts())
                totalAmount += cartProduct.getCurrentPrice();
            body.add(new BodyBuilder(new ListView<>(cart.getCartProducts(), MAX_WIDTH).buildView(
                    (cartProduct) -> new String[] {
                            cartProduct.getName(),
                            // TODO rating
                            "Quantity: " + cartProduct.getQuantity(),
                            "Price: " + cartProduct.getCurrentPrice()
                                    + (cartProduct.getDiscountPercent() != 0
                                            ? "(" + cartProduct.getDiscountPercent() + "% off (per product))"
                                            : ""),
                    })));
            body.add(new BodyBuilder(Body.LEAVE_LINE_BELOW, "Total amount = " + totalAmount));
        }
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialogMenu = new DialogMenu(
                    Option.SELECT_A_PRODUCT_LISTED_ABOVE,
                    Option.EDIT_CART_PRODUCT,
                    Option.PROCEED_TO_BUY,
                    Option.REMOVE_PRODUCT_FROM_CART,
                    Option.CLEAR_ALL,
                    Option.GO_BACK);
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case SELECT_A_PRODUCT_LISTED_ABOVE:
                case EDIT_CART_PRODUCT:
                    int i = AppConsole.getInt("Enter product serial no. listed above to edit it", 1,
                            customer.getCart().getCartProducts().size());
                    ProductCatalog catalog = new ProductCatalog(customer, userRepository, productRepository,
                            customer.getCart().getCartProducts().get(i - 1), option.equals(Option.EDIT_CART_PRODUCT));
                    catalog.buildAndRun();
                    if (option.equals(Option.EDIT_CART_PRODUCT))
                        userRepository.updateCartProduct(customer, customer.getCart().getCartProducts().get(i));
                    break;
                case PROCEED_TO_BUY:
                    double totalAmount = 0;
                    for (CartProduct cartProduct : customer.getCart().getCartProducts())
                        totalAmount += cartProduct.getCurrentPrice();
                    PaymentPage paymentPage = new PaymentPage(customer, userRepository, totalAmount);
                    paymentPage.buildAndRun();
                    if (paymentPage.getIsPaymentSuccessfull()) {
                        for (CartProduct cartProduct : customer.getCart().getCartProducts())
                            customer.addNewPurchasedProducts(cartProduct);
                        userRepository.updatePurchasedProducts(customer);
                    }
                    break;
                case REMOVE_PRODUCT_FROM_CART:
                    int ri = AppConsole.getInt("Enter product serial no. listed above to edit it", 1,
                            customer.getCart().getCartProducts().size());
                    CartProduct c = customer.getCart().removeCartProduct(ri - 1);
                    if (userRepository.updateCart(customer))
                        System.out.println(c.getName() + " is successfully removed from your cart");
                    AppConsole.waitUntilReturn();
                    break;
                case CLEAR_ALL:
                    customer.getCart().clearCart();
                    if (userRepository.updateCart(customer))
                        System.out.println("All items in cart are removed succesfully!");
                    AppConsole.waitUntilReturn();
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("Default case " + option);
                    AppConsole.waitUntilReturn();
                    break;
            }
        } while (true);
    }

}
