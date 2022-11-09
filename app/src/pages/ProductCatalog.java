package pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Models.Admin;
import Models.CartProduct;
import Models.Customer;
import Models.DetailTag;
import Models.Product;
import Models.User;
import Models.ValueTag;
import app.AppConsole;
import app.helpers.Pair;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import layout.StringAlignment;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.Option;

public class ProductCatalog extends Page {
    private User user;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private Product product;

    public ProductCatalog(User user, UserRepository userRepository,
            ProductRepository productRepository, Product product, boolean editable) {
        this.user = user;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.product = product;
    }

    public ProductCatalog(User user, UserRepository userRepository,
            ProductRepository productRepository, Product product) {
        this(user, userRepository, productRepository, product, false);
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.CENTER, "Product Catalog"));
        body.add(new BodyBuilder(Body.FILL, "-"));
        body.add(new BodyBuilder(Alignment.CENTER, Body.LEAVE_LINE_ABOVE | Body.LEAVE_LINE_BELOW, product.getName()));
        body.add(new BodyBuilder("Price: " + product.getCurrentPrice()
                + (product.getDiscountPercent() != 0 ? "(" + product.getDiscountPercent() + "% off)" : ""),
                "Mrp: " + product.getMrp(),
                "Quantity Available: " + product.getQuantity()));
        if (product.getInfo() != null || !product.getInfo().isEmpty())
            body.add(new BodyBuilder("Info:", product.getInfo()));
        body.add(new BodyBuilder("Category: " + product.getCategory()));
        if (!product.getValueTags().isEmpty()) {
            body.add(new BodyBuilder(Alignment.CENTER, Body.LEAVE_LINE_ABOVE, "('*' means that value is chosen)"));
            List<String> tags = new LinkedList<>();
            for (Pair<ValueTag<String>, String> pair : product.getValueTags()) {
                StringBuilder stringBuilder = new StringBuilder(pair.getFirst().getName()).append(" : ");
                for (String value : pair.getFirst().getValues()) {
                    stringBuilder.append(value);
                    if (value.equals(pair.getSecond()))
                        stringBuilder.append("*");
                    stringBuilder.append(" | ");
                }
                stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
                tags.add(stringBuilder.toString());
            }
            body.add(new BodyBuilder(tags));
        }
        if (!product.getDetailTags().isEmpty()) {
            body.add(new BodyBuilder(Alignment.CENTER, Body.LEAVE_LINE_ABOVE, "Other specifications and details"));
            body.add(new BodyBuilder(
                    StringAlignment.<DetailTag>tree(product.getDetailTags(), (d) -> d.getSubDetailTags(), (d) -> {
                        StringBuilder stringBuilder = new StringBuilder(d.getName()).append(" : ");
                        for (String value : d.getValues())
                            stringBuilder.append(value).append(", ");
                        return stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length()).toString();
                    }, false, 2, ' ', MAX_WIDTH)));
        }
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            System.out.println(buildLayout());
            DialogMenu dialogMenu = getDialog();
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case ADD_TO_CART:
                    ((Customer) user).getCart().addCartProduct(new CartProduct(product));
                    userRepository.updateCart((Customer) user);
                    break;
                case ADD_TO_WISHLIST:
                    ((Customer) user).getWishList().addProduct(product);
                    userRepository.updateWishlist((Customer) user);
                    break;
                case BUY_PRODUCT:
                    List<CartProduct> cartProducts = new LinkedList<>();
                    cartProducts.add(new CartProduct(product));
                    CheckOutPage checkOutPage = new CheckOutPage((Customer) user,
                            userRepository, productRepository, cartProducts);
                    checkOutPage.buildAndRun();
                    // TODO
                    break;
                case EDIT_NAME:
                    product.setName(AppConsole.getLine("Enter product name"));
                    break;
                case EDIT_MRP:
                    product.setMrp(AppConsole.getDouble("Enter mrp", 0.1, Double.MAX_VALUE));
                    break;
                case EDIT_DISCOUNT_PERCENT:
                    product.setDiscountPercent(AppConsole.getInt("Enter discount percent", 0, 100));
                    break;
                case EDIT_QUANTITY:
                    product.setQuantity(AppConsole.getInt("Enter quantity", 0, Integer.MAX_VALUE));
                    break;
                case EDIT_DETAIL_TAG:
                    String dtag = AppConsole.getLine("Enter the detail tag name you want to edit").trim();
                    String[] dvalues = AppConsole.getLine("Enter the values as comma seperated strings").split(",");
                    product.setValuesForDetailTag(dtag, Arrays.asList(dvalues));
                    // TODO check empty check availabity is already present
                    break;
                case EDIT_VALUE_TAG:
                    String vtag = AppConsole.getLine("Enter the value tag name you want to edit").trim();
                    String[] vvalues = AppConsole.getLine("Enter the values as comma seperated strings").split(",");
                    product.setValuesForValueTag(vtag, Arrays.asList(vvalues));
                    // TODO
                    break;
                case SELECT_VALUE_FOR_VALUE_TAG:
                    String vvtag = AppConsole.getLine("Enter the value tag name you want to edit").trim();
                    String vvvalues = AppConsole.getLine("Enter the value present for the value tag").trim();
                    for (Pair<ValueTag<String>, String> pair : product.getValueTags())
                        if (pair.getFirst().getName().equals(vvtag))
                            product.setValueForValueTag(pair.getFirst(), vvvalues);
                    // TODO
                    break;
                case ADD_NEW_PRODUCT:
                    productRepository.addProduct(product);
                    break;
                case SAVE:
                    productRepository.updateProduct(product);
                    break;
                case RESET:
                    break;
                case CLEAR_ALL:
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("In default for option " + option);
                    AppConsole.waitUntilReturn();
                    break;
            }
        } while (true);
    }

    private DialogMenu getDialog() {
        List<Option> options = new LinkedList<>();
        options.add(Option.EDIT_QUANTITY);
        if (!product.getValueTags().isEmpty())
            options.add(Option.SELECT_VALUE_FOR_VALUE_TAG);
        if (user != null) {
            if (user instanceof Customer) {
                options.add(Option.ADD_TO_CART);
                options.add(Option.ADD_TO_WISHLIST);
                options.add(Option.BUY_PRODUCT);
            } else if (user instanceof Admin) {
                options.add(Option.EDIT_NAME);
                options.add(Option.EDIT_MRP);
                options.add(Option.EDIT_DISCOUNT_PERCENT);
                options.add(Option.EDIT_DETAIL_TAG);
                options.add(Option.EDIT_VALUE_TAG);
                options.add(Option.ADD_NEW_PRODUCT);
                options.add(Option.SAVE);
                // TODO
                // options.add(Option.RESET);
                // options.add(Option.CLEAR_ALL);
            }
        }
        options.add(Option.GO_BACK);
        return new DialogMenu(options.toArray(new Option[options.size()]));
    }

}
