package com.example.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

public class HoldCountExample {
    private static final ReentrantLock lock = new ReentrantLock(); // 创建一个 ReentrantLock 实例

    public static void main(String[] args) {
        // 启动一个线程
        new Thread(() -> task("线程 1")).start();
    }

    private static void task(String threadName) {
        lock.lock(); // 第一次加锁
        try {
            System.out.println(threadName + " 当前持有锁的次数: " + lock.getHoldCount());
            lock.lock(); // 第二次加锁
            System.out.println(threadName + " 当前持有锁的次数: " + lock.getHoldCount());
        } finally {
            lock.unlock(); // 第一次解锁
            System.out.println(threadName + " 当前持有锁的次数: " + lock.getHoldCount());
            lock.unlock(); // 第二次解锁
            System.out.println(threadName + " 当前持有锁的次数: " + lock.getHoldCount());
        }
    }
}
