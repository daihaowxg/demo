`ReentrantReadWriteLock` 是 Java 并发包（`java.util.concurrent.locks`）中的一种读写锁实现，适用于读多写少的场景。它提供了读锁和写锁两种模式，允许多个线程同时持有读锁（共享锁），但写锁（独占锁）只能由一个线程持有。与 `ReentrantLock` 类似，`ReentrantReadWriteLock` 支持锁的可重入性，这意味着同一线程可以多次获取同一个锁而不会导致死锁。

下面我将详细介绍 `ReentrantReadWriteLock` 的用法，并提供示例代码。

---

### ReentrantReadWriteLock 的主要特点
1. **读写分离**：
    - **读锁（Read Lock）**：允许多个线程同时读取数据（共享锁）。
    - **写锁（Write Lock）**：只允许一个线程修改数据（独占锁），同时阻塞所有读写操作。
2. **可重入性**：同一线程可以多次获取读锁或写锁，前提是每次获取都需要对应的释放。
3. **公平性**：支持公平模式（`fair`）和非公平模式（默认），通过构造函数指定。
4. **锁降级**：支持从写锁降级为读锁，但不支持读锁升级为写锁。

---

### 基本方法
- **构造方法**：
    - `ReentrantReadWriteLock()`：创建默认非公平的读写锁。
    - `ReentrantReadWriteLock(boolean fair)`：指定是否为公平锁，`true` 表示公平锁。
- **获取读锁**：
    - `readLock().lock()`：阻塞式获取读锁。
    - `readLock().tryLock()`：非阻塞式尝试获取读锁。
    - `readLock().unlock()`：释放读锁。
- **获取写锁**：
    - `writeLock().lock()`：阻塞式获取写锁。
    - `writeLock().tryLock()`：非阻塞式尝试获取写锁。
    - `writeLock().unlock()`：释放写锁。

---

### 使用示例

#### 1. 基本读写锁示例
以下是一个简单的例子，展示如何使用读锁和写锁保护共享数据：

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

    public static void main(String[] args) {
        ReentrantReadWriteLockExample example = new ReentrantReadWriteLockExample();
        example.write(42);
        example.read();
    }
}
```

#### 2. 多线程并发读写
以下示例展示多个线程同时读取和写入数据：

```java
public class MultiThreadExample {
    private static final ReentrantReadWriteLockExample example = new ReentrantReadWriteLockExample();

    public static void main(String[] args) {
        // 创建多个读线程
        Runnable reader = () -> {
            for (int i = 0; i < 3; i++) {
                example.read();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // 创建一个写线程
        Runnable writer = () -> {
            for (int i = 0; i < 3; i++) {
                example.write(i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // 启动线程
        Thread t1 = new Thread(reader, "Reader-1");
        Thread t2 = new Thread(reader, "Reader-2");
        Thread t3 = new Thread(writer, "Writer");

        t1.start();
        t2.start();
        t3.start();
    }
}
```

运行结果会显示多个读线程可以同时读取数据，而写线程会独占访问权。

#### 3. 锁降级示例
`ReentrantReadWriteLock` 支持从写锁降级为读锁，通常用于在写操作后需要读取数据的场景：

```java
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
```

**注意**：不能直接从读锁升级为写锁，否则会导致死锁。例如以下代码是错误的：
```java
readLock.lock();
writeLock.lock(); // 错误！会导致死锁
```

---

### 注意事项
1. **锁释放顺序**：必须按照获取锁的相反顺序释放锁。例如，先获取写锁再获取读锁（降级），则需先释放读锁再释放写锁。
2. **异常处理**：始终在 `finally` 块中释放锁，避免锁泄漏。
3. **公平性选择**：
    - 默认非公平模式性能更高，但可能导致线程饥饿。
    - 公平模式（`new ReentrantReadWriteLock(true)`）保证先到先得，但吞吐量较低。
4. **不支持条件变量**：与 `ReentrantLock` 不同，`ReentrantReadWriteLock` 的锁不支持 `Condition` 对象。

---

### 与 StampedLock 的对比
- **性能**：`StampedLock` 的乐观读机制在读多写少时性能优于 `ReentrantReadWriteLock`。
- **重入性**：`ReentrantReadWriteLock` 支持锁重入，`StampedLock` 不支持。
- **锁升级**：`ReentrantReadWriteLock` 不支持读锁升级为写锁，而 `StampedLock` 支持锁转换。
- **使用复杂性**：`ReentrantReadWriteLock` 更简单易用，`StampedLock` 需要手动管理 `stamp`。

---

### 总结
`ReentrantReadWriteLock` 是一个功能强大且易用的读写锁实现，适合需要读写分离且支持锁重入的场景。它的读锁允许多线程并发读取，而写锁保证数据修改的独占性。如果你的应用需要简单的读写分离且可能涉及锁重入，`ReentrantReadWriteLock` 是一个很好的选择。如果追求更高的性能并能接受复杂性，可以考虑 `StampedLock`。

希望这对你理解和使用 `ReentrantReadWriteLock` 有帮助！如果有其他问题，欢迎继续提问。