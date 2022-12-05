public interface Position<E> {
    /** @return element stored at Position */
    E getElement() throws IllegalStateException;
}
