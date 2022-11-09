package pages;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import Models.Admin;
import Models.Customer;
import Models.Product;
import Models.User;
import app.AppConsole;
import app.helpers.ProductSearcher;
import app.helpers.Triple;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public final class HomePage extends Page {
    public static final int MAX_PRODUCTS_VIEW_COUNT = 10;

    public static int productsViewCount;

    static {
        productsViewCount = 5;
    }

    private User user;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    private Stack<Triple<String, List<Product>, Integer>> pageStack;

    public HomePage(User user, UserRepository userRepository, ProductRepository productRepository) {
        this.user = user;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.pageStack = new Stack<>();
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.SPACE_BETWEEN, "RV Shopping", user != null ? user.getName() : ""));
        body.add(new BodyBuilder(Body.FILL | Body.LEAVE_LINE_BELOW, "-"));
        if (pageStack.isEmpty())
            return body;
        Triple<String, List<Product>, Integer> page = pageStack.peek();
        if (page.getFirst() != null) {
            body.add(new BodyBuilder(page.getFirst().equals(Option.VIEW_VIEWED_PRODUCTS.name()) ? "Viewed Products:-"
                    : "Showing results for '" + page.getFirst() + "'"));
        }
        List<Product> products = page.getSecond();
        if (products.isEmpty()) {
            body.add(new BodyBuilder("Oops, there are no results / products for your search!.."));
            return body;
        }
        int startIndex = page.getThird(), finalIndex = Math.min(startIndex + productsViewCount, products.size());
        body.add(new BodyBuilder(Alignment.RIGHT, Body.LEAVE_LINE_BELOW,
                "Showing " + startIndex + " - " + finalIndex + " of " + products.size()));
        body.add(new BodyBuilder(new ListView<>(products, startIndex, finalIndex, MAX_WIDTH)
                .buildView((product) -> new String[] {
                        product.getName(),
                        // TODO rating
                        "Price: " + product.getCurrentPrice()
                                + (product.getDiscountPercent() != 0
                                        ? "(" + product.getDiscountPercent() + "% off)"
                                        : ""),
                        "Mrp: " + product.getMrp(),
                        product.getQuantity() > 0 ? "In stock" : "Out of stock",
                })));
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            System.out.println(buildLayout());
            DialogMenu dialog = getDialogMenu();
            System.out.println(dialog);
            Option option = dialog.getUserChoice();
            switch (option) {
                case ADD_NEW_PRODUCT:
                    // ProductCatalog productCatalog = new ProductCatalog();
                    break;
                case VIEW_DELIVERIES_PENDING:
                    break;
                case SEARCH_PRODUCTS:
                    String search = AppConsole.getLine("Search about");
                    List<Product> products = ProductSearcher.filter(productRepository.getAllProducts(), search);
                    pageStack.push(new Triple<>(search, products, 0));
                    break;
                case SELECT_A_PRODUCT_LISTED_ABOVE:
                    int startIndex = pageStack.peek().getThird(),
                            finalIndex = Math.min(startIndex + productsViewCount, pageStack.peek().getSecond().size());
                    int i = AppConsole.getInt("Give the serial no.", startIndex + 1, finalIndex + 1);
                    Product product = pageStack.peek().getSecond().get(i - 1);
                    ProductCatalog productCatalog = new ProductCatalog(user, userRepository, productRepository,
                            product);
                    productCatalog.buildAndRun();
                    ((Customer) user).addViewedProducts(product);
                    break;
                case GO_TO_NEXT_PAGE:
                    pageStack.peek().setThird(pageStack.peek().getThird() + productsViewCount);
                    break;
                case GO_TO_PREVEOUS_PAGE:
                    pageStack.peek().setThird(pageStack.peek().getThird() - productsViewCount);
                    break;
                case VIEW_CART:
                    CartPage cartPage = new CartPage((Customer) user, userRepository, productRepository);
                    cartPage.buildAndRun();
                    // TODO cartPage
                    break;
                case VIEW_WISHLIST:
                    WishListPage wishListPage = new WishListPage(((Customer) user), userRepository, productRepository);
                    wishListPage.buildAndRun();
                    break;
                case VIEW_VIEWED_PRODUCTS:
                    pageStack.push(new Triple<>(Option.VIEW_VIEWED_PRODUCTS.name(),
                            ((Customer) user).getViewedProducts(), 0));
                    break;
                case VIEW_PROFILE:
                    ProfilePage profilePage = new ProfilePage(user, userRepository);
                    profilePage.buildAndRun();
                    break;
                case HOME:
                    pageStack.clear();
                    pageStack.push(new Triple<>(null, productRepository.getAllProducts(), 0));
                    break;
                case SIGN_IN:
                    SignInPage signInPage = new SignInPage(userRepository, productRepository);
                    signInPage.buildAndRun();
                    this.user = signInPage.getUser();
                    break;
                case SIGN_OUT:
                    if (AppConsole.getYesOrNoAsBoolean("Do you really want to sign out? [y/n]"))
                        this.user = null;
                    break;
                case APP_SETTINGS:
                    // TODO
                    break;
                case GO_BACK:
                    if (!pageStack.isEmpty())
                        pageStack.pop();
                    break;
                case EXIT_APPLICATION:
                    break loop;
                default:
                    System.out.println("In default case for option " + option);
                    AppConsole.waitUntilReturn();
                    break loop;
            }
        } while (true);
    }

    public DialogMenu getDialogMenu() {
        List<Option> options = new LinkedList<>();
        options.add(Option.SEARCH_PRODUCTS);
        if (!pageStack.isEmpty() && !pageStack.peek().getSecond().isEmpty()) {
            options.add(Option.SELECT_A_PRODUCT_LISTED_ABOVE);
            if (pageStack.peek().getThird() + 1 - productsViewCount > 0)
                options.add(Option.GO_TO_PREVEOUS_PAGE);
            if (pageStack.peek().getThird() + 1 + productsViewCount <= pageStack.peek().getSecond().size())
                options.add(Option.GO_TO_NEXT_PAGE);
        }
        if (user == null) {
            options.add(Option.SIGN_IN);
        } else {
            if (user instanceof Customer) {
                options.add(Option.VIEW_CART);
                options.add(Option.VIEW_WISHLIST);
                if (!pageStack.peek().getFirst().equals(Option.VIEW_VIEWED_PRODUCTS.name()))
                    options.add(Option.VIEW_VIEWED_PRODUCTS);
            } else if (user instanceof Admin) {
                options.add(Option.ADD_NEW_PRODUCT);
                options.add(Option.VIEW_DELIVERIES_PENDING);
            }
            options.add(Option.HOME);
            options.add(Option.VIEW_PROFILE);
            options.add(Option.APP_SETTINGS);
            options.add(Option.SIGN_OUT);
        }
        if (pageStack.size() > 1)
            options.add(Option.GO_BACK);
        options.add(Option.EXIT_APPLICATION);
        return new DialogMenu(options.toArray(new Option[options.size()]));
    }

}
