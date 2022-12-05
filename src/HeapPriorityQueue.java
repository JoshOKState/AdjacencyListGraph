import java.util.ArrayList;
import java.util.Comparator;

public class HeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {
    // Queue implemented as ArrayList
    protected ArrayList<Entry<K,V>> heap = new ArrayList<>();

    // Constructors

    /** Constructs a Queue with default Comparator */
    public HeapPriorityQueue() { super(); }

    /** Constructs a Queue with given Comparator */
    public HeapPriorityQueue(Comparator<K> comp) { super(comp); }

    /**
     * Constructs a Queue with given keys and values
     * @param keys an array of Keys to insert into Queue
     * @param values an array of values to insert into Queue
     */
    public HeapPriorityQueue(K[] keys, V[] values) {
        super();
        for (int j = 0; j < Math.min(keys.length, values.length); j ++)
            heap.add(new PQEntry<>(keys[j], values[j]));
        heapify();
    }

    // Protected utilities
    /** @return index of parent */
    protected int parent(int j) { return (j-1) / 2; }

    /** @return index of left child for given index */
    protected int left(int j) { return 2 * j + 1; }

    /** @return index of right child for given index */
    protected int right(int j) { return 2 * j + 2; }

    /** @return true if Node at given index has left child */
    protected boolean hasLeft(int j) { return left(j) < heap.size(); }

    /** @return true if Node at given index has right child */
    protected boolean hasRight(int j) { return right(j) < heap.size(); }

    /** swaps elements at given positions */
    protected void swap(int i, int j) {
        Entry<K,V> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /** Moves element at given index's position as required to satisfy heap property */
    protected void upheap(int j) {
        while (j > 0) {
            int p = parent(j);
            if (compare(heap.get(j), heap.get(p)) >= 0) break;  // heap property verified
            swap(j,p);
            j = p;          // continue from parent's location
        }
    }

    /** Moves element at given index's position as required to satisfy heap property */
    protected void downheap(int j) {
        while (hasLeft(j)) {
            int leftIndex = left(j);
            int smallChildIndex = leftIndex;
            if (hasRight(j)) {
                int rightIndex = right(j);
                if (compare(heap.get(leftIndex), heap.get(rightIndex)) > 0)
                    smallChildIndex = rightIndex;
            }
            if(compare(heap.get(smallChildIndex), heap.get(j)) >= 0) break;
            swap(j, smallChildIndex);
            j = smallChildIndex;        // continue at position of child
        }
    }

    /** Restores heap property as required */
    protected void heapify() {
        int startIndex = parent(size() - 1);
        for (int j = startIndex; j >= 0; j--) downheap(j);
    }

    /**
     * Returns the number of items in heap
     * @return number of items in heap
     */
    @Override
    public int size() { return heap.size(); }

    /**
     * Returns but does not remove min entry in Queue
     * @return minimum entry in Queue, or null if none exists
     */
    @Override
    public Entry<K,V> min() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    /**
     * Inserts an entry with given key and value into the heap
     * @param key the key for the new entry
     * @param value the value stored at the new entry
     * @return the newly created Entry
     * @throws IllegalArgumentException if key or value are invalid type for Queue
     */
    @Override
    public Entry<K,V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        Entry<K,V> newest = new PQEntry<>(key, value);
        heap.add(newest);
        upheap(heap.size() - 1);
        return newest;
    }

    /**
     * Removes and returns Entry in Queue with minimum key if exists
     * @return Entry with minimum key, or null if none exists
     */
    @Override
    public Entry<K,V> removeMin() {
        if (heap.isEmpty()) return null;
        Entry<K,V> answer = heap.get(0);
        swap(0, heap.size() - 1);
        heap.remove(heap.size() - 1);
        downheap(0);
        return answer;
    }

    // For debugging purposes
    private void sanityCheck() {
        for (int j = 0; j < heap.size(); j++) {
            int left = left(j);
            int right = right(j);
            if (left < heap.size() && compare(heap.get(left), heap.get(j)) < 0)
                System.out.println("Invalid left child relationship");
            if (right < heap.size() && compare(heap.get(right), heap.get(j)) < 0)
                System.out.println("Invalid right child relationship");
        }
    }
}
