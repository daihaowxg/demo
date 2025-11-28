package io.github.daihaowxg.demo.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * 多个线程在某个点上等待，然后一起继续执行
 * @author wxg
 * @since 2025/3/19
 */
public class CyclicBarrierExample {
    private static final int THREAD_COUNT = 3;
    private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始准备...");
                Thread.sleep((long) (Math.random() * 1000)); // 模拟准备时间
                System.out.println(Thread.currentThread().getName() + " 准备完成，到达屏障");
                barrier.await(); // 等待所有线程到达
                System.out.println(Thread.currentThread().getName() + " 通过屏障，继续执行");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // 创建并启动线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }

        /*
         * 所有线程在到达 barrier.await() 时等待，直到 3 个线程都到达后一起继续执行。
         */
        // Thread-0 开始准备...
        // Thread-2 开始准备...
        // Thread-1 开始准备...
        // Thread-2 准备完成，到达屏障
        // Thread-0 准备完成，到达屏障
        // Thread-1 准备完成，到达屏障
        // Thread-1 通过屏障，继续执行
        // Thread-2 通过屏障，继续执行
        // Thread-0 通过屏障，继续执行
    }
}
