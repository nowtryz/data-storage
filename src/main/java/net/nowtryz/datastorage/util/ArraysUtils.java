package net.nowtryz.datastorage.util;

public class ArraysUtils {
    public static <T> boolean arrayContains(T[] array, T value) {
        for (T obj : array) if (obj.equals(value)) return true;
        return false;
    }

    public static boolean arrayContains(int[] array, int value) {
        for (int i : array) if (i == value) return true;
        return false;
    }
}
