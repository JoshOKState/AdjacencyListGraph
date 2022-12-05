public interface Queue<E> {

    /** @return the number of elements in the queue */
    int size();

    /** @return true if Queue is empty, false otherwise */
    boolean isEmpty();

    /** add a new element to the back of the queue */
    void enqueue(E e);

    /** @return (but do not remove) the first element in the queue */
    E first();

    /** removes and returns the first element in the queue */
    E dequeue();
}
