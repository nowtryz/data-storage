package net.nowtryz.datastorage.entity;

import net.nowtryz.datastorage.graph.AbstractDataGraph;
import org.jgrapht.Graphs;

import java.util.Collections;
import java.util.List;

public abstract class Node {
    /**
     * The id of the node
     */
    protected int id;

    /**
     * Default constructor of a Node
     * @param id the id of the node
     * @
     */
    public Node(int id) {
        this.id = id;
        Object a = Collections.EMPTY_LIST;
    }

    /**
     * Get for the id property
     * @return the id of the node
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the neighbours of the current node
     * @param graph the graph to explore to search the current node
     * @return a list of nodes connected to the current node
     */
    public List<Node> getNeighbours(AbstractDataGraph graph) {
        return Graphs.neighborListOf(graph, this);
    }
}
