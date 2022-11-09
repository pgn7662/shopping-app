package pages;

import java.util.LinkedList;
import java.util.List;

import Models.Customer;
import Models.User;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.Option;

public class SignUpPage extends Page {
    private User user;
    private UserRepository userRepository;
    private String name, email, password;
    private Long phone;

    private boolean showPassword;

    public SignUpPage(UserRepository repository) {
        this.userRepository = repository;
        showPassword = false;
    }

    @Override
    public String getBorder() {
        return "*";
    }

    @Override
    public int getHorizontalPaddLength() {
        return 5;
    }

    @Override
    public int getVerticalPaddLength() {
        return 2;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.CENTER, "Sign Up as Customer"));
        body.add(new BodyBuilder(Alignment.FILL, Body.LEAVE_LINE_BELOW, "-"));
        body.add(new BodyBuilder("Name     : " + (name != null ? name : ""),
                "Email    : " + (email != null ? email : ""),
                "Phone    : " + (phone != null ? phone : ""),
                "Password : " + (password != null && showPassword ? password : "")));
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialog = new DialogMenu(
                    Option.EDIT_NAME,
                    Option.EDIT_EMAIL,
                    Option.EDIT_PHONE,
                    Option.EDIT_PASSWORD,
                    showPassword ? Option.HIDE_PASSWORD : Option.SHOW_PASSWORD,
                    Option.CREATE_ACCOUNT,
                    Option.CLEAR_ALL,
                    Option.GO_BACK);
            System.out.println(dialog);
            switch (dialog.getUserChoice()) {
                case EDIT_NAME:
                    name = AppConsole.getUserName("Enter your Name");
                    break;
                case EDIT_EMAIL:
                    email = AppConsole.getEmail("Enter your Email");
                    break;
                case EDIT_PHONE:
                    phone = AppConsole.getPhoneNumber("Enter your phone");
                    break;
                case EDIT_PASSWORD:
                    editPassword();
                    break;
                case SHOW_PASSWORD:
                    showPassword = true;
                    break;
                case HIDE_PASSWORD:
                    showPassword = false;
                    break;
                case CREATE_ACCOUNT:
                    if (createAccount()) {
                        System.out.println("Account created successfully!");
                        System.out.println();
                        AppConsole.waitUntilReturn();
                        break loop;
                    }
                    break;
                case CLEAR_ALL:
                    name = email = password = null;
                    phone = null;
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("Unimplemented (default in switch case in sign up page)...");
                    AppConsole.waitUntilReturn();
                    break loop;
            }
        } while (true);
    }

    public User getUser() {
        return user;
    }

    private boolean createAccount() {
        if (name != null && email != null && phone != null && password != null) {
            User user = userRepository.addUser(new Customer(name, email, password, phone, null));
            if (user != null) {
                this.user = user;
                return true;
            }
            System.out.println("Some technical issue in creating a new account!, come back later!");
        } else
            System.out.println("All the fields should be filled to create a account!");
        System.out.println();
        AppConsole.waitUntilReturn();
        return false;
    }

    private void editPassword() {
        do {
            String pwd = AppConsole.getPassword("Enter new password");
            String cPwd = AppConsole.getPassword("Confirm Password");
            if (pwd.equals(cPwd)) {
                this.password = pwd;
                return;
            } else {
                System.out.println("Oops, confirm password doesn't match new password!");
                System.out.println();
            }
        } while (true);
    }

}
