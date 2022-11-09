package pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Models.Admin;
import Models.Delivery;
import Models.DeliveryStatus;
import Models.User;
import app.AppConsole;
import layout.BodyBuilder;
import repositories.ProductRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public class DeliveryPage extends Page {
    private User user;
    private Delivery delivery;
    private ProductRepository productRepository;

    public DeliveryPage(User user, Delivery delivery, ProductRepository productRepository) {
        this.user = user;
        this.delivery = delivery;
        this.productRepository = productRepository;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder("Products to delivery:-"));
        body.add(new BodyBuilder(new ListView<>(delivery.getCartProducts(), MAX_WIDTH)
                .buildView((cartProduct) -> new String[] { "Name: " + cartProduct.getName(),
                        "Quantity: " + cartProduct.getQuantity(),
                })));
        body.add(new BodyBuilder("Delivery to the customer " + delivery.getCustomer().getName()));
        body.add(new BodyBuilder("Status " + delivery.getDeliveryStatus()));
        return body;
    }

    @Override
    public void buildAndRun() {
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialogMenu = getDialogMenu();
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case EDIT_DELIVERY_STATUS:
                    ListView<DeliveryStatus> listView = new ListView<>(Arrays.asList(DeliveryStatus.values()),
                            MAX_WIDTH);
                    for (String line : listView.buildView())
                        System.out.println(line);
                    int i = listView.selectItemIndexByUser("Select any by serial no. of the status");
                    delivery.setDeliveryStatus(DeliveryStatus.values()[i]);
                    productRepository.updateDeliveryStatus(delivery);
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("In default case = " + option);
                    break loop;
            }
        } while (true);
    }

    private DialogMenu getDialogMenu() {
        List<Option> options = new LinkedList<>();
        if (user != null) {
            if (user instanceof Admin) {
                options.add(Option.EDIT_DELIVERY_STATUS);
            }
        }
        options.add(Option.GO_BACK);
        return new DialogMenu(new Option[options.size()]);
    }
}
