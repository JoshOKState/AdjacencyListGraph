import java.util.Comparator;

public class HeapAdaptablePriorityQueue<K,V> extends HeapPriorityQueue<K,V>
                                    implements AdaptablePriorityQueue<K,V> {
    // Nested AdaptablePQEntry class
    protected static class AdaptablePQEntry<K,V> extends PQEntry<K,V> {
        // Instance variables
        private int index;

        // Constructors
        public AdaptablePQEntry(K key, V value, int j) {
            super(key, value);
            index = j;
        }

        // Accessors
        public int getIndex() { return index; }

        // Mutators
        public void setIndex(int j) { index = j; }
    }

    // Constructors

    /** Creates Queue with default Comparator */
    public HeapAdaptablePriorityQueue() { super(); }
    /** Creates Queue with given Comparator */
    public HeapAdaptablePriorityQueue(Comparator<K> comp) { super(comp); }

    /**
     * Checks the Entry exists in Queue
     * @param entry the entry to confirm exists
     * @return index of found Entry
     * @throws IllegalArgumentException if entry does not exist
     */
    protected  AdaptablePQEntry<K,V> validate(Entry<K,V> entry) throws IllegalArgumentException {
        if (!(entry instanceof AdaptablePQEntry))
            throw new IllegalArgumentException("Invalid entry");
        AdaptablePQEntry<K,V> locator = (AdaptablePQEntry<K, V>) entry;
        int j = locator.getIndex();
        if (j >= heap.size() || heap.get(j) != locator)
            throw new IllegalArgumentException("Invalid entry");
        return locator;
    }

    /**
     * Swaps positions of elements at given indices
     * @param i the index of the first element to swap
     * @param j the index of the second element to swap
     */
    @Override
    protected void swap(int i, int j) {
        super.swap(i,j);
        ((AdaptablePQEntry<K,V>) heap.get(i)).setIndex(i);
        ((AdaptablePQEntry<K,V>) heap.get(j)).setIndex(j);
    }

    /**
     * Adjusts element at given index's position as needed
     * @param j the index of the element that may need to be moved
     */
    protected void bubble(int j) {
        if (j > 0 && compare(heap.get(j), heap.get(parent(j))) < 0)
            upheap(j);
        else
            downheap(j);            // may not need to move
    }

    /**
     *
     * @param key the key for the new entry
     * @param value the value stored at the new entry
     * @return the newly created Entry
     * @throws IllegalArgumentException if key or value are not valid Object type
     */
    @Override
    public Entry<K,V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        Entry<K,V> newest = new AdaptablePQEntry<>(key, value, heap.size());
        heap.add(newest);
        upheap(heap.size() - 1);
        return newest;
    }

    /**
     * Removes a given Entry from the Queue
     * @param entry the entry to be removed
     * @throws IllegalArgumentException if entry does not exist in list
     */
    @Override
    public void remove(Entry<K,V> entry) throws IllegalArgumentException {
        AdaptablePQEntry<K,V> locator = validate(entry);
        int j = locator.getIndex();
        if (j == heap.size() - 1)
            heap.remove(heap.size() - 1);
        else {
            swap(j, heap.size() - 1);
            heap.remove(heap.size() - 1);
            bubble(j);
        }
    }

    /**
     * Replaces key at given Entry
     * @param entry an entry in the queue
     * @param key the new key for the entry
     * @throws IllegalArgumentException if Entry does not exist in Queue
     */
    @Override
    public void replaceKey(Entry<K,V> entry, K key) throws IllegalArgumentException {
        AdaptablePQEntry<K,V> locator = validate(entry);
        checkKey(key);
        locator.setKey(key);
        bubble(locator.getIndex());
    }

    /**
     * Replaces value at given Entry
     * @param entry an entry in the queue
     * @param value the new value for the entry
     * @throws IllegalArgumentException if Entry does not exist in Queue
     */
    @Override
    public void replaceValue(Entry<K,V> entry, V value) throws IllegalArgumentException {
        AdaptablePQEntry<K,V> locator = validate(entry);
        locator.setValue(value);
    }
}