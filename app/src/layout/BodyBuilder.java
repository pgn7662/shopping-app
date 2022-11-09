package layout;

public class BodyBuilder {
    Alignment alignment;
    int specs;
    String[] arrStrings;
    Iterable<String> iterableStrings;

    public BodyBuilder() {
        this.specs = 0;
    }

    public BodyBuilder(String... strings) {
        this.specs = 0;
        this.arrStrings = strings;
    }

    public BodyBuilder(Iterable<String> strings) {
        this.specs = 0;
        this.iterableStrings = strings;
    }

    public BodyBuilder(Alignment alignment, String... strings) {
        this(alignment, 0, strings);
    }

    public BodyBuilder(int specs, String... strings) {
        this(Alignment.LEFT, specs, strings);
    }

    public BodyBuilder(Alignment alignment, int specs, String... strings) {
        this.alignment = alignment;
        this.specs = specs;
        this.arrStrings = strings;
    }

    public BodyBuilder(Alignment alignment, Iterable<String> strings) {
        this(alignment, 0, strings);
    }

    public BodyBuilder(int specs, Iterable<String> strings) {
        this(Alignment.LEFT, specs, strings);
    }

    public BodyBuilder(Alignment alignment, int specs, Iterable<String> strings) {
        this.alignment = alignment;
        this.specs = specs;
        this.iterableStrings = strings;
    }

    public BodyBuilder setAlign(Alignment align) {
        this.alignment = align;
        return this;
    }

    public BodyBuilder setParts(String... arrStrings) {
        this.arrStrings = arrStrings;
        return this;
    }

    public BodyBuilder setParts(Iterable<String> iterableStrings) {
        this.iterableStrings = iterableStrings;
        return this;
    }

    Body build() {
        if (arrStrings != null) {
            return new BodyArray(alignment, specs, arrStrings);
        } else if (iterableStrings != null) {
            return new BodyList(alignment, specs, iterableStrings);
        }
        return null;
    }
}
