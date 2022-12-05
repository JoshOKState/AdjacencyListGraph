public interface Map<K,V> {
    /** @return number of entries in Map */
    int size();
    /** @return true if map is empty */
    boolean isEmpty();

    /**
     * Returns value stored at given key
     * @param key a key in the map
     * @return the value stored at the given key
     */
    V get(K key);

    /**
     * Inserts a new entry with given key and value into the map
     * @param key the key for the new entry
     * @param value the value represented by the key
     * @return the value of the newly created entry
     */
    V put(K key, V value);

    /**
     * Removes an entry with given key from the Map and returns its value
     * @param key the key of the entry to be removed
     * @return the value stored at the removed key
     */
    V remove(K key);

    /** @return an iterable collection of keys stored in the map */
    Iterable<K> keySet();

    /** @return an iterable collection of values stored in the map */
    Iterable<V> values();

    /** @return an iterable collection of Entries stored in the map */
    Iterable<Entry<K,V>> entrySet();
}
