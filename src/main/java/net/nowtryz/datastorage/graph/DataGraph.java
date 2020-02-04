package net.nowtryz.datastorage.graph;

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

import static net.nowtryz.datastorage.util.Utils.arrayContains;

/**
 * Specific graph to hold system nodes and users
 */
public class DataGraph extends SimpleWeightedGraph<Node> {
    /**
     * Iterates on the given iterator until it encounter a SystemNode
     * @param iterator the iterator to iterate
     * @return the SystemNode encountered
     */
    private static SystemNode nextSystemNode(ClosestFirstIterator<Node, DefaultWeightedEdge> iterator) {
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
     * Collect all data users of the graph are interested in and place them and the right position in order to let it be
     * the closest possible of all interested users.<br>
     * This method does not care of the <i>MKP (Multiple Knapsack Problem)</i>
     */
    public void placeData() {
        // get a user stream to manipulate users of the graph
        User[] users = this.vertexSet().stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .toArray(User[]::new);

        // retrieve data of each user
        Arrays.stream(users).parallel()
                .map(User::getInterests)
                // get a list of all distinct data id needed in the graph
                .map(Arrays::stream)
                .flatMap(IntStream::boxed)
                .distinct()
                // get Data objects from the list of id
                .filter(Objects::nonNull)
                .map(Data::getFromId)
                // for each data, get interested users and call placeData for the specific data
                .forEach(data -> this.placeSpecificData(data, Arrays.stream(users)
                        .filter(user -> arrayContains(user.getInterests(), data.getId()))
                        .toArray(User[]::new)
                ));
    }

    /**
     * Place all interests of the given user on the graph
     * @param user the user from which to take interests
     */
    public void placeUserData(User user) {
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

    /**
     * Place a specific data, the nearest possible of all given users. To place the data, we will give a score to
     * each node, which is the sum of the inverse of distances from all interested users and we take the lowest score node.
     * @param data the data to place
     * @param users users that are interested in the data
     */
    protected void placeSpecificData(Data data, User[] users) {
        final Map<SystemNode, Double> scores = this.vertexSet()
                .stream()
                // only keep system nodes that have enough space to receive the data
                .filter(SystemNode.class::isInstance)
                .map(SystemNode.class::cast)
                .filter(x -> x.hasEnoughSpace(data.getSize()))
                // get the score of each nodes based on the sum of edges' weight from each user
                .collect(Collectors.toMap(x -> x, x -> Arrays
                        .stream(users)
                        .mapToDouble(u -> DijkstraShortestPath.findPathBetween(this, u, x).getWeight())
                        .map(d -> 1/d)
                        .sum()
                ));

        // find the node with the best score
        final Optional<SystemNode> bestNode = scores.keySet().stream().min(Comparator.comparingDouble(scores::get));

        // throw an error if there's no such node
        if (!bestNode.isPresent()) throw new RuntimeException("There isn't enough space");

        // add the data to the best node
        bestNode.get().addToStorage(data.getId());
    }
}
