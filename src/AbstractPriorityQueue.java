import java.util.Comparator;

public abstract class AbstractPriorityQueue<K,V> implements PriorityQueue<K,V> {
    // nested PQ entry class
    protected static class PQEntry<K,V> implements Entry<K,V> {
        // instance variables
        private K k;    // key
        private V v;    // value

        // Constructors
        /** Constructs an entry with given key and value */
        public PQEntry(K key, V value) {
            k = key;
            v = value;
        }

        // Accessors

        /**
         * returns the Object stored as entry's key
         * @return the entry's key
         */
        public K getKey() { return k; }

        /**
         * returns the Object stored as entry's value
         * @return the entry's value
         */
        public V getValue() { return v; }

        // Mutators

        /**
         * Updates entry with given key
         * @param key the new key for the entry
         */
        protected void setKey(K key) { k = key; }

        /**
         * Updates entry with given value
         * @param value the new value for the entry
         */
        protected void setValue(V value) { v = value; }
    }

    /**
     * Default comparator for the Queue
     */
    private Comparator<K> comp;

    /** Creates a Queue with given Comparator */
    protected AbstractPriorityQueue(Comparator<K> c) { comp = c; }

    /** Creates a Queue with default Comparator */
    protected AbstractPriorityQueue() { this(new DefaultComparator<K>()); }

    /**
     * Compares keys for given entries
     * @param a the first entry whose key should be compared
     * @param b the second entry whose key should be compared
     * @return 1 if a > b, -1 if b < a, 0 if b == a
     */
    protected  int compare(Entry<K, V> a, Entry<K,V> b) { return comp.compare(a.getKey(), b.getKey()); }

    /**
     * Checks that key is valid
     * @param key an Object to represent entry's key
     * @return true if Object can be compared to itself
     * @throws IllegalArgumentException if Object cannot be compared
     */
    protected boolean checkKey(K key) throws IllegalArgumentException {
        try {
            return (comp.compare(key, key) == 0);   // see if key can be compared to itself
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible key");
        }
    }

    /** @return true if Queue is empty */
    @Override
    public boolean isEmpty() { return size() == 0; }
}
