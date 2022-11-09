package layout;

import java.util.LinkedList;
import java.util.List;

public abstract class Layout {
    public static final int MAX_WIDTH = 120;
    public static final String DEFAULT_BORDER = "*";
    public static final int MAX_BORDER_LENGTH = 6;
    public static final int MAX_HORIZONTAL_PADDING = 9;
    public static final int MAX_VERTICAL_PADDING = 6;
    public static final char DEFAULT_PADD_CHAR = ' ';

    private static String border;
    private static int horizontalPaddLength;
    private static int verticalPaddLength;
    private static char paddChar;

    static {
        border = DEFAULT_BORDER;
        horizontalPaddLength = 3;
        verticalPaddLength = 1;
        paddChar = DEFAULT_PADD_CHAR;
    }

    public String getBorder() {
        return border;
    }

    public static void setBorder(String border) {
        Layout.border = border;
    }

    public int getHorizontalPaddLength() {
        return horizontalPaddLength;
    }

    public static void setHorizontalPaddLength(int horizontalPaddLength) {
        Layout.horizontalPaddLength = horizontalPaddLength;
    }

    public int getVerticalPaddLength() {
        return verticalPaddLength;
    }

    public static void setVerticalPaddLength(int verticalPaddLength) {
        Layout.verticalPaddLength = verticalPaddLength;
    }

    public char getPaddChar() {
        return paddChar;
    }

    public static void setPaddChar(char paddChar) {
        Layout.paddChar = paddChar;
    }

    protected abstract List<BodyBuilder> getBodyBuilders();

    private List<Body> buildLayoutBodies() {
        List<Body> body = new LinkedList<>();
        for (BodyBuilder builder : getBodyBuilders())
            body.add(builder.build());
        return body;
    }

    private int calculateWidth(List<Body> body) {
        int width = 0;
        for (Body layoutBody : body)
            width = Math.min(Math.max(layoutBody.calculateWidth(), width), MAX_WIDTH);
        return width;
    }

    protected final String buildLayout() {
        String border = getBorder();
        if (border.length() > MAX_BORDER_LENGTH)
            border = border.substring(0, MAX_BORDER_LENGTH);
        int horizontalPaddLength = Math.min(getHorizontalPaddLength(), MAX_HORIZONTAL_PADDING);
        int verticalPaddLength = Math.min(getVerticalPaddLength(), MAX_VERTICAL_PADDING);
        char paddChar = getPaddChar();
        List<Body> layoutBodies = buildLayoutBodies();
        int width = calculateWidth(layoutBodies);
        String horizontalPadding = getStringfilledWith(String.valueOf(paddChar), horizontalPaddLength);
        String paddLine = border + horizontalPadding + getStringfilledWith(String.valueOf(paddChar), width)
                + horizontalPadding + border;
        String filledBorder = getStringfilledWith(border, (border.length() + horizontalPaddLength) * 2 + width);
        StringBuilder layout = new StringBuilder();
        appendStringBuilder(layout, filledBorder, border.length());
        appendStringBuilder(layout, paddLine, verticalPaddLength);
        for (Body body : layoutBodies) {
            if (body.leaveLineAbove())
                layout.append(paddLine).append(System.lineSeparator());
            for (String part : body.build(paddChar, width)) {
                layout.append(border).append(horizontalPadding).append(part).append(horizontalPadding)
                        .append(border).append(System.lineSeparator());
            }
            if (body.leaveLineBelow())
                layout.append(paddLine).append(System.lineSeparator());
        }
        appendStringBuilder(layout, paddLine, verticalPaddLength);
        appendStringBuilder(layout, filledBorder, border.length());
        return layout.toString();
    }

    protected void appendStringBuilder(StringBuilder builder, String string, int times) {
        while (times-- > 0)
            builder.append(string).append(System.lineSeparator());
    }

    private String getStringfilledWith(String with, int maxLength) {
        StringBuilder stringBuilder = new StringBuilder();
        while (stringBuilder.length() + with.length() <= maxLength)
            stringBuilder.append(with);
        if (maxLength % with.length() > 0)
            stringBuilder.append(with.substring(0, maxLength % with.length()));
        return stringBuilder.toString();
    }

}