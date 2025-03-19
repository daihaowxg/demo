`AtomicInteger` 是 `java.util.concurrent.atomic` 包中的一个类，用于在多线程环境下对 `int` 类型变量进行原子操作。

### `AtomicInteger` 的作用
`AtomicInteger` 提供了一种线程安全的方式操作 `int` 值，避免了使用 `synchronized` 或显式锁的开销。它通过底层硬件的 CAS（Compare-And-Swap）操作实现原子性。

---

### `AtomicInteger` 的核心特性
1. **原子性**：所有操作都是原子的，线程安全。
2. **高性能**：基于 CAS 实现，避免了锁的开销。
3. **常用方法**：提供了丰富的原子操作方法，如递增、递减、加法、比较并交换等。

---

### `AtomicInteger` 的常用方法
1. **构造函数**
    - `AtomicInteger()`：初始值为 0。
    - `AtomicInteger(int initialValue)`：指定初始值。

2. **常用方法**
    - `int get()`：获取当前值。
    - `void set(int newValue)`：设置新值。
    - `int getAndSet(int newValue)`：获取当前值并设置新值。
    - `int getAndIncrement()`：获取当前值并递增 1。
    - `int getAndDecrement()`：获取当前值并递减 1。
    - `int getAndAdd(int delta)`：获取当前值并加上指定值。
    - `int incrementAndGet()`：递增 1 并返回新值。
    - `int decrementAndGet()`：递减 1 并返回新值。
    - `int addAndGet(int delta)`：加上指定值并返回新值。
    - `boolean compareAndSet(int expect, int update)`：如果当前值等于 `expect`，则设置为 `update`，返回是否成功。

---

### `AtomicInteger` 的使用示例

#### 示例 1：基本用法
以下示例展示了 `AtomicInteger` 的基本用法。

```java
import java.util.concurrent.atomic.AtomicInteger;

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
```

---

#### 示例 2：多线程环境下的原子操作
以下示例展示了 `AtomicInteger` 在多线程环境下的线程安全性。

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerThreadSafeExample {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        // 创建 10 个线程
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    counter.incrementAndGet(); // 原子递增
                }
            });
            threads[i].start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 输出最终结果
        System.out.println("最终结果: " + counter.get()); // 输出 10000
    }
}
```

**说明：**
- 10 个线程并发执行，每个线程对 `counter` 递增 1000 次。
- 由于 `AtomicInteger` 的原子性，最终结果总是 10000。

---

#### 示例 3：`compareAndSet` 的使用
以下示例展示了如何使用 `compareAndSet` 实现条件更新。

```java
import java.util.concurrent.atomic.AtomicInteger;

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
```

---

### `AtomicInteger` 的应用场景
1. **计数器**：如统计访问量、任务完成数等。
2. **标志位**：如控制某个状态是否已更新。
3. **非阻塞算法**：如实现无锁队列、栈等数据结构。

---

### 总结
- `AtomicInteger` 是 Java 中用于原子操作 `int` 值的工具类。
- 它通过 CAS 实现线程安全，性能优于锁。
- 适用于多线程环境下的计数器、标志位等场景。