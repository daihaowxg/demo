package io.github.daihaowxg.concurrency.basics.lifecycle;

/**
 * 演示 BLOCKED 状态
 * 
 * 场景：
 * 线程 A 持有锁，线程 B 尝试获取同一个锁，B 将进入 BLOCKED 状态
 */
public class StateBlockedDemo {

    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 状态演示: BLOCKED ===\n");

        // 线程 1: 占有锁不释放
        Thread t1 = new Thread(() -> {
            synchronized (LOCK) {
                System.out.println("   [T1] 拿到锁了，持有 2秒...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("   [T1] 释放锁");
            }
        }, "T1-LockHolder");

        // 线程 2: 尝试获取同一个锁
        Thread t2 = new Thread(() -> {
            System.out.println("   [T2] 尝试获取锁...");
            synchronized (LOCK) {
                System.out.println("   [T2] 终于拿到锁了!");
            }
        }, "T2-BlockedUser");

        t1.start();
        Thread.sleep(100); // 确保 T1 先拿到锁

        t2.start();
        Thread.sleep(100); // 确保 T2 已经运行到 synchronized 块并被阻塞

        System.out.println("T2 等待锁时的状态: " + t2.getState());
    }
}
