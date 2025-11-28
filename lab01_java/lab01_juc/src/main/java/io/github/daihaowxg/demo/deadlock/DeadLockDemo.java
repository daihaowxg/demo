package io.github.daihaowxg.demo.deadlock;

/**
 * 死锁示例
 * 1. 互斥条件：互斥资源，即同一时间只能被一个线程使用
 * 2. 占有并等待：一个线程已经获得资源，在等待其它资源
 * 3. 不可抢占：线程不能抢占其它线程已经使用的资源
 * 4. 循环等待：线程1占有资源1，等待资源2，线程2占有资源2，等待资源1，线程1、线程2互为等待，造成死锁
 * @author wxg
 * @since 2025/4/2
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();

        // 线程1：先锁A，再锁B
        Runnable task1 = () -> {
            synchronized (A) {
                try { Thread.sleep(1000); }
                catch (InterruptedException e) {}
                synchronized (B) {
                    System.out.println("A：do something...");
                }
            }
        };

        // 线程2：先锁B，再锁A
        Runnable task2 = () -> {
            synchronized (B) {
                try { Thread.sleep(1000); }
                catch (InterruptedException e) {}
                synchronized (A) {
                    System.out.println("B：do something...");
                }
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }
}
