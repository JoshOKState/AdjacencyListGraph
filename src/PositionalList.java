import java.util.Iterator;

public interface PositionalList<E> extends Iterable<E> {
    /** @return number of elements in list */
    int size();
    /** @return true if list is empty */
    boolean isEmpty();
    /** @return the first Position in the list or null */
    Position<E> first();
    /** @return the last Position in the list or null */
    Position<E> last();
    /**
     * @param p a Position of the list
     * @return the Position preceding p or null
     * @throws IllegalArgumentException if p is not a valid Position
     */
    Position<E> before(Position<E> p) throws IllegalArgumentException;

    /**
     * @param p     a Position of the list
     * @return the Position of the following ellement or null
     * @throws IllegalArgumentException if p is not a valid Position
     */
    Position<E> after(Position<E> p) throws IllegalArgumentException;
    /**
     * Inserts element at front of list.
     *
     * @param e the new element
     * @return the Position representing the location of the new element
     */
    Position<E> addFirst(E e);

    /**
     * Inserts an element at the end of a list.
     *
     * @param e the new element
     * @return the Position representing the location of the new element
     */
    Position<E> addLast(E e);

    /**
     * Inserts an element immediately before a given Position.
     *
     * @param p the Position to follow the new element
     * @param e the new element
     * @return the Position representing the location of the new element
     * @throws IllegalArgumentException if p is not a valid Position in the list
     */
    Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException;

    /**
     * Inserts a new element immediately after a given Position in the list.
     *
     * @param p the Position to precede the new element
     * @param e the new element
     * @return the Position representing the location of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this list
     */
    Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException;

    /**
     * Replaces the element at a given Position with a new element and returns replaced element.
     *
     * @param p the Position of the element to be replaced
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position in the list
     */
    E set(Position<E> p, E e) throws IllegalArgumentException;

    /**
     * Removes a given Position from the list and returns its element
     *
     * @param p the Position to be removed
     * @return the element contained at Position p
     * @throws IllegalArgumentException if P is not a valid Position for this list
     */
    E remove(Position<E> p) throws IllegalArgumentException;

    /**
     * Returns an iterator of elements stored in the list.
     * @return iterator of list's elements
     */
    Iterator<E> iterator();

    /**
     * Returns Positions in list in iterable form from first to last.
     * @return iterable collection of Positions in list.
     */
    Iterable<Position<E>> positions();
}
