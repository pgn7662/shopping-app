package Models;

import java.util.List;

public class ValueTag<T> extends Tag {
    private List<T> values;

    public ValueTag(String name, List<T> values) throws Exception {
        super(name);
        this.values = values;
        if (values == null || values.isEmpty())
            throw new Exception("Values in value tag should not be empty!");
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public void addValue(T value) {
        values.add(value);
    }

    public boolean isValuePresent(Object value) {
        for (T val : values)
            if (val.equals(value) || val.toString().equals(value.toString()))
                return true;
        return false;
    }

}
