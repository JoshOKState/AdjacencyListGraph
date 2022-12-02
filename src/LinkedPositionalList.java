import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedPositionalList<E> implements PositionalList<E> {

    // nested Node class
    /**
     * Node of a doubly linked list
     */
    private static class Node<E> implements Position<E> {

        /** the element stored at this Node */
        private E element;

        private Node<E> prev;

        private Node<E> next;

        // Constructor

        /**
         * Creates a Node with given element and surrounding Nodes
         *
         * @param e the element to be stored at the new Node
         * @param p the Node to precede the new Node
         * @param n the Node to follow the new Node
         */
        public Node(E e, Node<E> p, Node<E> n) {
            element = e;
            prev = p;
            next = n;
        }

        // Accessors

        /**
         * Returns element stored at Node
         *
         * @return the stored Element
         * @throws IllegalStateException if Node not linked to others
         * */
        public E getElement() throws IllegalStateException {
            if(next == null) {
                throw new IllegalStateException("Position no longer valid");
            }
            return element;
        }


        /**
         * Returns the previous node in the list or null if none exists
         * @return previous node in list
         */
        public Node<E> getPrev() { return prev; }

        /**
         * Returns the next node in the list or null if none exists
         * @return the next node in the list
         */
        public Node<E> getNext() { return next; }

        // Mutators

        /**
         * Set the Node's element to given element e
         * @param e the Node's new element
         */
        public void setElement(E e) { element = e ; }

        /**
         * Sets Node's prev reference to Node p
         * @param p the Node to precede this one
         */
        public void setPrev(Node<E> p) { prev = p; }

        /**
         * Sets Node's next reference to Node n
         * @param n the Node to follow this one
         */
        public void setNext(Node<E> n) { next = n; }
    }

    // Instance variables
    /** Sentinel Node at beginning of list */
    final private Node<E> header;

    /** Sentinel Node at end of list */
    final private Node<E> trailer;

    /** Number of elements in list (not including sentinels) */
    private int size = 0;

    // Constructors
    /** Constructs a new empty list */
    public LinkedPositionalList() {
        header = new Node<>(null, null, null);  // create header
        trailer = new Node<>(null, header, null); // trailer is preceded by header
        header.setNext(trailer);                        // header is followed by trailer
    }

    // private utilities

    /**
     * Verifies that a Position belongs to appropriate class and has not been removed.
     *
     * @param p a Position that should be in the list
     * @return the Node at that Position
     * @throws IllegalArgumentException if p is in invalid Position
     */
    private Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if(!(p instanceof Node)) throw new IllegalArgumentException("Invalid p");
        Node <E> node = (Node<E>) p;    // safe cast
        if(node.getNext() == null) {    // convention for defunct node
            throw new IllegalArgumentException("p is no longer in the list");
        }
        return node;
    }

    /**
     * returns given node as a Position unless node is a sentinel
     */
    private Position<E> position(Node<E> node) {
        if(node == header || node == trailer) return null;    // do not expose user to sentinels
        return node;
    }

    // Accessors

    /**
     * Returns number of elements in list
     */
    public int size() { return size; }

    /**
     * returns true if list is empty
     */
    public boolean isEmpty() { return size == 0; }

    /**
     * @return the first Position in the list or null
     */
    public Position<E> first() { return position(header.getNext()); }

    /**
     * @return the last Position in the list or null
     */
    public Position<E> last() { return position(trailer.getPrev()); }

    /**
     * @param p a Position of the list
     * @return the Position preceding p or null
     * @throws IllegalArgumentException if p is not a valid Position
     */
    public Position<E> before(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return position(node.getPrev());
    }

    /**
     * @param p a Position of the list
     * @return the Position of the following ellement or null
     * @throws IllegalArgumentException if p is not a valid Position
     */
    public Position<E> after(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return position(node.getNext());
    }

    /**
     * Adds element to linked list between given nodes. Returns the newly
     * created Node as a Position.
     *
     * @param e the element to be added to the list
     * @param pred the Node that should precede the new Node in the list
     * @param succ the Node that should follow the new Node in the list
     * @return the newly created Node
     */
    private Position<E> addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newest = new Node<>(e, pred, succ); // create and link new Node
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
        return newest;
    }

    /**
     * Inserts element at front of list.
     *
     * @param e the new element
     * @return the Position representing the location of the new element
     */
    public Position<E> addFirst(E e) { return addBetween(e, header, header.getNext()); }

    /**
     * Inserts an element at the end of a list.
     *
     * @param e the new element
     * @return the Position representing the location of the new element
     */
    public Position<E> addLast(E e) { return addBetween(e, trailer.getPrev(), trailer); }

    /**
     * Inserts an element immediately before a given Position.
     *
     * @param p the Position to follow the new element
     * @param e the new element
     * @return the Position representing the location of the new element
     * @throws IllegalArgumentException if p is not a valid Position in the list
     */
    public Position<E> addBefore(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return addBetween(e, node.getPrev(), node);
    }

    /**
     * Inserts a new element immediately after a given Position in the list.
     *
     * @param p the Position to precede the new element
     * @param e the new element
     * @return the Position representing the location of the new element
     * @throws IllegalArgumentException if p is not a valid Position for this list
     */
    public Position<E> addAfter(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return addBetween(e, node, node.getNext());
    }

    /**
     * Replaces the element at a given Position with a new element and returns replaced element.
     *
     * @param p the Position of the element to be replaced
     * @param e the new element
     * @return the replaced element
     * @throws IllegalArgumentException if p is not a valid Position in the list
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        E answer = node.getElement();
        node.setElement(e);
        return answer;
    }

    /**
     * Invalidates a given Position from the list and returns its element
     *
     * @param p the Position to be removed
     * @return the element contained at Position p
     * @throws IllegalArgumentException if P is not a valid Position for this list
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        E answer = node.getElement();
        node.setElement(null);      // Help with garbage collection
        node.setPrev(null);
        node.setNext(null);         // Convention for defunct node
        return answer;
    }

    public void invalidate(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        node.setElement(null);
        node.setPrev(null);
        node.setNext(null);
    }

    // nested PositionIterator class
    private class PositionIterator implements Iterator<Position<E>> {
        /** A Position of the containing list, initialized to first Position */
        private Position<E> cursor = first();   // position of the next element to report

        /** A Position of the most recent element reported (if any) */
        private  Position<E> recent = null;     // position of last reported element

        /**
         * Tests whether iterator has a next object
         * @return true if there are additional objects to iterate
         */
        public boolean hasNext() { return (cursor != null); }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public Position<E> next() throws NoSuchElementException {
            if(cursor == null) throw new NoSuchElementException("nothign left");
            recent = cursor;        // element at this Position might later be removed
            cursor = after(cursor);
            return recent;
        }

        public void remove() throws IllegalStateException {
            if (recent == null) throw new IllegalStateException("nothing to remove");
            LinkedPositionalList.this.remove(recent);   // remove from outer list
            recent = null;          // do not allow remove again until next is called
        }
    }

    private class PositionIterable implements Iterable<Position<E>> {
        public Iterator<Position<E>> iterator() { return new PositionIterator(); }
    }

    public Iterable<Position<E>> positions() { return new PositionIterable(); }

    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = new PositionIterator();
        public boolean hasNext() { return posIterator.hasNext(); }
        public E next() { return posIterator.next().getElement(); } // return element
        public void remove() { posIterator.remove(); }
    }

    /**
     * Returns an iterator of elements stored in the list.
     *
     * @return iterator of list's elements
     */
    public Iterator<E> iterator() { return new ElementIterator(); }

    /**
     * Returns Positions in list in iterable form from first to last.
     *
     * @return iterable collection of Positions in list.
     */

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = header.getNext();
        while (walk != trailer) {
            sb.append(walk.getElement());
            walk = walk.getNext();
            if (walk != trailer) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
