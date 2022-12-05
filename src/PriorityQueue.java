public interface PriorityQueue<K,V> {
    /** @return the size of the queue */
    int size();

    /** return true if queue is empty, false otherwise */
    boolean isEmpty();

    /**
     * inserts a new entry with given key and value into the queue
     * @param key the key for the new entry
     * @param value the value stored at the new entry
     * @return the newly created entry
     * @throws IllegalArgumentException if key or value are not valid
     */
    Entry<K,V> insert(K key, V value) throws IllegalArgumentException;

    /**
     * Returns (but does not remove) the entry with minimum key
     * @return entry in queue with minimum key
     */
    Entry<K,V> min();

    /**
     * Removes and returns entry with minimum key
     * @return entry with minimum key
     */
    Entry<K,V> removeMin();
}
