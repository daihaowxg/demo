package io.github.daihaowxg.demo.reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 非阻塞锁获取，使用 tryLock() 尝试获取锁
 * @author wxg
 * @since 2025/3/19
 */
public class ReentrantLockTryLock {
    private final ReentrantLock lock = new ReentrantLock();

    public void doWork() {
        if (lock.tryLock()) {
            try {
                System.out.println(Thread.currentThread().getName() + " 获取锁成功");
                Thread.sleep(1000); // 模拟工作
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + " 获取锁失败");
        }
    }

    public static void main(String[] args) {
        ReentrantLockTryLock example = new ReentrantLockTryLock();
        new Thread(example::doWork, "Thread-1").start();
        new Thread(example::doWork, "Thread-2").start();
    }

    // Thread-1 获取锁成功
    // Thread-2 获取锁失败
    // (等待 1 秒后 Thread-1 释放锁)
}
