package pages;

import java.util.LinkedList;
import java.util.List;

import Models.Customer;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.UserRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public class PaymentPage extends Page {
    private boolean isPaymentSuccessfull;

    private Customer customer;
    private UserRepository userRepository;
    private double amountToPay;

    public PaymentPage(Customer customer, UserRepository userRepository, double amountToPay) {
        this.customer = customer;
        this.userRepository = userRepository;
        this.amountToPay = amountToPay;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.CENTER, "Payment Page"));
        body.add(new BodyBuilder(Body.FILL | Body.LEAVE_LINE_BELOW, "-"));
        body.add(new BodyBuilder(Alignment.SPACE_BETWEEN, "Total amount to pay = ", String.valueOf(amountToPay)));
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialogMenu = new DialogMenu(
                    Option.PAY_BY_CARD,
                    Option.PAY_BY_UPI,
                    Option.GO_BACK);
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case PAY_BY_CARD:
                    if (!customer.getCards().isEmpty()) {
                        ListView<String> cards = new ListView<>(customer.getCards(), MAX_WIDTH);
                        for (String line : cards.buildView())
                            System.out.println(line);
                        int i = AppConsole.getInt("Enter the no of card listed above (if new card then type "
                                + (customer.getCards().size() + 1) + " else 0 to cancel)", 0, customer.getCards().size() + 1);
                        if (i == 0)
                            break;
                        if (i == customer.getCards().size() + 1) {
                            String newCard = AppConsole.getLine("Enter new card number");
                            customer.addNewCardNumber(newCard);
                            userRepository.updateCustomerPaymentDetails(customer);
                        }
                        isPaymentSuccessfull = true;
                        System.out.println("Payment done succesfull");
                    }
                    break;
                case PAY_BY_UPI:
                    if (!customer.getUpis().isEmpty()) {
                        ListView<String> upis = new ListView<>(customer.getUpis(), MAX_WIDTH);
                        for (String line : upis.buildView())
                            System.out.println(line);
                        int i = AppConsole.getInt("Enter the no of upi listed above (if new card then type "
                                + (customer.getUpis().size() + 1) + " else 0 to cancel)", 1, customer.getUpis().size() + 1);
                        if (i == 0)
                            break;
                        if (i == customer.getUpis().size() + 1) {
                            String newUpi = AppConsole.getLine("Enter new upi id");
                            customer.addNewUpiId(newUpi);
                            userRepository.updateCustomerPaymentDetails(customer);
                        }
                        isPaymentSuccessfull = true;
                        System.out.println("Payment done succesfull");
                    }
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("Default case for " + option);
                    AppConsole.waitUntilReturn();
                    break loop;
            }
        } while (!isPaymentSuccessfull);
    }

    public boolean getIsPaymentSuccessfull() {
        return isPaymentSuccessfull;
    }

}
