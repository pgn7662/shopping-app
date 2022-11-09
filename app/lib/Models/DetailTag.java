package Models;

import java.util.LinkedList;
import java.util.List;

public final class DetailTag extends Tag {
    private List<DetailTag> subDetailTags;
    private List<String> values;

    public DetailTag(String name, List<DetailTag> subDetailTags, List<String> values) {
        super(name);
        this.subDetailTags = subDetailTags;
        this.values = values;
    }

    public DetailTag(String name) {
        this(name, new LinkedList<>(), new LinkedList<>());
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return values;
    }

    public void addValue(String value) {
        values.add(value);
    }

    public void addSubDetailTag(DetailTag detailTag) {
        subDetailTags.add(detailTag);
    }

    public List<DetailTag> getSubDetailTags() {
        return subDetailTags;
    }

}
