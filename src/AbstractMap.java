import java.util.Iterator;

public abstract class AbstractMap<K,V> implements Map<K,V> {
    /**
     * Tests whether map is empty.
     * @return true if map is empty.
     */
    @Override
    public boolean isEmpty() { return size() == 0; }

    // Nested MapEntry class
    protected static class MapEntry<K,V> implements Entry<K,V> {
        private K k;
        private V v;

        /**
         * Creates an entry with given key and value
         * @param key the Object to be used as the key
         * @param value the Object to be stored as a value
         */
        public MapEntry(K key, V value) {
            k = key;
            v = value;
        }

        /**
         * Returns the object representing the entry's key
         * @return the entry's key
         */
        public K getKey() { return k; }

        /**
         * Returns the value stored for the entry
         * @return the value stored for the entry
         */
        public V getValue() { return v; }

        /**
         * Changes key to given Object
         * @param key the new Object to represent the entry's key
         */
        protected void setKey(K key) { k = key; }

        /**
         * Replaces the entry's value with a given Object
         * @param value the Object to be used as the entry's value
         * @return the newly set value
         */
        protected V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }

        // For debugging purposes
        public String toString() { return "<" + k + ", " + v + ">"; }
    }

    // nested KeyIterator class

    private class KeyIterator implements Iterator<K> {
        /**
         * Returns an iterable collection of entries in the map
         */
        private Iterator<Entry<K,V>> entries = entrySet().iterator();

        /**
         * Returns true if there are more entries in the collection
         * @return true if collection has more entries, false otherwise
         */
        public boolean hasNext() { return entries.hasNext(); }

        /**
         * Returns the next entry in the collection
         * @return next entry
         */
        public K next() { return entries.next().getKey(); }

        /**
         * Does nothing. Remove not supported.
         */
        public void remove() { throw new UnsupportedOperationException("remove not supported"); }
    }

    // Nested iterable class
    private class KeyIterable implements Iterable<K> {
        public Iterator<K> iterator() { return new KeyIterator(); }
    }

    /**
     * @return an iterable collection of keys stored in the queue
     */
    @Override
    public Iterable<K> keySet() { return new KeyIterable(); }

    // Nested ValueIterator class
    private class ValueIterator implements Iterator<V> {
        /**
         * Returns an iterable collection of entries
         */
        private Iterator<Entry<K,V>> entries = entrySet().iterator();

        /**
         * @return true if collection has more entries
         */
        public boolean hasNext() { return entries.hasNext(); }
        /** @return next entry in collection */
        public V next() { return entries.next().getValue(); }
        /** does nothing, remove not supported */
        public void remove() { throw new UnsupportedOperationException("remove not supported"); }
    }

    // nested ValueIterable class
    private class ValueIterable implements Iterable<V> {
        public Iterator<V> iterator() { return new ValueIterator(); }
    }

    /** @return an iterable collection of values in the queue */
    @Override
    public Iterable<V> values() { return new ValueIterable(); }
}
