import java.util.ArrayList;

public class AdjacencyListGraph<V, E> implements Graph<V, E> {


    private final boolean isDirected;
    private final LinkedPositionalList<Vertex<V>> vertices = new LinkedPositionalList<>();
    private final LinkedPositionalList<Edge<E>> edges = new LinkedPositionalList<>();

    public AdjacencyListGraph(boolean directed) { isDirected = directed; }

    /** A vertex of an adjacency list graph representation */
    private class InnerVertex<V> implements Vertex<V> {
        private final V element;

        private int dfsNum = Integer.MAX_VALUE, low = Integer.MAX_VALUE;
        private Position<Vertex<V>> pos;
        //private LinkedPositionalList<Edge<E>> outgoing, incoming;
        private final ArrayList<Edge<E>> outgoing, incoming;

        /** Constructs a new InnerVertex instance storing given element */
        public InnerVertex(V elem, boolean graphIsDirected) {
            element = elem;
            outgoing = new ArrayList<>();
            if(graphIsDirected) incoming = new ArrayList<>();
            else incoming = outgoing;
        }

        /**
         * Validates that this vertex instance belongs to the given graph
         * @param graph
         * @return
         */
        public boolean validate(Graph<V,E> graph) { return (AdjacencyListGraph.this == graph && pos != null); }

        /**
         * returns the element associated with the vertex
         * @return the element stored at the vertex
         */
        public V getElement() { return element; }

        /**
         * returns the DFS number associated with the vertex after DFS performed,
         * MAX_VALUE until DFS performed
         */
        public int getDfsNum() { return dfsNum; }

        /** Sets DFS num (for use during DFS search) */
        public void setDfsNum(int dfsNum) { this.dfsNum = dfsNum; }

        /** returns the low value calculated when finding connectors, MAX_VALUE by default */
        public int getLow() { return low; }

        /** Sets low value, for use when finding connectors */
        public void setLow(int low) { this.low = low; }

        /** Stores the position of this vertex within the graph's vertex list */
        public void setPosition(Position<Vertex<V>> p) { pos = p; }

        /** Returns the position of this vertex within the graph's vertex list */
        public Position<Vertex<V>> getPosition() { return pos; }

        /** Returns reference to the underlying list of outgoing edges */
        public ArrayList<Edge<E>> getOutgoing() { return outgoing; }

        /** Returns reference to the underlying list of incoming edges */
        public ArrayList<Edge<E>> getIncoming() { return incoming; }

    }

    /** An edge between two vertices */
    private class InnerEdge<E> implements Edge<E> {
        private final E element;
        private Position<Edge<E>> pos;
        private final Vertex<V>[] endpoints;

        /** Constructs InnerEdge instance from u to v, storing given element */
        public InnerEdge(Vertex<V> u, Vertex<V> v, E elem) {
            element = elem;
            endpoints = (Vertex<V>[]) new Vertex[]{u,v};    // array of length 2
        }

        public boolean validate(Graph<V, E> graph) { return (AdjacencyListGraph.this == graph && pos != null); }

        /** Returns element associated with Edge */
        public E getElement() { return element; }

        /** Returns reference to endpoint array */
        public Vertex<V>[] getEndpoints() { return endpoints; }

        /** Stores position of this edge within graph's Vertex list */
        public void setPosition(Position<Edge<E>> p) { pos = p; }

        /** Returns position of this edge within graph's Vertex list */
        public Position<Edge<E>> getPosition() { return pos; }
    }

    /**
     * Returns the number of vertices in the graph
     * @return number of vertices in graph
     */
    public int numVertices() { return vertices.size(); }

    /** @return an iterable collection of all vertices stored in graph */
    public Iterable<Vertex<V>> vertices() { return vertices; }

    /** @return the number of edges in the graph */
    public int numEdges() { return edges.size(); }

    /** @return an iterable collection of all edges stored in graph */
    public Iterable<Edge<E>> edges() { return edges; }

