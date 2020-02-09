package net.nowtryz.datastorage;

import javafx.util.converter.IntegerStringConverter;
import net.nowtryz.datastorage.entity.Data;
import net.nowtryz.datastorage.entity.SystemNode;
import net.nowtryz.datastorage.entity.User;
import net.nowtryz.datastorage.graph.DataGraph;
import net.nowtryz.datastorage.gui.GraphWindow;

import javax.print.attribute.IntegerSyntax;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataStorage {
    // create system nodes
    static SystemNode
            node0 = new SystemNode(50),
            node1 = new SystemNode(40),
            node2 = new SystemNode(40),
            node3 = new SystemNode(40);

    public static void main(String[] args) {
        // create data
        new Data(40); // data 0
        new Data(25); // data 1
        new Data(25); // data 2

        // new GraphWindow<>(question2()).display("question 2");
        // new GraphWindow<>(question3()).display("question 3");
        new GraphWindow<>(question4()).display("question 4");
    }

    static DataGraph question2() {
        DataGraph graph = new DataGraph();

        // Add system nodes
        SystemNode[] nodes = {node0, node1, node2};
        for (SystemNode node: nodes) graph.addVertex(node);

        // add users
        User user0 = new User(0, 1, 2);
        graph.addVertex(user0);

        // create edges
        graph.addEdge(node0, node1, 1);
        graph.addEdge(node1, node2, 1);
        graph.addEdge(user0, node0, 2);

        // place data
        try {
            graph.placeUserData(user0);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

        return graph;
    }

    static DataGraph question3() {
        DataGraph graph = new DataGraph();

        // Add system nodes
        SystemNode[] nodes = {node0, node1, node2, node3};
        for (SystemNode node: nodes) graph.addVertex(node);

        // add users
        User user0 = new User(0);
        User user1 = new User(0);
        graph.addVertex(user0);
        graph.addVertex(user1);

        // create edges
        graph.addEdge(user0, node0, 2);
        graph.addEdge(node0, node1, 1);
        graph.addEdge(node1, node2, 1);
        graph.addEdge(node2, node3, 1);
        graph.addEdge(node3, user1, 2);

        // place data
        try {
            graph.placeData();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }


        return graph;
    }

    private static DataGraph question4() {
        DataGraph graph = new DataGraph();

        // Add system nodes
        SystemNode[] nodes = {node0, node1, node2};
        for (SystemNode node: nodes) graph.addVertex(node);

        // add users
        User user0 = new User(0, 1, 2);
        graph.addVertex(user0);

        // create edges
        graph.addEdge(user0, node0, 2);
        graph.addEdge(node0, node1, 1);
        graph.addEdge(node1, node2, 1);

        // place data
        try {
            graph.placeData();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

        System.out.println(graph.getData(new User []{user0}).mapToInt(Data::getId)
                .collect(
                    StringBuilder::new,
                    StringBuilder::appendCodePoint,
                    StringBuilder::append)
                .toString());

        return graph;
    }
}
