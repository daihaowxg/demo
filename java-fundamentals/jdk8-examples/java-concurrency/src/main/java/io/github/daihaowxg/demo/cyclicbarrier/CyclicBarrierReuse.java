package io.github.daihaowxg.demo.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier 可以循环使用，以下示例展示多次同步
 * @author wxg
 * @since 2025/3/19
 */
public class CyclicBarrierReuse {
    private static final int THREAD_COUNT = 2;
    private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    System.out.println(Thread.currentThread().getName() + " 开始第 " + i + " 次任务");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println(Thread.currentThread().getName() + " 到达第 " + i + " 次屏障");
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " 完成第 " + i + " 次任务");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();
    }

    // Thread-A 开始第 1 次任务
    // Thread-B 开始第 1 次任务
    // Thread-B 到达第 1 次屏障
    // Thread-A 到达第 1 次屏障
    // Thread-A 完成第 1 次任务
    // Thread-B 完成第 1 次任务
    // Thread-B 开始第 2 次任务
    // Thread-A 开始第 2 次任务
    // Thread-A 到达第 2 次屏障
    // Thread-B 到达第 2 次屏障
    // Thread-B 完成第 2 次任务
    // Thread-A 完成第 2 次任务
    // Thread-B 开始第 3 次任务
    // Thread-A 开始第 3 次任务
    // Thread-A 到达第 3 次屏障
    // Thread-B 到达第 3 次屏障
    // Thread-B 完成第 3 次任务
    // Thread-A 完成第 3 次任务
}
