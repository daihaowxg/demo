package io.github.daihaowxg.concurrency.basics.lifecycle;

/**
 * 演示 WAITING 状态
 * 
 * 场景：
 * 线程调用 Object.wait() 无限期等待，直到被 notify()
 */
public class StateWaitingDemo {

    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 状态演示: WAITING ===\n");

        Thread t = new Thread(() -> {
            synchronized (LOCK) {
                try {
                    System.out.println("   [线程内] 准备调用 wait()...");
                    LOCK.wait(); // 不带超时时间，进入 WAITING
                    System.out.println("   [线程内] 被 notify 唤醒了!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Waiting-Thread");

        t.start();
        Thread.sleep(100); // 确保线程启动并进入 wait

        System.out.println("1. 调用 wait() 后状态: " + t.getState());

        // Java 语言规范规定：当前线程必须拥有该对象（这里是 LOCK 对象）的监视器锁，才能调用该对象的 wait/notify 方法。
        // synchronized (LOCK) 的作用就是让主线程获取到 LOCK 的锁。
        // 只有拿到了锁，才能有资格去唤醒（notify）在同一个锁上等待（wait）的其他线程。
        synchronized (LOCK) {
            System.out.println("   [Main] 准备 notify()...");
            LOCK.notify();
        }

        Thread.sleep(100); // 等待状态转换（可能先经过 BLOCKED 再到 RUNNABLE）
        // 注意：被唤醒后，它需要重新获取锁。如果在获取锁的过程中被其他线程占用了，可能短暂变回 BLOCKED。
        // 但一旦拿到锁继续执行代码，就是 RUNNABLE
    }
}
