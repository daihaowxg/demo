package com.example.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 演示可重入性
 */
public class ReentrantExample {
    private static final ReentrantLock lock = new ReentrantLock(); // 创建一个 ReentrantLock 实例

    public static void main(String[] args) {
        // 启动一个线程
        new Thread(() -> reentrantTask("线程 1")).start();
    }

    private static void reentrantTask(String threadName) {
        lock.lock(); // 第一次加锁
        try {
            System.out.println(threadName + " 第一次获取了锁");
            lock.lock(); // 第二次加锁
            try {
                System.out.println(threadName + " 第二次获取了锁");
            } finally {
                lock.unlock(); // 第一次解锁
                System.out.println(threadName + " 第一次释放了锁");
            }
        } finally {
            lock.unlock(); // 第二次解锁
            System.out.println(threadName + " 第二次释放了锁");
        }
    }
}
