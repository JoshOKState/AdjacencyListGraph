public interface AdaptablePriorityQueue<K,V> extends PriorityQueue<K,V> {
    /**
     * Removes the entry from the Queue
     * @param entry the entry to be removed
     * @throws IllegalArgumentException if entry is invalid
     */
    void remove(Entry<K,V> entry) throws IllegalArgumentException;

    /**
     * Replaces the key for the given entry with given key
     * @param entry an entry in the queue
     * @param key the new key for the entry
     * @throws IllegalArgumentException if entry does not exist in Queue
     */
    void replaceKey(Entry<K,V> entry, K key) throws IllegalArgumentException;

    /**
     * Replaces the value for the given entry with a given value
     * @param entry an entry in the queue
     * @param value the new value for the entry
     * @throws IllegalArgumentException if entry does not exist in queue
     */
    void replaceValue(Entry<K,V> entry, V value) throws IllegalArgumentException;
}
