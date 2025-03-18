package com.example.demo;

import java.util.Comparator;
import java.util.TreeMap;

public class TreeMapExample {
    public static void main(String[] args) {
        // 使用自然顺序
        TreeMap<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "Three");
        treeMap.put(1, "One");
        treeMap.put(2, "Two");
        System.out.println("TreeMap: " + treeMap); // TreeMap: {1=One, 2=Two, 3=Three}

        // 使用自定义比较器
        TreeMap<Integer, String> reverseTreeMap = new TreeMap<>(Comparator.reverseOrder());
        reverseTreeMap.put(3, "Three");
        reverseTreeMap.put(1, "One");
        reverseTreeMap.put(2, "Two");
        System.out.println("Reverse TreeMap: " + reverseTreeMap); // Reverse TreeMap: {3=Three, 2=Two, 1=One}

        // 获取子映射
        System.out.println("SubMap (1, 3): " + treeMap.subMap(1, 3)); // SubMap (1, 3): {1=One, 2=Two}

        // 获取前缀映射：小于 toKey=2 的键值对
        System.out.println("HeadMap (2): " + treeMap.headMap(2)); // HeadMap (2): {1=One}

        // 获取后缀映射：大于等于 toKey=2 的键值对
        System.out.println("TailMap (2): " + treeMap.tailMap(2)); // TailMap (2): {2=Two, 3=Three}
    }
}

