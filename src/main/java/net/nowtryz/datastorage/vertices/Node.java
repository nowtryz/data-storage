package net.nowtryz.datastorage.vertices;

import java.util.Collections;

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

    public int getId() {
        return this.id;
    }
    public abstract Node[] getNeighbours();
}
