package io.github.daihaowxg.demo.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入性，展示同一个线程多次获取锁
 * @author wxg
 * @since 2025/3/19
 */
public class ReentrantLockReentrant {
    private final ReentrantLock lock = new ReentrantLock();

    public void outerMethod() {
        lock.lock();
        try {
            System.out.println("外层方法持有锁，次数: " + lock.getHoldCount());
            innerMethod();
        } finally {
            lock.unlock();
        }
    }

    public void innerMethod() {
        lock.lock();
        try {
            System.out.println("内层方法持有锁，次数: " + lock.getHoldCount());
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockReentrant example = new ReentrantLockReentrant();
        example.outerMethod();

        // 外层方法持有锁，次数: 1
        // 内层方法持有锁，次数: 2
    }
}
