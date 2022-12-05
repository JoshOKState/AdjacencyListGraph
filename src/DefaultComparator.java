import java.util.Comparator;

public class DefaultComparator<E> implements Comparator<E> {
    /**
     * Default comparator
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return 1 if a > b, -1 if b > a, 0 if a == b
     * @throws ClassCastException
     */
    public int compare(E a, E b) throws ClassCastException {
        return ((Comparable<E>) a).compareTo(b);
    }
}
