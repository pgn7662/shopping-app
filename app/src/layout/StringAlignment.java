package layout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public final class StringAlignment {
    public static List<String> align(Iterable<String> strings, char paddChar, int width, Alignment alignment) {
        switch (alignment) {
            case CENTER:
                return center(strings, paddChar, width);
            case LEFT:
                return left(strings, paddChar, width);
            case RIGHT:
                return right(strings, paddChar, width);
            case FILL:
                return fill(strings, paddChar, width);
            case SPACE_BETWEEN:
                return spaceBetween(strings, paddChar, width);
            case SPACE_EVENLY:
                return spaceEvenly(strings, paddChar, width);
        }
        return new LinkedList<>();
    }

    public static List<String> left(Iterable<String> strings, char paddChar, int width) {
        return align(strings, width, paddChar, (strLen) -> 0);
    }

    public static List<String> right(Iterable<String> strings, char paddChar, int width) {
        return align(strings, width, paddChar, (strLen) -> width - strLen);
    }

    public static List<String> center(Iterable<String> strings, char paddChar, int width) {
        return align(strings, width, paddChar, (strLen) -> (width - strLen) / 2);
    }

    public static List<String> fill(Iterable<String> strings, char paddChar, int width) {
        List<String> list = new LinkedList<>();
        for (String string : strings) {
            while (string.length() >= width) {
                list.add(string.substring(0, width));
                string = string.substring(width);
            }
            if (string == null || string.isEmpty())
                break;
            StringBuilder stringBuilder = new StringBuilder(string);
            while (stringBuilder.length() + string.length() <= width)
                stringBuilder.append(string);
            stringBuilder.append(stringBuilder.substring(0, width % string.length()));
            list.add(stringBuilder.toString());
        }
        return list;
    }

    private static List<String> align(
            Iterable<String> strings,
            int width,
            char paddChar,
            Function<Integer, Integer> getOffset) {
        List<String> lines = new LinkedList<>();
        for (String string : strings) {
            while (string.length() >= width) {
                lines.add(string.substring(0, width));
                string = string.substring(width);
            }
            if (string == null || string.isEmpty())
                continue;
            int offset = getOffset.apply(string.length());
            StringBuilder stringBuilder = new StringBuilder(width);
            while (stringBuilder.length() < width)
                stringBuilder.append(stringBuilder.length() == offset ? string : paddChar);
            lines.add(stringBuilder.toString());
        }
        return lines;
    }

    public static List<String> spaceBetween(Iterable<String> strings, char paddChar, int width) {
        return align(strings, paddChar, width, false);
    }

    public static List<String> spaceEvenly(Iterable<String> strings, char paddChar, int width) {
        return align(strings, paddChar, width, true);
    }

    private static List<String> align(Iterable<String> strings, char paddChar, int width, boolean even) {
        List<String> lines = new LinkedList<>();
        List<Integer> offsets = new ArrayList<>(width);
        StringBuilder part = null;
        System.out.println(width);
        for (String string : strings) {
            if (part != null && part.length() + 1 + string.length() > width) {
                insertPaddChars(part, offsets, paddChar, width, even);
                lines.add(part.toString());
                offsets.clear();
                part = new StringBuilder(string);
                offsets.add(part.length());
            } else {
                part = (part == null ? new StringBuilder(width) : part.append(paddChar)).append(string);
                offsets.add(part.length());
            }
        }
        if (part != null) {
            insertPaddChars(part, offsets, paddChar, width, even);
            lines.add(part.toString());
        }
        return lines;
    }

    private static void insertPaddChars(StringBuilder part, List<Integer> offsets, char paddChar, int width,
            boolean even) {
        int e = width - part.length();
        if (even) {
            offsets.add(0, part.length());
            offsets.add(0, 0);
        }
        String padd = String.valueOf(paddChar);
        for (int k = 0; e > 0; k = (k + 1) % offsets.size(), e--) {
            part.insert(offsets.get(k), padd);
            offsets.set(k, offsets.get(k) + 1);
        }
    }

    public static <T> List<String> tree(
            Iterable<T> items,
            Function<T, Iterable<T>> subItems,
            Function<T, String> itemString,
            boolean numbering, int treePaddLen, char paddChar, int width) {
        List<String> lines = new LinkedList<>();
        tree(lines, "", items, subItems, itemString, numbering ? 0 : -1, treePaddLen, paddChar, width);
        return lines;
    }

    private static <T> void tree(
            List<String> lines,
            String padding,
            Iterable<T> items,
            Function<T, Iterable<T>> subItems,
            Function<T, String> itemString,
            int number, int treePaddLen, char paddChar, int width) {
        if (items == null)
            return;
        int i = 1;
        for (T item : items) {
            String line = padding + (number < 1 ? "" : number + ".") + i + " " + itemString.apply(item);
            // TODO check for width of line
            lines.add(line);
            i++;
            tree(lines, padding + repeat(treePaddLen, paddChar), subItems.apply(item),
                    subItems, itemString, number == -1 ? -1 : i, treePaddLen, paddChar, width);
        }
    }

    private static String repeat(int times, char r) {
        StringBuilder str = new StringBuilder();
        while (times-- > 0)
            str.append(r);
        return str.toString();
    }

    public static int calculateWidth(Iterable<String> strings, Alignment alignment) {
        int width = 0;
        switch (alignment) {
            case CENTER:
            case LEFT:
            case RIGHT:
            case FILL:
                for (String s : strings)
                    width = Math.max(width, s.length());
                break;
            case SPACE_BETWEEN:
            case SPACE_EVENLY:
                for (String s : strings)
                    width += s.length() + 1;
                width--;
                break;
        }
        return width;
    }

}