package io.github.daihaowxg.demo.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * 如何使用写锁和悲观读锁
 * @author wxg
 * @since 2025/3/19
 */
public class StampedLockExample {
    private final StampedLock stampedLock = new StampedLock();
    private int value = 0;

    // 写操作
    public void write(int newValue) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            value = newValue;
            System.out.println("写操作: " + value);
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    // 读操作
    public int read() {
        long stamp = stampedLock.readLock(); // 获取读锁
        try {
            System.out.println("读操作: " + value);
            return value;
        } finally {
            stampedLock.unlockRead(stamp); // 释放读锁
        }
    }

    public static void main(String[] args) {
        StampedLockExample example = new StampedLockExample();

        // 写线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.write(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 读线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.read();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
