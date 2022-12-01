import java.util.Collection;

public class AdjacencyListGraph<V, E> implements Graph<V, E> {


    private boolean isDirected;
    private PositionalList<Vertex<V>> vertices = new LinkedPositionalList<>();
    private PositionalList<Edge<E>> edges = new LinkedPositionalList<>();

    public AdjacencyListGraph(boolean directed) { isDirected = directed; }

    /** A vertex of an adjacency list graph representation */
    private class InnerVertex<V> implements Vertex<V> {
        private V element;
        private Position<Vertex<V>> pos;
        private LinkedPositionalList<Edge<E>> outgoing, incoming;

        /** Constructs a new InnerVertex instance storing given element */
        public InnerVertex(V elem, boolean graphIsDirected) {
            element = elem;
            outgoing = new LinkedPositionalList<>();
            if(graphIsDirected) incoming = new LinkedPositionalList<>();
            else incoming = outgoing;
        }

        /** Validates that this vertex instance belongs to the given graph */
        public boolean validate(Graph<V,E> graph) { return (AdjacencyListGraph.this == graph && pos != null); }

        /** returns the element associated with the vertex */
        public V getElement() { return element; }

        /** Stores the position of this vertex within the graph's vertex list */
        public void setPosition(Position<Vertex<V>> p) { pos = p; }

        /** Returns the position of this vertex within the graph's vertex list */
        public Position<Vertex<V>> getPosition() { return pos; }

        /** Returns reference to the underlying list of outgoing edges */
        public PositionalList<Edge<E>> getOutgoing() { return outgoing; }

        /** Returns reference to the underlying list of incoming edges */
        public PositionalList<Edge<E>> getIncoming() { return incoming; }
    }

    /** An edge between two vertices */
    private class InnerEdge<E> implements Edge<E> {
        private E element;
        private Position<Edge<E>> pos;
        private Vertex<V>[] endpoints;

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

    public int numVertices() { return vertices.size(); }

    public Iterable<Vertex<V>> vertices() { return vertices; }

    public int numEdges() { return edges.size(); }

    public Iterable<Edge<E>> edges() { return edges; }

    public Edge<E> getEdge(Vertex<V> u, Vertex<V> v) throws IllegalArgumentException {

        InnerVertex<V> origin = validate(u);
        // return origin.getOutgoing().get(v);
        Vertex[] endpoints = {u,v};
        Vertex[] endpointsSwapped = {v,u};
        Iterable<Edge<E>> existingEdges = origin.getOutgoing();
        for(Edge<E> edge : existingEdges) {
            Vertex[] currentEndpoints = ((InnerEdge<E>) edge).endpoints;
            if(currentEndpoints[0] == endpoints[0] && currentEndpoints[1] == endpoints[1]) return edge;
            if(currentEndpoints[0] == endpointsSwapped[0] && currentEndpoints[1] == endpointsSwapped[1]) return edge;
        }
        return null;
    }

    public Vertex<V>[] endVertices(Edge<E> e) throws IllegalArgumentException {
        return new Vertex[0];
    }

    public Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException {
        InnerEdge<E> edge = validate(e);
        Vertex<V>[] endpoints = edge.getEndpoints();
        if(endpoints[0] == v) return endpoints[1];
        else if(endpoints[1] ==v) return endpoints[0];
        else throw new IllegalArgumentException("v is not incident to this edge");
    }

    public int outDegree(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getOutgoing().size();
    }

    public int inDegree(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getIncoming().size();
    }

    public Iterable<Position<Edge<E>>> outgoingEdges(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getOutgoing().positions();
    }

    public Iterable<Position<Edge<E>>> incomingEdges(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        return vert.getIncoming().positions();
    }

    public Vertex<V> insertVertex(V element) {
        InnerVertex<V> v = new InnerVertex<>(element, isDirected);
        v.setPosition(vertices.addLast(v));
        return v;
    }

    public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) throws IllegalArgumentException {
        if(getEdge(u,v) == null) {
            InnerEdge<E> e = new InnerEdge<>(u, v, element);
            e.setPosition(edges.addLast(e));
            InnerVertex<V> origin = validate(u);
            InnerVertex<V> dest = validate(v);
            origin.getOutgoing().addLast(e);
            dest.getIncoming().addLast(e);
            return e;
        } else throw new IllegalArgumentException("Edge from u to v already exists");
    }

    public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
        InnerVertex<V> vert = validate(v);
        // remove all incident edges from the graph
        for (Position<Edge<E>> e : vert.getOutgoing().positions()) removeEdge(e.getElement());
        for (Position<Edge<E>> e : vert.getIncoming().positions()) removeEdge(e.getElement());
        vertices.remove(vert.getPosition());
        vert.setPosition(null);
    }

    public void removeEdge(Edge<E> e) throws IllegalArgumentException {
        InnerEdge<E> edge = validate(e);
        // remove this edge from vertices' adjacencies
        InnerVertex<V>[] verts = (InnerVertex<V>[])edge.getEndpoints();
        verts[0].getOutgoing().remove(edge.getPosition());
        verts[1].getIncoming().remove(edge.getPosition());
        // remove this edge from the list of edges
        edges.remove(edge.getPosition());
        edge.setPosition(null);
    }

    private InnerVertex<V> validate(Vertex<V> v) {
        if (!(v instanceof InnerVertex)) throw new IllegalArgumentException("Invalid vertex");
        InnerVertex<V> vert = (InnerVertex<V>) v;   // safe cast
        if (!vert.validate(this)) throw new IllegalArgumentException("Invalid vertex");
        return vert;
    }

    private InnerEdge<E> validate(Edge<E> e) {
        if (!(e instanceof InnerEdge)) throw new IllegalArgumentException("Invalid edge");
        InnerEdge<E> edge = (InnerEdge<E>) e;   // safe cast
        if (!edge.validate(this)) throw new IllegalArgumentException("Invalid edge");
        return edge;
    }

    public Iterable<Edge<E>> friendsList(Vertex<V> v) {
        InnerVertex<V> vertex = validate(v);
        return vertex.getOutgoing();
    }
}
