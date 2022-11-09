package layout;

import java.util.Iterator;

final class BodyList extends Body {
    private Iterable<String> strings;

    public BodyList(Alignment alignment, Iterable<String> strings) {
        this(alignment, 0, strings);
    }

    public BodyList(Alignment alignment, int specs, Iterable<String> strings) {
        super(alignment, specs);
        this.strings = strings;
    }

    @Override
    public Iterator<String> iterator() {
        return strings.iterator();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((strings == null) ? 0 : strings.hashCode());
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
        BodyList other = (BodyList) obj;
        if (strings == null) {
            if (other.strings != null)
                return false;
        } else if (!strings.equals(other.strings))
            return false;
        return true;
    }

}
