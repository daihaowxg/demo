package com.example.demo.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可被中断的锁
 */
public class InterruptibleLockExample {
    private static final ReentrantLock lock = new ReentrantLock(); // 创建一个 ReentrantLock 实例

    public static void main(String[] args) throws InterruptedException {
        // 启动两个线程
        Thread thread1 = new Thread(() -> task("线程 1"));
        Thread thread2 = new Thread(() -> task("线程 2"));

        thread1.start();
        Thread.sleep(100); // 确保线程 1 先获取锁
        thread2.start();

        Thread.sleep(1000); // 等待线程 2 进入阻塞状态
        thread2.interrupt(); // 中断线程 2
    }

    private static void task(String threadName) {
        try {
            lock.lockInterruptibly(); // 可中断的加锁
            try {
                System.out.println(threadName + " 获取了锁");
                Thread.sleep(2000); // 模拟业务逻辑
            } finally {
                lock.unlock(); // 解锁
                System.out.println(threadName + " 释放了锁");
            }
        } catch (InterruptedException e) {
            System.out.println(threadName + " 被中断");
        }
    }
}
