package com.example.demo.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 带超时的等待
 * @author wxg
 * @since 2025/3/19
 */
public class TimeoutExample {
    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟长时间任务
                System.out.println("任务完成");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        worker.start();

        System.out.println("主线程等待，最多 1 秒...");
        boolean completed = latch.await(1, TimeUnit.SECONDS); // 等待 1 秒
        if (completed) {
            System.out.println("任务按时完成");
        } else {
            System.out.println("超时，任务未完成");
        }
    }

    /*
     * 如果任务未在指定时间内完成，await() 返回 false，主线程可以提前继续执行。
     */
    // 主线程等待，最多 1 秒...
    // 超时，任务未完成
    // 任务完成
}
