package net.nowtryz.datastorage.graph;

import net.nowtryz.datastorage.entity.Data;
import net.nowtryz.datastorage.entity.SystemNode;
import net.nowtryz.datastorage.entity.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.nowtryz.datastorage.util.ArraysUtils.arrayContains;

public class OptimizedDataGraph extends AbstractDataGraph {

    /**
     * To fill the graph with data, having a MKP sight, all data will be processed and temporary  put on the "perfect"
     * node, then the algorithm will keep the best arrangement for each node and loop until no collection is
     * overweight.
     */
    public void placeData() {
        // get a user stream to manipulate users of the graph
        User[] users = this.getUsers();
        List<SystemNode> nodes = this.getSystemNodes().collect(Collectors.toList());
        // retrieve data of each user
        List<Data> dataList = this.getData(users);

        do {
            List<SystemNode> remainingNodes = new ArrayList<>(nodes);
            dataList
                    // for each data, get interested users and call placeData for the specific data
                    .forEach(data -> this.putOnBestSpot(data, remainingNodes, Arrays.stream(users)
                            .filter(user -> arrayContains(user.getInterests(), data.getId()))
                            .toArray(User[]::new)
                    ));

            // store invalid positioned data to position the in the next loop insertion
            dataList = remainingNodes.stream()
                    // retrieve overweight nodes
                    .filter(SystemNode::isOverweight)
                    // remove theme from accessible nodes
                    .peek(nodes::remove)
                    // find the best arrangement for each node and get excess data
                    .flatMap(this::removeInvalidData)
                    // collect them and put them in the datalist for next iteration
                    .collect(Collectors.toList());

            // we can quit the loop if all data are place or if there isn't enough place
        } while (dataList.size() > 0 && nodes.size() > 0);

        if (dataList.size() > 0) throw new RuntimeException("There isn't enough space");
    }

    /**
     * Find the best node to put a data, without taking care of the max weight
     * @param data the data to place
     * @param nodes the available nodes
     * @param users the users interested in the data
     */
    private void putOnBestSpot(Data data, List<SystemNode> nodes, User[] users) {
        final Map<SystemNode, Double> scores = nodes.stream()
                // get the score of each nodes based on the sum of edges' weight from each user
                .collect(Collectors.toMap(x -> x, x -> this.computeScores(x, users)
                ));

        // find the node with the best score
        final Optional<SystemNode> bestNode = scores.keySet().stream().max(Comparator.comparingDouble(scores::get));

        // throw an error if there's no such node
        if (!bestNode.isPresent()) throw new RuntimeException("There isn't enough space");

        // add the data to the best node
        bestNode.get().addToStorage(data.getId());
    }

    /**
     * O-1 Knapsack recursion using dynamic programming to find the best arrangement
     * @param node the "knapsack" to consider
     * @return invalid data that need to be place elsewhere
     */
    private Stream<Data> removeInvalidData(SystemNode node) {
        final Map<Integer, Double> scores = node.getData()
                .stream()
                .filter(Objects::nonNull)
                .map(Data::getFromId)
                .collect(Collectors.toMap(Data::getId, x -> this.computeScores(node, Arrays
                        .stream(this.getUsers())
                        .filter(user -> arrayContains(user.getInterests(), x.getId()))
                        .toArray(User[]::new)
                )));

        Selection result = new KnapSack(scores, new LinkedHashSet<>(node.getData()), node.getCapacity()).startCompute();

        return node.getData()
                .stream()
                .filter(x -> !result.selected.contains(x))
                .peek(node::removeFromStorage)
                .map(Data::getFromId);


    }

    private static class KnapSack {
        Map<Integer, Double> scores;
        Map<Integer, Integer> weights;
        Set<Integer> ids;
        int maxWeight;

        public KnapSack(Map<Integer, Double> scores, Set<Integer> data, int maxWeight) {
            this.ids = data;
            this.scores = scores;
            this.maxWeight = maxWeight;
            this.weights = data.stream()
                    .map(Data::getFromId)
                    .collect(Collectors.toMap(Data::getId, Data::getSize));
        }

        public Selection startCompute() {
            return this.compute(0, new LinkedHashSet<>());
        }

        public Selection compute(int pos, Set<Integer> selected) {
            DoubleAdder totalValue = new DoubleAdder();
            AtomicInteger totalWeight = new AtomicInteger();
            selected.forEach(x -> {
                totalValue.add(scores.get(x));
                totalWeight.addAndGet(weights.get(x));
            });

            /*
             *  Base cases
             */
            // overweight
            if (totalWeight.get() > maxWeight) return new Selection(null, 0, 0);

            // end of recursion
            if (pos >= ids.size()) {
                return new Selection(selected, totalValue.doubleValue(), totalWeight.get());
            }

            /*
             * Recursive case
             */
            final LinkedHashSet<Integer> taken = new LinkedHashSet<>(selected);
            taken.add(pos);
            Selection on  = this.compute(pos + 1, taken);
            Selection off = this.compute(pos + 1, selected);

            if (on.totalValue > off.totalValue) return on;
            else return off;
        }
    }

    private static class Selection {
        Set<Integer> selected;
        double totalValue;
        int totalWeight;

        public Selection(Set<Integer> selected, double totalValue, int totalWeight) {
            this.selected = selected;
            this.totalValue = totalValue;
            this.totalWeight = totalWeight;
        }
    }
}
