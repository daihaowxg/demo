package com.example.demo.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 基本锁用法，使用 ReentrantLock 保护共享资源
 * @author wxg
 * @since 2025/3/19
 */
public class ReentrantLockBasic {
    private int counter = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // 获取锁
        try {
            counter++;
            System.out.println(Thread.currentThread().getName() + " 增加计数器: " + counter);
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    public static void main(String[] args) {
        ReentrantLockBasic example = new ReentrantLockBasic();

        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                example.increment();
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        t1.start();
        t2.start();
    }

    // Thread-2 增加计数器: 1
    // Thread-2 增加计数器: 2
    // Thread-2 增加计数器: 3
    // Thread-1 增加计数器: 4
    // Thread-1 增加计数器: 5
    // Thread-1 增加计数器: 6
}
