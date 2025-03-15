package com.example.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件变量（Condition）
 */
public class ConditionExample {
    private static final ReentrantLock lock = new ReentrantLock(); // 创建一个 ReentrantLock 实例
    private static final Condition condition = lock.newCondition(); // 创建一个 Condition 实例

    public static void main(String[] args) throws InterruptedException {
        // 启动一个等待线程
        new Thread(() -> waitTask("线程 1")).start();
        Thread.sleep(1000); // 确保线程 1 先进入等待状态
        // 启动一个通知线程
        new Thread(ConditionExample::signalTask).start();
    }

    private static void waitTask(String threadName) {
        lock.lock(); // 加锁
        try {
            System.out.println(threadName + " 正在等待");
            condition.await(); // 进入等待状态
            System.out.println(threadName + " 被唤醒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); // 解锁
        }
    }

    private static void signalTask() {
        lock.lock(); // 加锁
        try {
            System.out.println("通知线程正在运行");
            condition.signal(); // 唤醒等待的线程
        } finally {
            lock.unlock(); // 解锁
        }
    }
}
