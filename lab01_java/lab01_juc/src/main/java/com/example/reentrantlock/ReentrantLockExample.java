package com.example.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    // 创建一个 ReentrantLock 实例
    private final ReentrantLock lock = new ReentrantLock();

    // 共享资源，多个线程会并发访问和修改它
    private int sharedResource = 0;

    // 操作共享资源的方法
    public void increment() {
        // 获取锁，确保同一时刻只有一个线程可以执行下面的代码
        lock.lock();
        try {
            // 操作共享资源
            sharedResource++;
            // 打印当前线程名称和共享资源的值（中文日志）
            System.out.println(Thread.currentThread().getName() + " 增加了 sharedResource 的值，当前值为: " + sharedResource);
        } finally {
            // 释放锁，确保锁一定会被释放，避免死锁
            // finally 块中的代码无论是否发生异常都会执行
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        // 创建 ReentrantLockExample 实例
        ReentrantLockExample example = new ReentrantLockExample();

        // 定义一个任务，每个线程会执行这个任务
        Runnable task = () -> {
            // 每个线程会执行 5 次 increment 操作
            for (int i = 0; i < 5; i++) {
                example.increment();
            }
        };

        // 创建两个线程，分别执行任务
        Thread thread1 = new Thread(task, "线程-1");
        Thread thread2 = new Thread(task, "线程-2");

        // 启动线程
        thread1.start();
        thread2.start();

        try {
            // 主线程等待 thread1 和 thread2 执行完毕
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            // 如果线程被中断，打印异常信息
            e.printStackTrace();
        }

        // 打印共享资源的最终值（中文日志）
        System.out.println("共享资源的最终值为: " + example.sharedResource);
    }
}