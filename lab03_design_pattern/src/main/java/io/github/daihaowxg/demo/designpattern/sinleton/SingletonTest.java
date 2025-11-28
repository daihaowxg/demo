package io.github.daihaowxg.demo.designpattern.sinleton;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class SingletonTest {
    private static final int THREAD_COUNT = 100;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Testing Singleton Implementations...\n");

        testSingleton("Eager Singleton", () -> EagerSingleton.getInstance());
        testSingleton("Lazy Singleton (Thread Unsafe)", () -> LazySingletonUnsafe.getInstance());
        testSingleton("Lazy Singleton (Synchronized)", () -> LazySingletonSynchronized.getInstance());
        testSingleton("Double-Checked Locking", () -> DoubleCheckedLockingSingleton.getInstance());
        testSingleton("Static Inner Class", () -> StaticInnerClassSingleton.getInstance());
        testSingleton("Enum Singleton", () -> EnumSingleton.INSTANCE);
    }

    private static void testSingleton(String singletonName, SingletonSupplier supplier) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        Set<Integer> hashCodes = ConcurrentHashMap.newKeySet(); // 线程安全的 HashSet

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(() -> {
                Object instance = supplier.get();
                hashCodes.add(System.identityHashCode(instance)); // 记录实例的 hashCode
                latch.countDown();
            }).start();
        }

        latch.await(); // 等待所有线程完成

        System.out.println(singletonName + " -> Unique Instances: " + hashCodes.size());
        System.out.println("Instance HashCodes: " + hashCodes + "\n");
    }

    @FunctionalInterface
    interface SingletonSupplier {
        Object get();
    }
}
