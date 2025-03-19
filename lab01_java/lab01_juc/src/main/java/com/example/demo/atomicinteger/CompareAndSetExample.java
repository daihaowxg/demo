package com.example.demo.atomicinteger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wxg
 * @since 2025/3/19
 */
public class CompareAndSetExample {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(10);

        // 如果当前值是 10，则更新为 20
        boolean success = atomicInteger.compareAndSet(10, 20);
        System.out.println("更新是否成功: " + success); // 输出 true
        System.out.println("当前值: " + atomicInteger.get()); // 输出 20

        // 如果当前值是 10，则更新为 30（不会成功，因为当前值是 20）
        success = atomicInteger.compareAndSet(10, 30);
        System.out.println("更新是否成功: " + success); // 输出 false
        System.out.println("当前值: " + atomicInteger.get()); // 输出 20
    }
}
