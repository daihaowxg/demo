package io.github.daihaowxg.demo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HashMapExample {
    public static void main(String[] args) throws Exception {
        // 创建一个 HashMap
        HashMap<String, Integer> map = new HashMap<>(4, 0.75f);

        // 添加数据，触发哈希计算 & 插入
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4); // 可能触发扩容
        map.put("E", 5); // 可能触发扩容

        // 打印 HashMap 的底层结构
        printHashMapDetails(map);

        // 遍历方式 1：entrySet 遍历
        System.out.println("\n=== 遍历方式 1：entrySet ===");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // 遍历方式 2：keySet 遍历
        System.out.println("\n=== 遍历方式 2：keySet ===");
        for (String key : map.keySet()) {
            System.out.println("Key: " + key + ", Value: " + map.get(key));
        }

        // 遍历方式 3：values 遍历
        System.out.println("\n=== 遍历方式 3：values ===");
        for (Integer value : map.values()) {
            System.out.println("Value: " + value);
        }
    }

    /**
     * 反射查看 HashMap 内部状态（table, threshold, size, modCount）
     */
    private static void printHashMapDetails(HashMap<?, ?> map) throws Exception {
        Field tableField = HashMap.class.getDeclaredField("table");
        Field thresholdField = HashMap.class.getDeclaredField("threshold");
        Field sizeField = HashMap.class.getDeclaredField("size");
        Field modCountField = HashMap.class.getDeclaredField("modCount");

        tableField.setAccessible(true);
        thresholdField.setAccessible(true);
        sizeField.setAccessible(true);
        modCountField.setAccessible(true);

        Object[] table = (Object[]) tableField.get(map);
        int threshold = (int) thresholdField.get(map);
        int size = (int) sizeField.get(map);
        int modCount = (int) modCountField.get(map);

        System.out.println("\n=== HashMap 内部结构 ===");
        System.out.println("哈希桶数组长度 (table.length): " + (table == null ? 0 : table.length));
        System.out.println("当前元素个数 (size): " + size);
        System.out.println("扩容阈值 (threshold): " + threshold);
        System.out.println("修改次数 (modCount): " + modCount);
    }
}

