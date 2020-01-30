package net.nowtryz.datastorage.vertices;

import net.nowtryz.datastorage.vertices.Node;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class User extends Node {
    private static int current_id = 0;
    private int[] interests;

    public User(int... interests) {
        super(current_id++);
        this.interests = interests;
    }

    public int[] getInterests() {
        return interests;
    }

    @Override
    public Node[] getNeighbours() {
        return new Node[0];
    }

    @Override
    public String toString() {
        String ids = Arrays.stream(interests).mapToObj(Integer::toString).collect(Collectors.joining(", "));
        return "User(" + this.id + ") " +
                "interests(" + ids + ')';
    }
}
