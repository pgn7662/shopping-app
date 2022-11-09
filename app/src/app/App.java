package app;

import Models.User;
import databases.UserDatabase;
import pages.HomePage;
import pages.SignInPage;
import pages.SignUpPage;
import repositories.ProductRepository;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.Option;

public class App {
    private static DialogMenu appOpenDialogMenu = new DialogMenu(
            Option.SIGN_IN,
            Option.SIGN_UP,
            Option.VIEW_PRODUCTS,
            Option.EXIT_APPLICATION);

    public static void main(String[] args) {
        // TODO: start up page about this shopping app
        UserRepository userRepository = UserDatabase.getInstance();
        ProductRepository productRepository = null;
        User user = null;
        loop: do {
            AppConsole.clearScreen();
            System.out.println(appOpenDialogMenu);
            Option option = appOpenDialogMenu.getUserChoice();
            switch (option) {
                case SIGN_IN:
                    SignInPage signInPage = new SignInPage(userRepository, productRepository);
                    signInPage.buildAndRun();
                    user = signInPage.getUser();
                case SIGN_UP:
                    if (option.equals(Option.SIGN_UP)) {
                        SignUpPage signUpPage = new SignUpPage(userRepository);
                        signUpPage.buildAndRun();
                        user = signUpPage.getUser();
                        System.out.println(user);
                    }
                case VIEW_PRODUCTS:
                    if (option.equals(Option.VIEW_PRODUCTS) || user != null) {
                        HomePage homePage = new HomePage(user, userRepository, productRepository);
                        homePage.buildAndRun();
                    }
                    break;
                case EXIT_APPLICATION:
                default:
                    break loop;
            }
        } while (true);
    }

}