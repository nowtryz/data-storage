package net.nowtryz.datastorage;

import net.nowtryz.datastorage.graphs.DataGraph;
import net.nowtryz.datastorage.graphs.SimpleWeightedGraph;
import net.nowtryz.datastorage.gui.GraphWindow;
import net.nowtryz.datastorage.vertices.Node;
import net.nowtryz.datastorage.vertices.SystemNode;
import net.nowtryz.datastorage.vertices.User;

public class DataStorage {
    public static void main(String[] args) {
        DataGraph graph = new DataGraph();

        // Add system nodes
        SystemNode
            node0 = new SystemNode(50),
            node1 = new SystemNode(40),
            node2 = new SystemNode(40);
        SystemNode[] nodes = new SystemNode[]{node0, node1, node2};
        for (SystemNode node: nodes) graph.addVertex(node);

        // create data
        new Data(40); // data 0
        new Data(25); // data 1
        new Data(25); // data 2

        // add users
        User user0 = new User(0, 1, 2);
        graph.addVertex(user0);

        // create edges
        graph.addEdge(node0, node1, 1);
        graph.addEdge(node1, node2, 1);
        graph.addEdge(user0, node0, 2);

        // place data
        try {
            graph.placeData(user0);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

        new GraphWindow<>(graph).display();
    }
}
