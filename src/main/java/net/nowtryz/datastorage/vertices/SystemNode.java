package net.nowtryz.datastorage.vertices;

import net.nowtryz.datastorage.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A SystemNode can hold one or many data depending on it size. The node has it storage capacity that can differ from
 * one node to another. A System node can communicate with other system nodes and some users.
 */
public class SystemNode extends Node {
    private static int current_id = 0;
    private int capacity;
    private int[] storage = new int[0];

    /**
     * Create a SystemNode instance
     * @param capacity the capacity of this node
     */
    public SystemNode(int capacity) {
        super(current_id++);
        this.capacity = capacity;
    }

    /**
     * Create a SystemNode instance
     * @param capacity the capacity of this node
     * @param dataIds the ids of data holden by this node
     */
    public SystemNode(int capacity, int[] dataIds) {
        this(capacity);
        this.storage = dataIds;
    }

    /**
     * Add the data specified by the given id to the holden data of this node
     * @param id the id of the data
     */
    public void addToStorage(int id) {
        List<Integer> newStorage = Arrays.stream(storage).boxed().collect(Collectors.toList());
        newStorage.add(id);
        storage = newStorage.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Get the used space of this node
     * @return the used space of this node
     */
    public int getUsedSpace() {
        return Arrays.stream(storage).mapToObj(Data::getFromId).filter(Objects::nonNull).mapToInt(Data::getSize).sum();
    }

    /**
     *  Get the free space of this node
     * @return the free space
     */
    public int getFreeSpace() {
        return this.capacity - this.getUsedSpace();
    }

    /**
     * Check if the current node has enough free space to store data
     * @param need the space needed by the data
     * @return true if possible
     */
    public boolean hasEnoughSpace(int need) {
        return need <= getFreeSpace();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node[] getNeighbours() {
        return new Node[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SystemNode(" + id + ", " + getUsedSpace() + '/' + capacity + ')';
    }
}
