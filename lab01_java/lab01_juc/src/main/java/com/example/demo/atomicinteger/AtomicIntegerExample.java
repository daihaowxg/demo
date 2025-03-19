package com.example.demo.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger 的基本用法
 * @author wxg
 * @since 2025/3/19
 */
public class AtomicIntegerExample {
    public static void main(String[] args) {
        // 创建一个 AtomicInteger，初始值为 0
        AtomicInteger atomicInteger = new AtomicInteger(0);

        // 获取当前值
        System.out.println("当前值: " + atomicInteger.get()); // 输出 0

        // 设置新值
        atomicInteger.set(10);
        System.out.println("设置后的值: " + atomicInteger.get()); // 输出 10

        // 获取当前值并递增
        int oldValue = atomicInteger.getAndIncrement();
        System.out.println("递增前的值: " + oldValue); // 输出 10
        System.out.println("递增后的值: " + atomicInteger.get()); // 输出 11

        // 比较并交换
        boolean success = atomicInteger.compareAndSet(11, 20);
        System.out.println("比较并交换是否成功: " + success); // 输出 true
        System.out.println("当前值: " + atomicInteger.get()); // 输出 20
    }
}