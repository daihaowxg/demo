package io.github.daihaowxg.demo.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger 在多线程环境下的线程安全性
 * @author wxg
 * @since 2025/3/19
 */
public class AtomicIntegerThreadSafeExample {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // 创建 10 个线程
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet(); // 原子递增
                }
            });
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 输出最终结果
        System.out.println("最终结果: " + counter.get()); // 输出 10000
    }
}
