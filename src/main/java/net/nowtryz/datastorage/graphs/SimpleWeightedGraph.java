package net.nowtryz.datastorage.graphs;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.function.Supplier;

/**
 * Implementation of {@link org.jgrapht.graph.SimpleWeightedGraph SimpleWeightedGraph} that facilitate the manipulation
 * of weighed edges.
 * @param <V> the graph's vertex type
 */
public class SimpleWeightedGraph<V> extends org.jgrapht.graph.SimpleWeightedGraph<V, DefaultWeightedEdge> {
    /**
     * Creates a new simple weighted graph.
     */
    public SimpleWeightedGraph() {
        super(DefaultWeightedEdge.class);
    }

    /**
     * Creates a new simple weighted graph.
     * @param vertexSupplier the vertex supplier, can be null
     * @param edgeSupplier the edge supplier, can be null
     */
    public SimpleWeightedGraph(Supplier<V> vertexSupplier, Supplier<DefaultWeightedEdge> edgeSupplier) {
        super(vertexSupplier, edgeSupplier);
    }

    /**
     * Creates a new edge in this graph, going from the source vertex to the target vertex with the provided weight, and
     * returns the created edge. Some graphs do not allow edge-multiplicity. In such cases, if the graph already
     * contains an edge from the specified source to the specified target, than this method does not change the graph
     * and returns null. The source and target vertices must already be contained in this graph. If they are not found
     * in graph, {@link IllegalArgumentException} is thrown. This method creates the new edge e using this graph's edge
     * supplier *(see Graph.getEdgeSupplier()). For the new edge to be added e must not be equal to any other edge the
     * graph (even if the graph allows edge-multiplicity). More formally, the graph must not contain any edge e2 such
     * that e2.equals(e). If such e2 is found then the newly created edge e is abandoned, the method leaves this graph
     * unchanged and returns null. If the underlying graph implementation's Graph.getEdgeSupplier() returns null, then
     * this method cannot create edges and throws an UnsupportedOperationException.
     * @param sourceVertex source vertex of the edge.
     * @param targetVertex target vertex of the edge.
     * @param weight the weight to set on the newly created edge
     * @see org.jgrapht.Graph#getEdgeSupplier() Graph#getEdgeSupplier()
     * @return The newly created edge if added to the graph, otherwise null.
     */
    public DefaultWeightedEdge addEdge(V sourceVertex, V targetVertex, double weight) {
        DefaultWeightedEdge edge = super.addEdge(sourceVertex, targetVertex);
        super.setEdgeWeight(edge, weight);
        return edge;
    }
}
