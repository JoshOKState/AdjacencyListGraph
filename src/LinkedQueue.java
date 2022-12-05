public class LinkedQueue<E> implements Queue<E> {
    // Queue implemented as linked list
    private SinglyLinkedList<E> list = new SinglyLinkedList<>();

    public LinkedQueue() { }

    /**
     *
     * @return the number of Objects in the Queue
     */
    @Override
    public int size() {
        return list.size();
    }

    /** @return true if Queue is empty */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /** Adds a new Object at the back of the Queue */
    @Override
    public void enqueue(E e) {
        list.addLast(e);
    }

    /** @return but does not remove first Object in Queue */
    @Override
    public E first() {
        return list.first();
    }

    /**
     * removes and returns the first Object in the queue
     * @return first object in Queue
     */
    @Override
    public E dequeue() {
        return list.removeFirst();
    }

    // For debugging
    public String toString() { return list.toString(); }
}
