package io.github.daihaowxg.openrewrite.examples;

import java.util.*;

/**
 * 集合操作示例
 * 展示可以被 OpenRewrite 优化的集合使用模式
 */
public class CollectionExample {
    
    /**
     * 低效的集合初始化
     * 可以使用 List.of() 或 Arrays.asList()
     */
    public List<String> createList() {
        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        return list;
    }
    
    /**
     * 低效的 Map 初始化
     */
    public Map<String, Integer> createMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        return map;
    }
    
    /**
     * 可以使用 Stream API 简化
     */
    public List<String> filterAndTransform(List<String> items) {
        List<String> result = new ArrayList<>();
        for (String item : items) {
            if (item.length() > 3) {
                result.add(item.toUpperCase());
            }
        }
        return result;
    }
    
    /**
     * 低效的查找操作
     */
    public boolean containsIgnoreCase(List<String> list, String target) {
        for (String item : list) {
            if (item.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 可以使用 Collections.emptyList()
     */
    public List<String> getEmptyList() {
        return new ArrayList<>();
    }
    
    /**
     * 冗余的 null 检查和空集合创建
     */
    public List<String> processItems(List<String> items) {
        if (items == null) {
            items = new ArrayList<>();
        }
        
        List<String> result = new ArrayList<>();
        if (!items.isEmpty()) {
            for (String item : items) {
                if (item != null && !item.isEmpty()) {
                    result.add(item.trim());
                }
            }
        }
        return result;
    }
    
    /**
     * 使用显式类型参数（可以用菱形操作符）
     */
    public Map<String, List<Integer>> createNestedMap() {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put("numbers", new ArrayList<Integer>());
        return map;
    }
    
    /**
     * 低效的集合复制
     */
    public List<String> copyList(List<String> source) {
        List<String> copy = new ArrayList<>();
        for (String item : source) {
            copy.add(item);
        }
        return copy;
    }
}
