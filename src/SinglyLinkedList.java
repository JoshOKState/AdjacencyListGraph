public class SinglyLinkedList<E> implements Cloneable {

    // Nested Node class
    private static class Node<E> {
        private E element;

        private Node<E> next;

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        /** @return element stored at node */
        public E getElement() { return element; }

        /** @return pointer to next node in list */
        public Node<E> getNext() { return next; }

        /** sets next node in list with given node */
        public void setNext(Node<E> n) { next = n; }
    }

    // Instance variables
    private Node<E> head = null;

    private Node<E> tail = null;

    private int size = 0;

    // Constructors
    public SinglyLinkedList() { }

    // Accessors

    /**
     * Return number of items in list
     * @return size of list
     */
    public int size() { return size; }

    /**
     * Checks if list is empty
     * @return true if list is empty, false otherwise
     */
    public boolean isEmpty() { return size == 0; }

    /** @return first item in list or null if none exists */
    public E first() {
        if (isEmpty()) return null;
        return head.getElement();
    }

    /** @return last item in list or null if none exists */
    public E last() {
        if (isEmpty()) return null;
        return tail.getElement();
    }

    /** Insert a node with given element into the list */
    public void addFirst(E e) {
        head = new Node<>(e, head);
        if (size == 0)
            tail = head;
        size++;
    }

    /**
     * Insert a node with given element at end of list
     * @param e the element to be stored in the list
     */
    public void addLast(E e) {
        Node<E> newest = new Node<>(e, null);
        if (isEmpty()) head = newest;
        else tail.setNext(newest);
        tail = newest;
        size++;
    }

    /**
     * Returns and removes the first item in list if exists
     * @return element of first item in list or null if none exists
     */
    public E removeFirst() {
        if (isEmpty()) return null;
        E answer = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0) tail = null;
        return answer;
    }

    /**
     * Returns true if both lists are the same
     * @param o a list to compare
     * @return true if all elements in list are same, false if differing item found
     */
    public boolean equals(Object o) {
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        SinglyLinkedList other = (SinglyLinkedList) o;
        if (size != other.size) return false;
        Node walkA = head;
        Node walkB = other.head;
        while (walkA != null) {
            if (!walkA.getElement().equals(walkB.getElement())) return false;
            walkA = walkA.getNext();
            walkB = walkB.getNext();
        }
        return true;
    }

    /**
     * Clones the list
     * @return a new clone of current list
     * @throws CloneNotSupportedException if Clone not supported for Object type
     */
    public SinglyLinkedList<E> clone() throws CloneNotSupportedException {
        // Always use inherited Object.clone() to create initial copy
        SinglyLinkedList<E> other = (SinglyLinkedList<E>) super.clone();
        if (size > 0) {
            other.head = new Node<>(head.getElement(), null);
            Node<E> walk = head.getNext();
            Node<E> otherTail = other.head;
            while (walk != null) {
                Node<E> newest = new Node<>(walk.getElement(), null);
                otherTail.setNext(newest);
                otherTail = newest;
                walk = walk.getNext();
            }
        }
        return other;
    }

    /**
     * Calculates hash code for List Nodes
     * @return
     */
    public int hashCode() {
        int h = 0;
        for (Node walk = head; walk != null; walk = walk.getNext()) {
            h ^= walk.getElement().hashCode();
            h = (h << 5) | (h >>> 27);
        }
        return h;
    }

    // For debugging purposes
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = head;
        while (walk != null) {
            sb.append(walk.getElement());
            if (walk != tail)
                sb.append(", ");
            walk = walk.getNext();
        }
        sb.append(")");
        return sb.toString();
    }
}
