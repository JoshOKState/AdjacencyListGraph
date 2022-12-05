public interface Entry<K, V> {
    /** @return key stored in Entry */
    K getKey();
    /** @return value stored in Entry */
    V getValue();
}
