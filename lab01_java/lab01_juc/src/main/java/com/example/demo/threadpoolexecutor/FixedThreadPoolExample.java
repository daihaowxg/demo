package com.example.demo.threadpoolexecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 固定大小的线程池
 * @author wxg
 * @since 2025/3/19
 */
public class FixedThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建固定 3 个线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交任务
        for (int i = 0; i < 5; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // 模拟耗时任务
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("所有任务完成");

        // 线程池复用了 3 个线程处理 5 个任务。
    }

    // 任务 0 执行，线程: pool-1-thread-1
    // 任务 1 执行，线程: pool-1-thread-2
    // 任务 2 执行，线程: pool-1-thread-3
    // (等待 1 秒)
    // 任务 3 执行，线程: pool-1-thread-1
    // 任务 4 执行，线程: pool-1-thread-2
    // (等待 1 秒)
    // 所有任务完成
}
