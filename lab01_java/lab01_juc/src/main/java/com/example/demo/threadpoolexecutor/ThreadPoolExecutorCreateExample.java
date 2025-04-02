package com.example.demo.threadpoolexecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池创建示例
 */
public class ThreadPoolExecutorCreateExample {

    public static void main(String[] args) {

        // 核心线程数 = Java 虚拟机可用的 CPU 核心数
        int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
        System.out.println("CPU 核数：" + corePoolSize);
        int maximumPoolSize = corePoolSize * 2;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        for (int i = 0; i < 100; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();

    }
}
