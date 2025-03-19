package com.example.demo.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 主线程等待多个子线程完成任务
 * @author wxg
 * @since 2025/3/19
 */
public class CountDownLatchExample {
    private static final int THREAD_COUNT = 3;
    private static final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始执行任务...");
                Thread.sleep((long) (Math.random() * 1000)); // 模拟任务耗时
                System.out.println(Thread.currentThread().getName() + " 任务完成");
                latch.countDown(); // 计数器减 1
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // 启动子线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }

        System.out.println("主线程等待所有子线程完成...");
        latch.await(); // 主线程等待计数器归零
        System.out.println("所有子线程已完成，主线程继续执行");
    }

    // 主线程等待所有子线程完成...
    // Thread-1 开始执行任务...
    // Thread-0 开始执行任务...
    // Thread-2 开始执行任务...
    // Thread-2 任务完成
    // Thread-0 任务完成
    // Thread-1 任务完成
    // 所有子线程已完成，主线程继续执行
}
