package com.example.demo.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

public class FairLockExample {
    private static final ReentrantLock fairLock = new ReentrantLock(true); // 创建一个公平锁

    public static void main(String[] args) {
        // 启动 5 个线程
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            new Thread(() -> task("线程 " + threadId)).start();
            try {
                Thread.sleep(100); // 增加线程启动的延迟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void task(String threadName) {
        long startTime = System.currentTimeMillis(); // 记录任务开始时间
        fairLock.lock(); // 加锁
        try {
            long endTime = System.currentTimeMillis(); // 记录获取锁的时间
            System.out.println(threadName + " 获取了锁，等待时间: " + (endTime - startTime) + "ms");
            Thread.sleep(1000); // 增加任务执行时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            fairLock.unlock(); // 解锁
            System.out.println(threadName + " 释放了锁");
        }
    }
}