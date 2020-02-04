package net.nowtryz.datastorage.entity;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Users are interested in a list of list of data and can only access to these specific data. They can interact directly
 * with an only node and cannot communicate with other users.
 */
public class User extends Node {
    private static int current_id = 0;
    private int[] interests;

    /**
     * Create a user
     * @param interests the interests of the user
     */
    public User(int... interests) {
        super(current_id++);
        this.interests = interests;
    }

    /**
     * Get interests of the user
     * @return interests of the user
     */
    public int[] getInterests() {
        return interests;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String ids = Arrays.stream(interests).mapToObj(Integer::toString).collect(Collectors.joining(", "));
        return "User(" + this.id + ", interests=(" + ids + "))";
    }
}
