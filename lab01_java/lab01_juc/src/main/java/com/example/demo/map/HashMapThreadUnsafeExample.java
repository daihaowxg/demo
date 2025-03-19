package com.example.demo.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示 HashMap 线程不安全
 * - 多个线程同时调用 map.put() 可能导致数据覆盖或丢失。
 * - 在扩容时，多线程并发操作可能导致链表成环或数据丢失。
 * @author wxg
 * @since 2025/3/19
 */
public class HashMapThreadUnsafeExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个 HashMap
        Map<Integer, Integer> map = new HashMap<>();

        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 提交任务：向 HashMap 中添加 1000 个键值对
        for (int i = 0; i < 1000; i++) {
            final int key = i;
            executorService.submit(() -> {
                map.put(key, key); // 并发添加数据
            });
        }

        // 关闭线程池并等待所有任务完成
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(100);
        }

        // 输出 HashMap 的大小
        System.out.println("HashMap 的大小: " + map.size()); // HashMap 的大小: 998
    }
}
