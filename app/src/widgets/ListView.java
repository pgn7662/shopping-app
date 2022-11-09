package widgets;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import app.AppConsole;

public class ListView<T> {
    private List<T> items;
    private int initialIndex, finalIndex;
    private int maxWidth;

    public ListView(List<T> items, int maxWidth) {
        this(items, 0, items.size() - 1, maxWidth);
    }

    public ListView(List<T> items, int initialIndex, int finalIndex, int maxWidth) {
        this.items = items;
        this.initialIndex = initialIndex;
        this.maxWidth = maxWidth;
    }

    public List<String> buildView() {
        return buildView((listItem) -> listItem.toString().split(System.lineSeparator()));
    }

    public List<String> buildView(Function<T, String[]> by) {
        List<String> lines = new LinkedList<>();
        String firstLinePrefix = "%" + noOfDigits(items.size() + initialIndex) + "d. %s";
        String linePrefix = firstLinePrefix.replaceFirst("d.", "c ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = initialIndex; i <= finalIndex; i++) {
            boolean first = true;
            for (String obj : by.apply(this.items.get(i))) {
                if (obj == null || obj.isEmpty())
                    continue;
                String str = String.format(first ? firstLinePrefix : linePrefix, first ? i + 1 : ' ', obj);
                if (first)
                    first = false;
                while (str.length() > maxWidth) {
                    lines.add(str.substring(0, maxWidth));
                    str = String.format(linePrefix, ' ', str.substring(maxWidth));
                }
                if (!str.isEmpty())
                    lines.add(str);
                lines.add(" ");
            }
            stringBuilder.append(System.lineSeparator());
        }
        return lines;
    }

    public void updateListItems(List<T> items) {
        this.items = items;
    }

    public void setInitialIndex(int initialIndex) {
        this.initialIndex = initialIndex;
    }

    private int noOfDigits(int num) {
        int digits = 0;
        for (int size = items.size() + 1; size > 0; size /= 10)
            digits++;
        return digits;
    }

    public T selectItemByUser(String message) {
        if (items.isEmpty())
            return null;
        int itemNo = AppConsole.getInt(message, initialIndex + 1, finalIndex + 1);
        return itemNo == 0 ? null : items.get(itemNo - 1);
    }

    public int selectItemIndexByUser(String message) {
        if (items.isEmpty())
            return -1;
        return AppConsole.getInt(message, initialIndex + 1, finalIndex + 1);
    }

}
