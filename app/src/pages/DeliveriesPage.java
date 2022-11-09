package pages;

import java.util.LinkedList;
import java.util.List;

import Models.Delivery;
import Models.User;
import app.AppConsole;
import layout.Alignment;
import layout.Body;
import layout.BodyBuilder;
import repositories.ProductRepository;
import widgets.DialogMenu;
import widgets.ListView;
import widgets.Option;

public class DeliveriesPage extends Page {
    private User user;
    private List<Delivery> deliveries;
    private ProductRepository productRepository;

    public DeliveriesPage(User user, List<Delivery> deliveries, ProductRepository productRepository) {
        this.user = user;
        this.deliveries = deliveries;
        this.productRepository = productRepository;
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> body = new LinkedList<>();
        body.add(new BodyBuilder(Alignment.CENTER, "Deliveries"));
        body.add(new BodyBuilder(Body.FILL, "-"));
        if (deliveries.isEmpty()) {
            body.add(new BodyBuilder(Alignment.CENTER, "There are no deliveries!"));
        } else {
            body.add(new BodyBuilder(new ListView<>(deliveries, MAX_WIDTH).buildView((d) -> new String[] {
                    "To: " + d.getCustomer().getName(),
                    "Date: " + d.getDate(),
                    "Status: " + d.getDeliveryStatus(),
            })));
        }
        return body;
    }


    @Override
    public void buildAndRun() {
        loop: do {
            AppConsole.clearScreen();
            System.out.println(buildLayout());
            DialogMenu dialogMenu = new DialogMenu(
                    Option.SELECT_A_DELIVERY,
                    Option.GO_BACK);
            System.out.println(dialogMenu);
            Option option = dialogMenu.getUserChoice();
            switch (option) {
                case SELECT_A_DELIVERY:
                    int i = AppConsole.getInt("Enter the serial no. of the delivery listed above (or "
                            + (deliveries.size() + 1) + " to go back)", 1, deliveries.size() + 1);
                    DeliveryPage deliveryPage = new DeliveryPage(user, deliveries.get(i - 1), productRepository);
                    deliveryPage.buildAndRun();
                    break;
                case GO_BACK:
                    break loop;
                default:
                    System.out.println("Default case = " + option);
                    AppConsole.waitUntilReturn();
                    break loop;
            }
        } while (true);
    }

}
