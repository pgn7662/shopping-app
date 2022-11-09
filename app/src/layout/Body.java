package layout;

import java.util.List;

public abstract class Body implements Iterable<String> {
    public static final int LEAVE_LINE_ABOVE = 1;
    public static final int LEAVE_LINE_BELOW = 2;
    public static final int FILL = 4;

    private Alignment alignment;
    private int specs;

    protected Body(Alignment alignment) {
        this.alignment = alignment;
    }

    protected Body(Alignment alignment, int specs) {
        this.alignment = alignment;
        this.specs = specs;
    }

    protected Body(int specs) {
        this.specs = specs;
    }

    public boolean leaveLineAbove() {
        return (specs & LEAVE_LINE_ABOVE) == LEAVE_LINE_ABOVE;
    }

    public boolean fill() {
        return (specs & FILL) == FILL;
    }

    public boolean leaveLineBelow() {
        return (specs & LEAVE_LINE_BELOW) == LEAVE_LINE_BELOW;
    }

    public List<String> build(char paddChar, int width) {
        if (alignment == null)
            return StringAlignment.left(this, paddChar, width);
        return StringAlignment.align(this, paddChar, width, alignment);
    }

    public int calculateWidth() {
        return StringAlignment.calculateWidth(this, alignment);
    }

}
