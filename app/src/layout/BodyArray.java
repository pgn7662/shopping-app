package layout;

import java.util.Arrays;
import java.util.Iterator;

final class BodyArray extends Body {
    private int index;
    private String[] items;
    private Iterator<String> iterator = new Iterator<String>() {

        @Override
        public boolean hasNext() {
            if (index == items.length) {
                index = 0;
                return false;
            }
            return true;
        }

        @Override
        public String next() {
            return items[index++];
        }

    };

    public BodyArray(Alignment alignment, String... strings) {
        this(alignment, 0, strings);
    }

    public BodyArray(Alignment alignment, int specs, String... arrStrings) {
        super(alignment, specs);
        this.index = 0;
        this.items = arrStrings;
    }

    @Override
    public Iterator<String> iterator() {
        return iterator;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + Arrays.hashCode(items);
        result = prime * result + ((iterator == null) ? 0 : iterator.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BodyArray other = (BodyArray) obj;
        if (index != other.index)
            return false;
        if (!Arrays.equals(items, other.items))
            return false;
        if (iterator == null) {
            if (other.iterator != null)
                return false;
        } else if (!iterator.equals(other.iterator))
            return false;
        return true;
    }

}
