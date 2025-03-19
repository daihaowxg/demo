package com.example.demo.threadpoolexecutor;

import java.util.concurrent.*;

/**
 * 自定义线程池参数
 * @author wxg
 * @since 2025/3/19
 */
public class CustomThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, // 核心线程数
                4, // 最大线程数
                60L, // 空闲线程存活时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2), // 任务队列容量
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );

        // 提交 6 个任务
        for (int i = 0; i < 8; i++) {
            int taskId = i;
            try {
                executor.execute(() -> {
                    System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RejectedExecutionException e) {
                System.out.println("任务 " + taskId + " 被拒绝");
            }
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }

    // 任务 0 执行，线程: pool-1-thread-1
    // 任务 6 被拒绝
    // 任务 4 执行，线程: pool-1-thread-3
    // 任务 1 执行，线程: pool-1-thread-2
    // 任务 7 被拒绝
    // 任务 5 执行，线程: pool-1-thread-4
    // 任务 2 执行，线程: pool-1-thread-3
    // 任务 3 执行，线程: pool-1-thread-1
}
