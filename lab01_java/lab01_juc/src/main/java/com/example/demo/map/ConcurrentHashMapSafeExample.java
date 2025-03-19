package com.example.demo.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示 ConcurrentHashMap 线程安全
 * @author wxg
 * @since 2025/3/19
 */
public class ConcurrentHashMapSafeExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建一个 HashMap
        Map<Integer, Integer> map = new ConcurrentHashMap<>();

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
        System.out.println("ConcurrentHashMap 的大小: " + map.size()); // ConcurrentHashMap 的大小: 1000
    }
}
