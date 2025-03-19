package com.example.demo.threadpoolexecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 动态调整线程数的线程池
 * @author wxg
 * @since 2025/3/19
 */
public class CachedThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
        // 线程数根据任务需求动态增加，空闲线程会被回收。
    }

    // 任务 0 执行，线程: pool-1-thread-1
    // 任务 1 执行，线程: pool-1-thread-2
    // 任务 2 执行，线程: pool-1-thread-3
    // 任务 3 执行，线程: pool-1-thread-4
    // 任务 4 执行，线程: pool-1-thread-5
}
