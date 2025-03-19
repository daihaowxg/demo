package com.example.demo.reentrantreadwritelock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 基本读写锁示例，展示如何使用读锁和写锁保护共享数据
 * @author wxg
 * @since 2025/3/19
 */
public class ReentrantReadWriteLockExample {
    private int data = 0;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    // 写操作
    public void write(int value) {
        writeLock.lock(); // 获取写锁
        try {
            data = value;
            System.out.println("Write: " + data);
        } finally {
            writeLock.unlock(); // 释放写锁
        }
    }

    // 读操作
    public int read() {
        readLock.lock(); // 获取读锁
        try {
            System.out.println("Read: " + data);
            return data;
        } finally {
            readLock.unlock(); // 释放读锁
        }
    }

    /**
     * ReentrantReadWriteLock 支持从写锁降级为读锁，通常用于在写操作后需要读取数据的场景
     * 但不能直接从读锁升级为写锁，否则会导致死锁。例如以下代码是错误的：
     *  readLock.lock();
     *  writeLock.lock(); // 错误！直接从读锁升级为写锁会导致死锁
     */
    public int writeAndRead(int value) {
        writeLock.lock(); // 获取写锁
        try {
            data = value;
            // 降级为读锁
            readLock.lock(); // 在释放写锁前获取读锁
            writeLock.unlock(); // 释放写锁
            try {
                System.out.println("After write, read: " + data);
                return data;
            } finally {
                readLock.unlock(); // 释放读锁
            }
        } finally {
            if (writeLock.isHeldByCurrentThread()) {
                writeLock.unlock(); // 确保写锁被释放
            }
        }
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockExample example = new ReentrantReadWriteLockExample();
        example.write(42);
        example.read();
    }
}
