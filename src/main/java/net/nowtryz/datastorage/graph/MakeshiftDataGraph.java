package net.nowtryz.datastorage.graph;

import net.nowtryz.datastorage.entity.Data;
import net.nowtryz.datastorage.entity.Node;
import net.nowtryz.datastorage.entity.SystemNode;
import net.nowtryz.datastorage.entity.User;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.traverse.ClosestFirstIterator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.nowtryz.datastorage.util.ArraysUtils.arrayContains;

public class MakeshiftDataGraph extends AbstractDataGraph {

    /**
     * Collect all data users of the graph are interested in and place them and the right position in order to let it be
     * the closest possible of all interested users.<br>
     * This method does not care of the <i>MKP (Multiple Knapsack Problem)</i>
     */
    public void placeData() {
        // get a user stream to manipulate users of the graph
        User[] users = this.getUsers();

        // retrieve data of each user
        this.getData(users)
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
     * each node, which is obtained by computing the quadratic mean of distances from all interested users to the node
     * they are placed in and we take the lowest score node.
     * This method does not care of the <i>MKP (Multiple Knapsack Problem)</i>
     * @param data the data to place
     * @param users users that are interested in the data
     */
    private void placeSpecificData(Data data, User[] users) {
        final Map<SystemNode, Double> scores = this.getSystemNodes()
                // only keep system nodes that have enough space to receive the data
                .filter(x -> x.hasEnoughSpace(data.getSize()))
                // get the score of each nodes based on the sum of edges' weight from each user
                .collect(Collectors.toMap(x -> x, x -> Arrays
                        .stream(users)
                        .mapToDouble(u -> DijkstraShortestPath.findPathBetween(this, u, x).getWeight())
                        .map(d -> d * d)
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
