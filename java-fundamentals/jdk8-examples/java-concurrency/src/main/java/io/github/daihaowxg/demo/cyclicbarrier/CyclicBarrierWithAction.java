package io.github.daihaowxg.demo.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * 在所有线程到达屏障时执行一个额外的动作。
 * 屏障动作会在所有线程到达后、继续执行前由最后一个到达的线程执行
 * @author wxg
 * @since 2025/3/19
 */
public class CyclicBarrierWithAction {
    private static final int THREAD_COUNT = 3;
    private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () -> {
        System.out.println("所有线程已到达屏障，执行屏障动作！");
    });

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始工作...");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(Thread.currentThread().getName() + " 到达屏障");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " 继续工作");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }

    // Thread-0 开始工作...
    // Thread-2 开始工作...
    // Thread-1 开始工作...
    // Thread-0 到达屏障
    // Thread-2 到达屏障
    // Thread-1 到达屏障
    // 所有线程已到达屏障，执行屏障动作！
    // Thread-1 继续工作
    // Thread-2 继续工作
    // Thread-0 继续工作
}
