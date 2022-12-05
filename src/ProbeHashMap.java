import java.util.ArrayList;

public class ProbeHashMap<K,V> extends AbstractHashMap<K,V> {
    // Instance variables
    private MapEntry<K,V>[] table;      // a fixed array of entries
    private MapEntry<K,V> DEFUNCT = new MapEntry<>(null, null);

    // Constructors

    public ProbeHashMap() { super(); }

    public ProbeHashMap(int cap) { super(cap); }

    public ProbeHashMap(int cap, int p) { super(cap, p); }


    /**
     * creates a table for the map
     */
    @Override
    protected void createTable() { table = (MapEntry<K, V>[]) new MapEntry[capacity]; }

    /**
     * checks if location in table is occupied by object
     * @param j the index to check in the table
     * @return
     */
    private boolean isAvailable(int j) { return (table[j] == null || table[j] == DEFUNCT); }

    /**
     * Searches through table for available location for new entry
     * @param h the starting index for the search
     * @param k the Object to be stored in the table
     * @return next available index, or negative if none available
     */
    private int findSlot(int h, K k) {
        int avail = -1;                                         // no slot available yet
        int j = h;                                              // index while scanning table
        do {
            if (isAvailable(j)) {                               // may be either empty or defunct
                if (avail == -1) avail = j;                     // this is the first available slot!
                if (table[j] == null) break;                    // if empty, search fails immediately
            } else if (table[j].getKey().equals(k)) return j;   // successful match
            j = (j + 1) % capacity;                             // keep looking
        } while (j != h);                                       // stop if back at start
        return -(avail + 1);                                    // search failed
    }

    /**
     * Returns the value stored at given key
     * @param h the starting index of the table to begin search
     * @param k the key whose value is to be returned
     * @return value stored at k, or null if k not found
     */
    @Override
    protected V bucketGet(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) return null;
        return table[j].getValue();
    }

    /**
     * inserts a new entry with given key and value into Queue
     * @param h the hash value to begin search the table
     * @param k the key to be stored
     * @param v the value for the new entry at k
     * @return the value of the newly created entry
     */
    @Override
    protected V bucketPut(int h, K k, V v) {
        int j = findSlot(h, k);
        if (j >= 0) return table[j].setValue(v);
        table[-(j+1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }

    /**
     * removes a given key from the table
     * @param h the hash value at which to begin table search
     * @param k the key to be removed
     * @return the value of the removed key or null if not found
     */
    @Override
    protected V bucketRemove(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) return null;
        V answer = table[j].getValue();
        table[j] = DEFUNCT;
        n--;
        return answer;
    }

    /**
     * @return an iterable collection of entries in Map
     */
    @Override
    public Iterable<Entry<K,V>> entrySet() {
        ArrayList<Entry<K,V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++) if (!isAvailable(h)) buffer.add(table[h]);
        return buffer;
    }
}
