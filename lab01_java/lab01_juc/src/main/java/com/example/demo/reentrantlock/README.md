`ReentrantLock` 是 Java 并发包（`java.util.concurrent.locks`）中的一种可重入锁实现，引入于 Java 5。它是 `synchronized` 关键字的替代品，提供了更灵活的锁机制。与 `synchronized` 相比，`ReentrantLock` 支持非阻塞锁获取、超时等待、公平性选择以及与条件变量（`Condition`）配合使用等高级功能。

下面我将详细介绍 `ReentrantLock` 的用法，并提供示例代码。

---

### ReentrantLock 的主要特点
1. **可重入性**：同一个线程可以多次获取同一把锁，每次获取需要对应次数的释放。
2. **显式锁**：需要手动调用 `lock()` 和 `unlock()`，相比 `synchronized` 更灵活但也更复杂。
3. **公平性**：支持公平锁（先到先得）和非公平锁（默认，性能更高）。
4. **条件变量**：通过 `Condition` 对象实现线程间的精确唤醒和等待。
5. **中断支持**：支持在等待锁时响应线程中断。

---

### 基本方法
- **构造方法**：
    - `ReentrantLock()`：创建默认非公平锁。
    - `ReentrantLock(boolean fair)`：指定是否为公平锁，`true` 表示公平锁。
- **锁操作**：
    - `lock()`：获取锁，如果锁不可用则阻塞。
    - `tryLock()`：尝试获取锁，立即返回是否成功。
    - `tryLock(long time, TimeUnit unit)`：带超时的尝试获取锁。
    - `lockInterruptibly()`：获取锁，但可以响应中断。
    - `unlock()`：释放锁。
- **状态检查**：
    - `isLocked()`：检查锁是否被持有。
    - `getHoldCount()`：返回当前线程持有锁的次数。
- **条件变量**：
    - `newCondition()`：创建与该锁关联的 `Condition` 对象。

---

### 使用示例

#### 1. 基本锁用法
使用 `ReentrantLock` 保护共享资源：

```java
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockBasic {
    private int counter = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock(); // 获取锁
        try {
            counter++;
            System.out.println(Thread.currentThread().getName() + " 增加计数器: " + counter);
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    public static void main(String[] args) {
        ReentrantLockBasic example = new ReentrantLockBasic();

        Runnable task = () -> {
            for (int i = 0; i < 3; i++) {
                example.increment();
            }
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        t1.start();
        t2.start();
    }
}
```

**运行结果示例**：
```
Thread-1 增加计数器: 1
Thread-1 增加计数器: 2
Thread-1 增加计数器: 3
Thread-2 增加计数器: 4
Thread-2 增加计数器: 5
Thread-2 增加计数器: 6
```

使用 `lock()` 和 `unlock()` 确保计数器的线程安全。

---

#### 2. 可重入性
展示同一个线程多次获取锁：

```java
import java.util.concurrent.locks.ReentrantLock;

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
    }
}
```

**运行结果**：
```
外层方法持有锁，次数: 1
内层方法持有锁，次数: 2
```

同一个线程可以重入锁，`getHoldCount()` 返回持有次数。

---

#### 3. 非阻塞锁获取
使用 `tryLock()` 尝试获取锁：

```java
import java.util.concurrent.locks.ReentrantLock;

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
}
```

**运行结果示例**：
```
Thread-1 获取锁成功
Thread-2 获取锁失败
(等待 1 秒后 Thread-1 释放锁)
```

`tryLock()` 不会阻塞，获取失败立即返回 `false`。

---

#### 4. 条件变量
使用 `Condition` 实现线程间通信：

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

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
}
```

**运行结果示例**：
```
消费者等待...
生产者准备数据...
(等待 1 秒)
生产者完成
消费者消费数据
```

`Condition` 的 `await()` 和 `signal()` 类似于 `Object` 的 `wait()` 和 `notify()`。

---

### 注意事项
1. **手动释放锁**：必须在 `finally` 块中调用 `unlock()`，否则可能导致死锁。
2. **公平性选择**：
    - 默认非公平锁（`new ReentrantLock()`）性能更高。
    - 公平锁（`new ReentrantLock(true)`）保证先到先得，但吞吐量较低。
3. **异常处理**：确保锁操作在 `try-finally` 中，避免锁泄漏。
4. **与 synchronized 的对比**：
    - `synchronized`：隐式锁，简单但功能有限。
    - `ReentrantLock`：显式锁，支持高级特性（如条件变量、超时）。

---

### 总结
`ReentrantLock` 是一个功能强大的锁实现，适用于需要灵活控制锁行为的场景。它支持可重入性、非阻塞获取、条件变量和公平性选择，比 `synchronized` 更适合复杂并发需求。如果你的程序需要简单的同步，`synchronized` 可能足够；但如果需要高级功能（如超时、中断、精确唤醒），`ReentrantLock` 是更好的选择。

希望这对你理解和使用 `ReentrantLock` 有帮助！如果有其他问题，欢迎继续提问。