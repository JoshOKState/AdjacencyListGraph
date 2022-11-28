public interface Graph<V, E> {
    /** Returns number of vertices in graph */
    int numVertices();

    /** Returns an iteration of all vertices in the graph */
    Iterable<Vertex<V>> vertices();

    /** Returns number of edges in graph */
    int numEdges();

    /** Returns iteration of all edges of the graph */
    Iterable<Edge<E>> edges();

    /**
     * Returns edge from Vertex u to Vertex v if exists; otherwise return null
     *
     * @param u a Vertex in the Graph
     * @param v a Vertex in the Graph
     * @return edge from u to v if they are adjacent; null otherwise
     * @throws IllegalStateException if u or v are invalid vertices
     */
    Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws IllegalArgumentException;

    /**
     * Returns array containing two endpoint vertices of given Edge e
     *
     * @param e an Edge in the Graph
     * @return array of length 2 containing vertices of Edge e
     * @throws IllegalArgumentException if Edge e is not a valid Edge in the Graph
     */
    Vertex<V>[] endVertices(Edge<E> e) throws IllegalArgumentException;

    /**
     * For Edge e incident to Vertex v, returns the other Vertex of the Edge. Error occurs if e is not incident to v
     *
     * @param v a Vertex in the Graph
     * @param e an Edge in the Graph incident to v
     * @return the other Vertex connected to Edge e
     * @throws IllegalArgumentException if v or e are not valid in the Graph, or if e is not incident to v
     */
    Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException;

    /**
     * Returns number of edges leaving given Vertex v
     * For undirected graph, this is same result returned by inDegree.
     *
     * @param v a Vertex in the Graph
     * @return the number of Edges leaving given Vertex v
     * @throws IllegalArgumentException if V is not a valid Vertex in the Graph.
     */
    int outDegree(Vertex<V> v) throws IllegalArgumentException;

    /**
     * Returns number of incoming edges to given Vertex v
     * For undirected graph, this is same result returned by outDegree.
     *
     * @param v a Vertex in the Graph
     * @return the number of incoming edges to given Vertex v
     * @throws IllegalArgumentException if V is not a valid Vertex in the Graph
     */
    int inDegree(Vertex<V> v) throws IllegalArgumentException;

    /**
     * Returns an iterable collection of all outgoing edges from a given Vertex v
     * For undirected graph, this is same result returned by incomingEdges
     *
     * @param v a Vertex in the Graph
     * @return an iterable collection of all outgoing edges from v
     * @throws IllegalArgumentException if V is not a valid Vertex in the Graph
     */
    Iterable<Position<Edge<E>>> outgoingEdges(Vertex<V> v) throws IllegalArgumentException;

    /**
     * Returns an iterable collection of all incoming edges from a given Vertex v
     * For undirected graph, this is same result returned by incomingEdges
     *
     * @param v a Vertex in the Graph
     * @return an iterable collection of all incoming edges from v
     * @throws IllegalArgumentException if V is not a valid Vertex in the Graph
     */
    Iterable<Position<Edge<E>>> incomingEdges(Vertex<V> v) throws IllegalArgumentException;

    /**
     * Creates and returns a new Vertex storing element.
     *
     * @param element the new element to be inserted into the Graph
     * @return the Vertex inserted into the Graph
     */
    Vertex<V> insertVertex(V element);

    /**
     * Creates and returns a new Edge from Vertex u to Vertex v; error occurs if edge already exists.
     *
     * @param u a Vertex in the Graph
     * @param v a Vertex in the Graph not adjacent to u
     * @param element the element stored at the new Edge
     * @return the newly created Edge
     * @throws IllegalArgumentException if u or v are not valid Vertices in the graph, or if Edge already exists.
     */
    Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException;

    /**
     * Removes Vertex v and all incident edges from Graph
     *
     * @param v a Vertex in the Graph
     * @throws IllegalArgumentException if v is not a valid Vertex in the Graph
     */
    void removeVertex(Vertex<V> v) throws IllegalArgumentException;

    /**
     * Removes Edge e from the Graph.
     *
     * @param e the Edge to be removed from the Graph
     * @throws IllegalArgumentException if e not a valid Edge in the Graph
     */
    void removeEdge(Edge<E> e) throws IllegalArgumentException;
}
