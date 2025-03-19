package com.example.demo.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 条件变量，使用 Condition 实现线程间通信
 * @author wxg
 * @since 2025/3/19
 */
public class ReentrantLockCondition {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean ready = false;

    public void producer() {
        lock.lock();
        try {
            System.out.println("生产者准备数据...");
            Thread.sleep(1000);
            ready = true;
            condition.signal(); // 唤醒等待的线程
            System.out.println("生产者完成");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consumer() {
        lock.lock();
        try {
            while (!ready) {
                System.out.println("消费者等待...");
                condition.await(); // 等待信号
            }
            System.out.println("消费者消费数据");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockCondition example = new ReentrantLockCondition();
        new Thread(example::consumer, "Consumer").start();
        new Thread(example::producer, "Producer").start();
    }

    // 消费者等待...
    // 生产者准备数据...
    // (等待 1 秒)
    // 生产者完成
    // 消费者消费数据
}
