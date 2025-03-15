package com.example.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 的基本用法：加锁与解锁
 */
public class BasicLockExample {
    private static final ReentrantLock lock = new ReentrantLock(); // 创建一个 ReentrantLock 实例

    public static void main(String[] args) {
        // 启动两个线程
        new Thread(() -> task("线程 1")).start();
        new Thread(() -> task("线程 2")).start();
    }

    private static void task(String threadName) {
        lock.lock(); // 加锁
        try {
            System.out.println(threadName + " 获取了锁");
            Thread.sleep(3000); // 模拟业务逻辑
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 解锁
            System.out.println(threadName + " 释放了锁");
        }
    }
}