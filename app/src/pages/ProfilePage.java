package pages;

import java.util.LinkedList;
import java.util.List;

import Models.Address;
import Models.User;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.Option;

public class ProfilePage extends Page {
    private User user;
    private UserRepository userRepository;

    private Long phone;
    Integer zipcode;
    private String name, email, password, addressLineOne, addressLineTwo, area, city, state, country, landmark;

    private boolean showPassword;

    public ProfilePage(User user, UserRepository userRepository) {
        this.user = user;
        this.userRepository = userRepository;
        showPassword = false;
        reset();
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.CENTER, "Profile page of " + user.getName()));
        body.add(new BodyBuilder(Alignment.FILL, Body.LEAVE_LINE_BELOW, "-"));
        body.add(new BodyBuilder("Name             : " + (name != null ? name : "*"),
                "Email            : " + (email != null ? email : "*"),
                "Phone            : " + (phone != null ? phone : "*"),
                "Password         : " + (password != null && showPassword ? password : "*"),
                "Address:- ",
                "Address line one : " + (addressLineOne != null ? addressLineOne : ""),
                "Address line two : " + (addressLineTwo != null ? addressLineTwo : ""),
                "Area             : " + (area != null ? area : ""),
                "City             : " + (city != null ? city : ""),
                "state            : " + (state != null ? state : ""),
                "Country          : " + (country != null ? country : ""),
                "Zipcode          : " + (zipcode != null ? zipcode : ""),
                "Landmark         : " + (landmark != null ? landmark : "")));
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            DialogMenu dialog = new DialogMenu(
                    // Option.EDIT_EMAIL,
                    Option.EDIT_NAME, Option.EDIT_PHONE, Option.EDIT_PASSWORD,
                    showPassword ? Option.HIDE_PASSWORD : Option.SHOW_PASSWORD,
                    Option.EDIT_ADDRESS_LINE_ONE, Option.EDIT_ADDRESS_LINE_TWO,
                    Option.EDIT_AREA, Option.EDIT_CITY, Option.EDIT_STATE, Option.EDIT_COUNTRY, Option.EDIT_ZIPCODE,
                    Option.EDIT_LANDMARK,
                    Option.RESET,
                    Option.SAVE,
                    Option.CLEAR_ALL,
                    Option.GO_BACK);
            System.out.println(dialog);
            switch (dialog.getUserChoice()) {
                case CLEAR_ALL:
                    break;
                case EDIT_ADDRESS_LINE_ONE:
                    addressLineOne = AppConsole.getLine("Enter address line one");
                    break;
                case EDIT_ADDRESS_LINE_TWO:
                    addressLineTwo = AppConsole.getLine("Enter address line two");
                    break;
                case EDIT_AREA:
                    area = AppConsole.getLine("Enter area");
                    break;
                case EDIT_CITY:
                    city = AppConsole.getLine("Enter city");
                    break;
                case EDIT_COUNTRY:
                    country = AppConsole.getLine("Enter country");
                    break;
                case EDIT_LANDMARK:
                    landmark = AppConsole.getLine("Enter landmark");
                    break;
                case EDIT_NAME:
                    name = AppConsole.getUserName("Enter name");
                    break;
                case EDIT_PASSWORD:
                    // TODO enter old password
                    editPassword();
                    break;
                case EDIT_PHONE:
                    phone = AppConsole.getPhoneNumber("Enter phone");
                    break;
                case EDIT_STATE:
                    state = AppConsole.getLine("Enter state");
                    break;
                case EDIT_ZIPCODE:
                    zipcode = Integer.parseInt(AppConsole.getLine("Enter zipcode"));
                    break;
                case GO_BACK:
                    break loop;
                case HIDE_PASSWORD:
                    showPassword = false;
                    break;
                case RESET:
                    reset();
                    break;
                case SAVE:
                    if (isDifferent()) {
                        updateUser();
                        if (userRepository.updateUser(user))
                            System.out.println("Updated successfully!");
                        else
                            System.out.println("Oops, some technical issue on our side!.. try again later");
                    } else
                        System.out.println("There is no change to update");
                    AppConsole.waitUntilReturn();
                    break;
                case SHOW_PASSWORD:
                    showPassword = true;
                    break;
                default:
                    break;
            }
        } while (true);
    }

    private void reset() {
        name = user.getName();
        email = user.getEmail();
        password = user.getPassword();
        phone = user.getPhoneNumber();
        Address address = user.getAddress();
        addressLineOne = address.getAddressLineOne();
        addressLineTwo = address.getAddressLineTwo();
        area = address.getArea();
        city = address.getCity();
        state = address.getState();
        country = address.getCountry();
        zipcode = address.getZipcode();
        landmark = address.getLandmark();
    }

    private boolean isDifferent() {
        return !(name.equals(user.getName()) &&
                password.equals(user.getPassword()) &&
                phone.equals(user.getPhoneNumber()) &&
                addressLineOne.equals(user.getAddress().getAddressLineOne()) &&
                addressLineTwo.equals(user.getAddress().getAddressLineTwo()) &&
                area.equals(user.getAddress().getArea()) &&
                city.equals(user.getAddress().getCity()) &&
                state.equals(user.getAddress().getState()) &&
                country.equals(user.getAddress().getCountry()) &&
                zipcode.equals(user.getAddress().getZipcode()) &&
                landmark.equals(user.getAddress().getLandmark()));
    }

    private void updateUser() {
        user.setName(name);
        user.setPassword(password);
        user.setPhoneNumber(phone);
        Address address = user.getAddress();
        address.setAddressLineOne(addressLineOne);
        address.setAddressLineTwo(addressLineTwo);
        address.setArea(area);
        address.setCity(city);
        address.setState(state);
        address.setCountry(country);
        address.setZipcode(zipcode);
        address.setLandmark(landmark);
    }

    private void editPassword() {
        do {
            String pwd = AppConsole.getPassword("Enter new password");
            String cPwd = AppConsole.getPassword("Confirm Password");
            if (pwd.equals(cPwd)) {
                password = pwd;
                return;
            } else {
                System.out.println("Oops, confirm password doesn't match new password!");
                System.out.println();
            }
        } while (true);
    }
}
