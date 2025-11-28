package io.github.daihaowxg.demo.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 使用 CountDownLatch 让多个线程在某个时刻同时开始执行
 * @author wxg
 * @since 2025/3/19
 */
public class StartTogetherExample {
    private static final int THREAD_COUNT = 3;
    private static final CountDownLatch startLatch = new CountDownLatch(1); // 用于统一开始

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备就绪");
                startLatch.await(); // 等待主线程信号
                System.out.println(Thread.currentThread().getName() + " 开始执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // 启动工作线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }

        Thread.sleep(1000); // 模拟主线程准备时间
        System.out.println("主线程发出开始信号");
        startLatch.countDown(); // 计数器减到 0，触发所有线程开始
    }

    // Thread-0 准备就绪
    // Thread-2 准备就绪
    // Thread-1 准备就绪
    // 主线程发出开始信号
    // Thread-1 开始执行
    // Thread-0 开始执行
    // Thread-2 开始执行
}
