package pages;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Models.Admin;
import Models.Customer;
import Models.Product;
import Models.User;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.ListView;

public class SignInPage extends Page {
    private User user;
    private String email;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public SignInPage(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
    }

    public User getUser() {
        return user;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> list = new ArrayList<>();
        list.add(new BodyBuilder(Alignment.CENTER, "SIGN IN", "-------"));
        if (email != null && !email.isEmpty()) {
            list.add(new BodyBuilder(Alignment.SPACE_BETWEEN, Body.LEAVE_LINE_ABOVE, "Email:", email));
        }
        return list;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            if (getEmailFromUser()) {
                getPasswordFromUser();
                if (user != null) {
                    AppConsole.clearScreen();
                    onLogBack();
                    break loop;
                }
            }
            if (AppConsole.getYesOrNoAsBoolean("Do you want to sign up [y/n]")) {
                SignUpPage signUpPage = new SignUpPage(userRepository);
                signUpPage.buildAndRun();
                this.user = signUpPage.getUser();
                AppConsole.clearScreen();
            }
        } while (true);
    }

    private void onLogBack() {
        if (user instanceof Customer) {
            Customer customer = (Customer) user;
            List<Product> products = new LinkedList<>();
            for (Product product : customer.getWishList().getProducts()) {
                if (product.getQuantity() > 0)
                    products.add(product);
            }
            if (!products.isEmpty()) {
                System.out.println(customer.getName() + ", there are arrivals of products that are in your wishlist!");
                if (AppConsole.getYesOrNoAsBoolean("Do you want to see them first [y/n]")) {
                    ListView<Product> listView = new ListView<>(products, MAX_WIDTH);
                    for (String line : listView.buildView())
                        System.out.println(line);
                    AppConsole.waitUntilReturn();
                }
            }
        } else if (user instanceof Admin) {
            Admin admin = (Admin) user;
            List<Product> products = new LinkedList<>();
            for (Product product : productRepository.getAllProducts())
                if (product.getQuantity() <= 0)
                    products.add(product);
            if (!products.isEmpty()) {
                System.out.println(admin.getName() + ", there are products that are in out of stock!");
                if (AppConsole.getYesOrNoAsBoolean("Do you want to see them first [y/n]")) {
                    ListView<Product> listView = new ListView<>(products, MAX_WIDTH);
                    for (String line : listView.buildView())
                        System.out.println(line);
                    AppConsole.waitUntilReturn();
                }
            }
        }
    }

    private boolean getEmailFromUser() {
        String layout = buildLayout();
        do {
            AppConsole.clearScreen();
            System.out.println(layout);
            String userEmail = AppConsole.getLine("Enter email").trim();
            if (userRepository.isUserEmailPresent(userEmail)) {
                this.email = userEmail;
                return true;
            }
        } while (AppConsole.getYesOrNoAsBoolean("Do you want to try again [y/n]"));
        return false;
    }

    private void getPasswordFromUser() {
        String layout = buildLayout();
        do {
            AppConsole.clearScreen();
            System.out.println(layout);
            String password = AppConsole.getPassword("Enter password");
            User u = userRepository.getUser(email, password);
            if (u != null) {
                this.user = u;
                return;
            }
        } while (AppConsole.getYesOrNoAsBoolean("Do you want to try again [y/n]"));
    }

}
