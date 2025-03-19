package com.example.demo.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * 如何使用乐观读锁
 * @author wxg
 * @since 2025/3/19
 */
public class OptimisticReadExample {
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

    // 乐观读操作
    public int optimisticRead() {
        long stamp = stampedLock.tryOptimisticRead(); // 尝试获取乐观读锁
        int currentValue = value; // 读取值
        if (!stampedLock.validate(stamp)) { // 验证 stamp 是否有效
            stamp = stampedLock.readLock(); // 获取悲观读锁
            try {
                currentValue = value;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        System.out.println("乐观读操作: " + currentValue);
        return currentValue;
    }

    public static void main(String[] args) {
        OptimisticReadExample example = new OptimisticReadExample();

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
                example.optimisticRead();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

