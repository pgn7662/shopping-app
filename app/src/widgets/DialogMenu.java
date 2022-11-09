package widgets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import app.AppConsole;
import layout.Alignment;
import layout.BodyBuilder;
import layout.Layout;

public final class DialogMenu extends Layout {
    private Option[] options;
    private String dialogPicture;

    public DialogMenu(Option... options) {
        this.options = options;
    }

    @Override
    public String getBorder() {
        return "-";
    }

    @Override
    public int getHorizontalPaddLength() {
        return 4;
    }

    @Override
    public int getVerticalPaddLength() {
        return 2;
    }

    @Override
    public char getPaddChar() {
        return ' ';
    }

    @Override
    protected List<BodyBuilder> getBodyBuilders() {
        List<BodyBuilder> list = new ArrayList<>(1);
        List<String> sOptions = new LinkedList<>();
        for (int i = 0; i < options.length; i++)
            sOptions.add(enumToCapitalizedString(i + 1, options[i]));
        // for (int i = options.length - 1, k = 1; i > 0; i--, k += 2)
        //     sOptions.add(k, " ");
        list.add(new BodyBuilder(Alignment.LEFT, sOptions));
        return list;
    }

    private String enumToCapitalizedString(int k, Option item) {
        String name = item.name();
        StringBuilder stringBuilder = new StringBuilder().append(k).append(". ").append(name.charAt(0));
        for (int i = 1; i < name.length(); i++)
            stringBuilder.append(Character.isAlphabetic(name.charAt(i)) ? Character.toLowerCase(name.charAt(i)) : ' ');
        return stringBuilder.toString();
    }

    public Option getUserChoice(String message) {
        return options[AppConsole.getInt(message, 1, options.length) - 1];
    }

    public Option getUserChoice() {
        return getUserChoice("Your option");
    }

    @Override
    public String toString() {
        if (dialogPicture == null)
            dialogPicture = buildLayout();
        return dialogPicture;
    }

}
