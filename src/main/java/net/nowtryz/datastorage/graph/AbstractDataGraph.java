package net.nowtryz.datastorage.graph;

import com.sun.istack.internal.NotNull;
import net.nowtryz.datastorage.entity.Data;
import net.nowtryz.datastorage.entity.Node;
import net.nowtryz.datastorage.entity.SystemNode;
import net.nowtryz.datastorage.entity.User;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.nowtryz.datastorage.util.ArraysUtils.arrayContains;

/**
 * Specific graph to hold system nodes and users
 */
public abstract class AbstractDataGraph extends SimpleWeightedGraph<Node> {
    /**
     * Iterates on the given iterator until it encounter a SystemNode
     * @param iterator the iterator to iterate
     * @return the SystemNode encountered
     */
    protected static SystemNode nextSystemNode(ClosestFirstIterator<Node, DefaultWeightedEdge> iterator) {
        Node node;
        while (iterator.hasNext()) {
            node = iterator.next();
            if (node instanceof SystemNode) return (SystemNode) node;
        }
        return null;
    }
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

    /**
     * Filter vertices supplied to the graph to fetch all users and return them as a array
     * @return an array of User
     */
    public User[] getUsers() {
        return this.vertexSet().stream()
            .filter(User.class::isInstance)
            .map(User.class::cast)
            .toArray(User[]::new);
    }

    /**
     * Filter vertices supplied to the graph to fetch all system nodes and return them in the resulting stream
     * @return a stream of system nodes
     */
    protected Stream<SystemNode> getSystemNodes() {
        return this.vertexSet().stream()
                // only keep system nodes
                .filter(SystemNode.class::isInstance)
                .map(SystemNode.class::cast);
    }

    /**
     * Fetch all date the given users are interested in. The resulting {@link Stream} doesn't contain any duplicates or
     * null values.
     * @param users the users from which to gather interests
     * @return a not yet used stream of Data
     */
    @NotNull
    protected List<Data> getData(@NotNull  User[] users) {
        return Arrays.stream(users)
                .map(User::getInterests)
                // get a list of all distinct data id needed in the graph
                .map(Arrays::stream)
                .flatMap(IntStream::boxed)
                .distinct()
                // get Data objects from the list of id
                .filter(Objects::nonNull)
                .map(Data::getFromId)
                .collect(Collectors.toList());
    }

    /**
     * Compute the score for a data that would be placed on the given node and which all given users are interested in.
     *
     * <p>The <i>profit</i> of each {@link Data} is obtained by computing the inverse of the quadratic mean distances
     * from all interested users to the node they are placed in and their <i>weight</i> are simply their size.
     * @param node the node which the score is calculated from
     * @param users all users interested in the data
     * @return the score of the the data on the given node
     */
    protected double computeScores(SystemNode node, User[] users) {
        return Arrays
                .stream(users)
                .mapToDouble(u -> DijkstraShortestPath.findPathBetween(this, u, node).getWeight())
                .map(d -> 1 / (d * d))
                .sum();
    }

    abstract public void placeData();
}
