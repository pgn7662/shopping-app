package pages;

import java.util.LinkedList;
import java.util.List;

import Models.Customer;
import Models.WishList;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public class WishListPage extends Page {
    private Customer customer;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public WishListPage(Customer customer, UserRepository userRepository, ProductRepository productRepository) {
        this.customer = customer;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.SPACE_BETWEEN, "Wishlist", customer.getName()));
        body.add(new BodyBuilder(Body.FILL, "-"));
        if (customer.getWishList().getProducts().isEmpty()) {
            body.add(new BodyBuilder(Alignment.CENTER, "Oops, there is no products in your wishlist!"));
        } else {
            body.add(new BodyBuilder(Body.LEAVE_LINE_ABOVE,
                    new ListView<>(customer.getWishList().getProducts(), MAX_WIDTH).buildView(
                            (product) -> new String[] {
                                    product.getName(),
                            // TODO rating
                            })));
        }
        return body;
    }

    @Override
    public void buildAndRun() {
        WishList wishList = customer.getWishList();
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialogMenu = new DialogMenu(
                    Option.SELECT_A_PRODUCT_LISTED_ABOVE,
                    Option.REMOVE_PRODUCT_FROM_WISHLIST,
                    Option.GO_BACK);
            if (wishList.getProducts().isEmpty()) {
                dialogMenu = new DialogMenu(Option.GO_BACK);
            }
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case SELECT_A_PRODUCT_LISTED_ABOVE:
                    int i = AppConsole.getInt("Enter product serial no. listed above to select it (or enter "
                            + wishList.getProducts().size() + 1 + " to go back)", 1, wishList.getProducts().size() + 1);
                    ProductCatalog productCatalog = new ProductCatalog(customer, userRepository,
                            productRepository, wishList.getProducts().get(i - 1));
                    productCatalog.buildAndRun();
                    break;
                case REMOVE_PRODUCT_FROM_WISHLIST:
                    int ri = AppConsole.getInt("Enter product serial no. listed above to remove it (or enter "
                            + wishList.getProducts().size() + 1 + " to go back)", 1, wishList.getProducts().size() + 1);
                    wishList.removeProduct(ri - 1);
                    userRepository.updateWishlist(customer);
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("Default case " + option);
                    AppConsole.waitUntilReturn();
                    break loop;
            }
        } while (true);
    }

}