    /**
     * Returns the edge found between given vertices u and v
     * @param u a Vertex in the Graph
     * @param v a Vertex in the Graph
     * @return the edge found between the 2 vertices, or null if none exists
     * @throws IllegalArgumentException if either Vertex is not found in graph
     */
    public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws IllegalArgumentException {
        Vertex[] endpoints = {u,v};
        for(Edge<E> edge : edges) {
            Vertex[] currentEndpoints = ((InnerEdge<E>) edge).endpoints;
            if(currentEndpoints[0] == endpoints[0] && currentEndpoints[1] == endpoints[1]) return edge;
            if(currentEndpoints[0] == endpoints[1] && currentEndpoints[1] == endpoints[0]) return edge;
        }
        return null;
    }

    /**
     * Returns the end vertices of a given Edge
     * @param e an Edge in the Graph
     * @return an array of Vertices representing the endpoints of the Edge
     * @throws IllegalArgumentException if Edge does not exist in graph
     */
    public Vertex<V>[] endVertices(Edge<E> e) throws IllegalArgumentException {
        return new Vertex[0];
    }

    /**
     * Returns the Vertex opposite to a given Vertex and Edge in the graph
     * @param v a Vertex in the Graph
     * @param e an Edge in the Graph incident to v
     * @return the Vertex connected to v by e
     * @throws IllegalArgumentException if v or e are not valid Objects in the graph
     */
    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException {
        InnerEdge<E> edge = validate(e);
        Vertex<V>[] endpoints = edge.getEndpoints();
        if(endpoints[0] == v) return endpoints[1];
        else if(endpoints[1] ==v) return endpoints[0];
        else throw new IllegalArgumentException("v is not incident to this edge");
    }

    /**
     * Returns the number of outgoing Edges from a given Vertex
     * @param v a Vertex in the Graph
     * @return the number of outgoing Edges connected to the Vertex
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public int outDegree(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getOutgoing().size();
    }

    /**
     * Returns the number of incoming Edges to a given Vertex
     * @param v a Vertex in the Graph
     * @return the number of incoming Edges connected to the Vertex
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public int inDegree(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getIncoming().size();
    }

    /**
     * Returns an iterable collection of outgoing Edges from a given Vertex
     * @param v a Vertex in the Graph
     * @return an iterable collection of v's outgoing edges
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public Iterable<Position<Edge<E>>> outgoingEdges(Vertex<V> v) throws IllegalArgumentException {
        return null;
    }

    /**
     * Returns an iterable collection of incoming Edges to a given Vertex
     * @param v a Vertex in the Graph
     * @return an iterable collection of v's incoming edges
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public Iterable<Position<Edge<E>>> incomingEdges(Vertex<V> v) throws IllegalArgumentException {
        return null;
    }

    /**
     * Returns given Vertex's outgoing edge list
     * @param v a Vertex in the graph
     * @return the ArrayList representing v's outgoing edges
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public ArrayList<Edge<E>> outgoingEdgeList(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getOutgoing();
    }

    /**
     * Returns given Vertex's incoming edge list
     * @param v a Vertex in the graph
     * @return the ArrayList representing v's incoming edges
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public ArrayList<Edge<E>> incomingEdgeList(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getIncoming();
    }

    /**
     * Inserts a new Vertex in the Graph
     * @param element the new element to be inserted into the Graph
     * @return the newly created Vertex
     */
    public Vertex<V> insertVertex(V element) {
        InnerVertex<V> v = new InnerVertex<>(element, isDirected);
        v.setPosition(vertices.addLast(v));
        return v;
    }

