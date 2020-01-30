package net.nowtryz.datastorage.graphs;

import net.nowtryz.datastorage.Data;
import net.nowtryz.datastorage.vertices.Node;
import net.nowtryz.datastorage.vertices.SystemNode;
import net.nowtryz.datastorage.vertices.User;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DataGraph extends SimpleWeightedGraph<Node> {
    /**
     * Connect a user to the graph
     * @param user the user to connect to the graph
     * @param systemNode the systemNode to which the user will be connected
     * @param weight the weight to set on the newly created edge
     * @see org.jgrapht.Graph#getEdgeSupplier() Graph#getEdgeSupplier()
     * @see SimpleWeightedGraph#addEdge(Object, Object, double)
     * @return The newly created edge if added to the graph, otherwise null.
     */
    public DefaultWeightedEdge addEdge(User user, Node systemNode, double weight) {
        if (systemNode instanceof User) throw new IllegalArgumentException("You cannot connect a User to another User");
        return super.addEdge(user, systemNode, weight);
    }

    /**
     * Connect a user to the graph
     * @param systemNode the systemNode to which the user will be connected
     * @param user the user to connect to the graph
     * @param weight the weight to set on the newly created edge
     * @see org.jgrapht.Graph#getEdgeSupplier() Graph#getEdgeSupplier()
     * @see SimpleWeightedGraph#addEdge(Object, Object, double)
     * @return The newly created edge if added to the graph, otherwise null.
     */
    public DefaultWeightedEdge addEdge(Node systemNode, User user, double weight) {
        return this.addEdge(user, systemNode, weight);
    }

    public void placeData(User user) {
        ClosestFirstIterator<Node, DefaultWeightedEdge> iterator = new ClosestFirstIterator<>(this, user);
        SystemNode node = null;
        for (Data data: Arrays.stream(user.getInterests()).mapToObj(Data::getFromId).collect(Collectors.toList())) {
            while (iterator.hasNext() && (node == null || !node.hasEnoughSpace(data.getSize()))) {
                node = nextSystemNode(iterator);
            }
            if (node == null) throw new RuntimeException("There isn't enough space");
            node.addToStorage(data.getId());
        }
    }

    private static SystemNode nextSystemNode(ClosestFirstIterator<Node, DefaultWeightedEdge> iterator) {
        Node node;
        while (iterator.hasNext()) {
            node = iterator.next();
            if (node instanceof SystemNode) return (SystemNode) node;
        }
        return null;
    }
}
