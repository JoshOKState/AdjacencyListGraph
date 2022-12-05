
import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractHashMap<K,V> extends AbstractMap<K,V> {
    protected int n = 0;        // number of entries
    protected int capacity;     // length of the table
    private int prime;          // prime factor
    private long scale, shift;   // the shift and scaling factors

    // Constructors

    /**
     * Constructs a map with given capacity and prime factors
     * @param cap the capacity of the new map
     * @param p the prime factor to be used for the map
     */

    public AbstractHashMap(int cap, int p) {
        prime = p;
        capacity = cap;
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1;
        shift = rand.nextInt(prime);
        createTable();
    }

    /**
     * Constructs a map with given capacity and default prime 109345121
     * @param cap the capactity of the new map
     */
    public AbstractHashMap(int cap) { this(cap, 109345121); }

    /**
     * Creates a map with default capacity of 17 and default prime 109345121
     */
    public AbstractHashMap() { this(17); }      // default capacity

    /**
     * Returns the number of entries in the map
     */
    @Override
    public int size() { return n; }

    /**
     * Returns the value found at the given key
     * @param key a key in the map
     * @return the value stored at the given key
     */
    @Override
    public V get(K key) { return bucketGet(hashValue(key), key); }

    /**
     * Removes the given key from the map and returns its value
     * @param key the key to be removed
     * @return the value stored at the removed key
     */
    @Override
    public V remove(K key) { return bucketRemove(hashValue(key), key); }

    /**
     * Stores a new key/value pair in the map
     * @param key the object to be stored as a key
     * @param value the value to be stored at the given key
     * @return the value of the entry
     */
    @Override
    public V put(K key, V value) {
        V answer = bucketPut(hashValue(key), key, value);
        if(n > capacity / 2) resize(2 * capacity - 1);
        return answer;
    }

    /**
     * Determines the hash value for a given key
     * @param key a key in the map
     * @return the calculated hash value
     */
    private int hashValue(K key) { return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity); }

    /**
     * Resizes the map to given capactiy
     * @param newCap the new capacity for the map
     */
    private void resize(int newCap) {
        ArrayList<Entry<K,V>> buffer = new ArrayList<>(n);
        for (Entry<K,V> e : entrySet()) buffer.add(e);
        capacity = newCap;
        createTable();      // based on updated capacity
        n = 0;              // will be recomputed while reinserting entries
        for(Entry<K,V> e : buffer) put(e.getKey(), e.getValue());
    }

    // Protected utilities
    protected abstract void createTable();

    protected abstract V bucketGet(int h, K k);

    protected abstract V bucketPut(int h, K k, V v);

    protected abstract V bucketRemove(int h, K k);
}