    /**
     *
     * @param u a Vertex in the Graph
     * @param v a Vertex in the Graph not adjacent to u
     * @param element the element stored at the new Edge
     * @return
     * @throws IllegalArgumentException
     */
    public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException {
        if(getEdge(u,v) == null) {
            InnerEdge<E> e = new InnerEdge<>(u, v, element);
            e.setPosition(edges.addLast(e));
            InnerVertex<V> origin = validate(u);
            InnerVertex<V> dest = validate(v);
            //origin.getOutgoing().addLast(e);
            origin.getOutgoing().add(e);
            //dest.getIncoming().addLast(e);
            dest.getIncoming().add(e);
            return e;
        } else throw new IllegalArgumentException("Edge from u to v already exists");
    }

    /**
     * Removes given Vertex from graph
     * @param v a Vertex in the Graph
     * @throws IllegalArgumentException if v does not exist in graph
     */
    public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        // remove all incident edges from the graph
        for(Edge<E> edge : vert.getOutgoing()) {
            InnerEdge<E> e = validate(edge);
            Vertex<V> opp = opposite(v, edge);
            InnerVertex<V> opposing = (InnerVertex<V>)opp;
            opposing.getIncoming().remove(e);
            edges.remove(e.getPosition());
        }
        vert.getOutgoing().clear();
        for(Edge<E> edge : vert.getIncoming()) {
            InnerEdge<E> e = validate(edge);
            Vertex<V> opp = opposite(v, edge);
            InnerVertex<V> opposing = (InnerVertex<V>)opp;
            opposing.getIncoming().remove(e);
            edges.remove(e.getPosition());
        }
        vert.getIncoming().clear();
        vertices.remove(vert.getPosition());
        vert.setPosition(null);
    }

    /**
     * Removes a given Edge from the Graph
     * @param e the Edge to be removed from the Graph
     * @throws IllegalArgumentException if e is not a valid edge
     */
    public void removeEdge(Edge<E> e) throws IllegalArgumentException {
        InnerEdge<E> edge = validate(e);
        Vertex<V>[] verts = edge.getEndpoints();
        InnerVertex<V>[] innerVerts = new InnerVertex[verts.length];
        for(int i = 0; i < verts.length; ++i) innerVerts[i] = (InnerVertex<V>) verts[i];
        innerVerts[0].getOutgoing().remove(e);
        innerVerts[1].getIncoming().remove(e);
        edges.remove(edge.getPosition());
        edge.setPosition(null);
    }

    /**
     * Checks graph for existence of a given Vertex
     * @param v a Vertex in the Graph
     * @return InnerVertex that matches v
     */
    private InnerVertex<V> validate(Vertex<V> v) {
        if (!(v instanceof InnerVertex)) throw new IllegalArgumentException("Invalid vertex");
        InnerVertex<V> vert = (InnerVertex<V>) v;   // safe cast
        if (!vert.validate(this)) throw new IllegalArgumentException("Invalid vertex");
        return vert;
    }

    /**
     * Checks graph for existence of a given Edge
     * @param e an Edge in the Graph
     * @return InnerEdge that matches e
     */
    private InnerEdge<E> validate(Edge<E> e) {
        if (!(e instanceof InnerEdge)) throw new IllegalArgumentException("Invalid edge");
        InnerEdge<E> edge = (InnerEdge<E>) e;   // safe cast
        if (!edge.validate(this)) throw new IllegalArgumentException("Invalid edge");
        return edge;
    }

    /**
     * Returns an iterable collection of Edges incident to given Vertex
     * @param v a Vertex in the Graph
     * @return an iterable collection of Edges incident to v
     */
    public Iterable<Edge<E>> friendsList(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vertex = validate(v);
        return vertex.getOutgoing();
    }

    // Methods needed to find graph connectors

    public void setDFS(Vertex<V> v, int DFS) throws IllegalArgumentException{
        InnerVertex<V> inner = validate(v);
        inner.setDfsNum(DFS);
    }

    public int getDFS(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> inner = validate(v);
        return inner.getDfsNum();
    }

    public void setLow(Vertex<V> v, int low) throws IllegalArgumentException {
        InnerVertex<V> inner = validate(v);
        inner.setDfsNum(low);
    }

    public int getLow(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> inner = validate(v);
        return inner.getLow();
    }
}