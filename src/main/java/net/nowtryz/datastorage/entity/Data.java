package net.nowtryz.datastorage.entity;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private static int current_id = 0;
    private static Map<Integer, Data> dataList = new HashMap<>();
    private int id = current_id++;
    private int size;

    public Data(int size) {
        this.size = size;
        dataList.put(this.id, this);
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public static Data getFromId(int id) {
        return dataList.get(id);
    }
}
