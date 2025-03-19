`StampedLock` 是 Java 8 引入的一种高性能读写锁，它是对 `ReentrantReadWriteLock` 的改进，提供了更灵活的锁机制和更高的并发性能。`StampedLock` 支持三种模式：写锁、悲观读锁和乐观读锁。

---

### `StampedLock` 的核心特性
1. **三种模式**：
    - **写锁（Write Lock）**：独占锁，类似于 `ReentrantReadWriteLock` 的写锁。
    - **悲观读锁（Read Lock）**：共享锁，类似于 `ReentrantReadWriteLock` 的读锁。
    - **乐观读锁（Optimistic Read Lock）**：无锁机制，适用于读多写少的场景，性能更高。

2. **高性能**：
    - 乐观读锁不阻塞写锁，适合读多写少的场景。
    - 通过 `stamp`（戳记）来管理锁的状态，避免了锁的重入问题。

3. **不可重入**：
    - `StampedLock` 的锁是不可重入的，同一个线程重复获取锁会导致死锁。

---

### `StampedLock` 的常用方法
1. **写锁**：
    - `long writeLock()`：获取写锁，返回一个 stamp。
    - `void unlockWrite(long stamp)`：释放写锁，需要传入对应的 stamp。

2. **悲观读锁**：
    - `long readLock()`：获取悲观读锁，返回一个 stamp。
    - `void unlockRead(long stamp)`：释放悲观读锁，需要传入对应的 stamp。

3. **乐观读锁**：
    - `long tryOptimisticRead()`：尝试获取乐观读锁，返回一个 stamp。
    - `boolean validate(long stamp)`：验证乐观读锁是否有效（即在获取 stamp 后没有写锁被获取）。

4. **锁转换**：
    - `long tryConvertToWriteLock(long stamp)`：尝试将读锁转换为写锁。
    - `long tryConvertToReadLock(long stamp)`：尝试将写锁转换为读锁。

5. **其他方法**：
    - `boolean isWriteLocked()`：判断是否持有写锁。
    - `boolean isReadLocked()`：判断是否持有读锁。

---

### `StampedLock` 的使用示例

#### 示例 1：写锁和悲观读锁
以下示例展示了如何使用写锁和悲观读锁。

```java
import java.util.concurrent.locks.StampedLock;

public class StampedLockExample {
    private final StampedLock stampedLock = new StampedLock();
    private int value = 0;

    // 写操作
    public void write(int newValue) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            value = newValue;
            System.out.println("写操作: " + value);
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    // 读操作
    public int read() {
        long stamp = stampedLock.readLock(); // 获取读锁
        try {
            System.out.println("读操作: " + value);
            return value;
        } finally {
            stampedLock.unlockRead(stamp); // 释放读锁
        }
    }

    public static void main(String[] args) {
        StampedLockExample example = new StampedLockExample();

        // 写线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.write(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 读线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.read();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
```

**输出结果：**
```
写操作: 0
读操作: 0
写操作: 1
读操作: 1
...
```

---

#### 示例 2：乐观读锁
以下示例展示了如何使用乐观读锁。

```java
import java.util.concurrent.locks.StampedLock;

public class OptimisticReadExample {
    private final StampedLock stampedLock = new StampedLock();
    private int value = 0;

    // 写操作
    public void write(int newValue) {
        long stamp = stampedLock.writeLock(); // 获取写锁
        try {
            value = newValue;
            System.out.println("写操作: " + value);
        } finally {
            stampedLock.unlockWrite(stamp); // 释放写锁
        }
    }

    // 乐观读操作
    public int optimisticRead() {
        long stamp = stampedLock.tryOptimisticRead(); // 尝试获取乐观读锁
        int currentValue = value; // 读取值
        if (!stampedLock.validate(stamp)) { // 验证 stamp 是否有效
            stamp = stampedLock.readLock(); // 获取悲观读锁
            try {
                currentValue = value;
            } finally {
                stampedLock.unlockRead(stamp); // 释放悲观读锁
            }
        }
        System.out.println("乐观读操作: " + currentValue);
        return currentValue;
    }

    public static void main(String[] args) {
        OptimisticReadExample example = new OptimisticReadExample();

        // 写线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.write(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // 读线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                example.optimisticRead();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
```

**输出结果：**
```
写操作: 0
乐观读操作: 0
写操作: 1
乐观读操作: 1
...
```

---

#### 示例 3：锁转换
以下示例展示了如何将读锁转换为写锁。

```java
import java.util.concurrent.locks.StampedLock;

public class LockConversionExample {
    private final StampedLock stampedLock = new StampedLock();
    private int value = 0;

    // 读操作并尝试转换为写操作
    public void readAndWrite(int newValue) {
        long stamp = stampedLock.readLock(); // 获取读锁
        try {
            while (value == 0) {
                long writeStamp = stampedLock.tryConvertToWriteLock(stamp); // 尝试转换为写锁
                if (writeStamp != 0) {
                    stamp = writeStamp;
                    value = newValue;
                    System.out.println("转换为写操作: " + value);
                    break;
                } else {
                    stampedLock.unlockRead(stamp); // 释放读锁
                    stamp = stampedLock.writeLock(); // 获取写锁
                }
            }
        } finally {
            stampedLock.unlock(stamp); // 释放锁
        }
    }

    public static void main(String[] args) {
        LockConversionExample example = new LockConversionExample();

        // 线程尝试读并转换为写
        new Thread(() -> example.readAndWrite(10)).start();
    }
}
```

---

### `StampedLock` 的应用场景
1. **读多写少的场景**：如缓存系统、统计系统。
2. **高性能并发控制**：如无锁数据结构的实现。
3. **锁升级和降级**：如从读锁升级为写锁。

---

### 总结
- `StampedLock` 是一种高性能的读写锁，支持写锁、悲观读锁和乐观读锁。
- 乐观读锁适合读多写少的场景，性能更高。
- 需要注意 `StampedLock` 的不可重入性，避免死锁。