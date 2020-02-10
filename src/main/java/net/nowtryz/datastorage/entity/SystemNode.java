package net.nowtryz.datastorage.entity;

import java.util.Arrays;
import java.util.LinkedList;
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
    private List<Integer> storage = new LinkedList<>();

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
        Arrays.stream(dataIds).forEach(this::addToStorage);
    }

    /**
     * Add the data specified by the given id to the holden data of this node
     * @param id the id of the data
     */
    public void addToStorage(int id) {
        storage.add(id);
    }

    /**
     * Remove the data specified by the given id to the holden data of this node
     * @param id the id of the data
     */
    public void removeFromStorage(Integer id) {
        storage.remove(id);
    }

    /**
     * Get the used space of this node
     * @return the used space of this node
     */
    public int getUsedSpace() {
        return this.storage.stream().map(Data::getFromId).filter(Objects::nonNull).mapToInt(Data::getSize).sum();
    }

    /**
     *  Get the free space of this node
     * @return the free space
     */
    public int getFreeSpace() {
        return capacity - getUsedSpace();
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
     * Check weather the capacity of this node has been exceeded.
     * @return if if is is the case, false otherwise
     */
    public boolean isOverweight() {
        return  this.getUsedSpace() > capacity;
    }

    public List<Integer> getData() {
        return new LinkedList<>(storage);
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String ids = this.storage.stream().map(Object::toString).collect(Collectors.joining(", "));
        return "SystemNode(" + id + ", " + getUsedSpace() + '/' + capacity + ", data(" + ids + "))";
    }
}
