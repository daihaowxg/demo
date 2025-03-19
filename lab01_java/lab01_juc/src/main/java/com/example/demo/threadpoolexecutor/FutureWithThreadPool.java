package com.example.demo.threadpoolexecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 提交任务并通过 Future 获取结果
 * @author wxg
 * @since 2025/3/19
 */
public class FutureWithThreadPool {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });

        System.out.println("等待结果...");
        Integer result = future.get();
        System.out.println("结果: " + result);

        executor.shutdown();
    }

    // 等待结果...
    // (等待 1 秒)
    // 结果: 42
}
