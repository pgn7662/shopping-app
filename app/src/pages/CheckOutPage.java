package pages;

import java.util.LinkedList;
import java.util.List;

import Models.CartProduct;
import Models.Customer;
import app.AppConsole;
import layout.Body;
import layout.BodyBuilder;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public class CheckOutPage extends Page {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private Customer customer;
    private List<CartProduct> cartProducts;

    private double totalAmount;

    public CheckOutPage(Customer customer, UserRepository userRepository, ProductRepository productRepository,
            List<CartProduct> cartProducts) {
        this.customer = customer;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartProducts = cartProducts;
        totalAmount = 0;
        for (CartProduct cartProduct : cartProducts)
            totalAmount += cartProduct.getCurrentPrice();
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Body.LEAVE_LINE_BELOW, "Chosen products:-"));
        body.add(new BodyBuilder(new ListView<CartProduct>(cartProducts, MAX_WIDTH)
                .buildView((cartProduct) -> new String[] {
                        "Name     : " + cartProduct.getName(),
                        "Quantity : " + cartProduct.getQuantity(),
                        "Price    : " + cartProduct.getCurrentPrice(),
                })));
        body.add(new BodyBuilder("Total amount = " + totalAmount));
        // TODO COUPONS
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialogMenu = new DialogMenu(
                    Option.EDIT_CART_PRODUCT,
                    Option.PROCEED_TO_BUY,
                    Option.GO_BACK);
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case EDIT_CART_PRODUCT:
                    int i = AppConsole.getInt("Enter product serial no. listed above to edit it", 1,
                            cartProducts.size());
                    ProductCatalog catalog = new ProductCatalog(customer, userRepository, productRepository,
                            cartProducts.get(i - 1));
                    catalog.buildAndRun();
                    userRepository.updateCartProduct(customer, cartProducts.get(i));
                    break;
                case PROCEED_TO_BUY:
                    PaymentPage paymentPage = new PaymentPage(customer, userRepository, totalAmount);
                    paymentPage.buildAndRun();
                    if (paymentPage.getIsPaymentSuccessfull()) {
                        for (CartProduct cartProduct : cartProducts)
                            customer.addNewPurchasedProducts(cartProduct);
                        userRepository.updatePurchasedProducts(customer);
                    }
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("Default case for " + option);
                    AppConsole.waitUntilReturn();
                    break loop;
            }
        } while (true);
    }

    // private void applyCoupon() {
    //     // TODO
    // }

}
