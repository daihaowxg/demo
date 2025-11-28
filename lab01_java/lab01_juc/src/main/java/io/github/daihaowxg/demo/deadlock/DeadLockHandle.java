package io.github.daihaowxg.demo.deadlock;

/**
 * 解决死锁：一次性获取所有需要的锁，并按照固定顺序获取
 *
 * @author wxg
 * @since 2025/4/2
 */
public class DeadLockHandle {
    public static void main(String[] args) {
        Object A = new Object();
        Object B = new Object();
        Runnable task1 = () -> {
            synchronized (A) {
                synchronized (B) {  // 按照 A、B 的顺序获取锁
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("A：do something...");
                }
            }
        };

        Runnable task2 = () -> {
            synchronized (A) {  // 和 task1 相同的顺序
                synchronized (B) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                    System.out.println("B：do something...");
                }
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();
    }
}
