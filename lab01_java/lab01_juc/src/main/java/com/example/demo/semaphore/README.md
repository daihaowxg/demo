`Semaphore` 是 Java 并发包 (`java.util.concurrent`) 中的一个同步工具，用于控制对共享资源的并发访问。它通过维护一组许可证（permits）来实现资源的限制访问。`Semaphore` 的主要作用是控制同时访问某个资源的线程数量。

### Semaphore 的核心概念
- **许可证（Permits）**：`Semaphore` 内部维护一定数量的许可证。线程可以通过 `acquire()` 获取许可证，通过 `release()` 释放许可证。
- **限制并发数**：通过初始化时指定许可证的数量，可以限制同时访问某个资源的线程数。
- **公平性**：`Semaphore` 支持公平模式和非公平模式。在公平模式下，等待时间最长的线程会优先获取许可证。

---

### Semaphore 的常用方法
1. **构造函数**
    - `Semaphore(int permits)`：创建一个非公平的 `Semaphore`，指定许可证数量。
    - `Semaphore(int permits, boolean fair)`：创建一个 `Semaphore`，指定许可证数量和是否公平。

2. **核心方法**
    - `void acquire()`：获取一个许可证，如果没有可用的许可证，则线程阻塞，直到有许可证可用。
    - `void release()`：释放一个许可证，将其返还给 `Semaphore`。
    - `boolean tryAcquire()`：尝试获取一个许可证，如果成功返回 `true`，否则返回 `false`（非阻塞）。
    - `boolean tryAcquire(long timeout, TimeUnit unit)`：在指定时间内尝试获取许可证，超时后返回 `false`。
    - `int availablePermits()`：返回当前可用的许可证数量。

---

### Semaphore 的使用示例

#### 示例 1：限制资源并发访问
以下示例展示了如何使用 `Semaphore` 限制同时访问某个资源的线程数量。

```java
import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    public static void main(String[] args) {
        // 创建一个 Semaphore，允许最多 3 个线程同时访问
        Semaphore semaphore = new Semaphore(3);

        // 创建 10 个线程，模拟资源访问
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    // 获取许可证
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " 获取许可证，正在访问资源...");

                    // 模拟资源访问
                    Thread.sleep(2000);

                    System.out.println(Thread.currentThread().getName() + " 释放许可证，资源访问结束。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放许可证
                    semaphore.release();
                }
            }, "Thread-" + i).start();
        }
    }
}
```

**输出结果：**
```
Thread-1 获取许可证，正在访问资源...
Thread-2 获取许可证，正在访问资源...
Thread-3 获取许可证，正在访问资源...
Thread-1 释放许可证，资源访问结束。
Thread-4 获取许可证，正在访问资源...
Thread-2 释放许可证，资源访问结束。
Thread-5 获取许可证，正在访问资源...
...
```

**说明：**
- 最多只有 3 个线程可以同时访问资源，其他线程需要等待许可证释放。

---

#### 示例 2：使用 `tryAcquire()` 非阻塞获取许可证
以下示例展示了如何使用 `tryAcquire()` 尝试获取许可证，如果失败则执行其他逻辑。

```java
import java.util.concurrent.Semaphore;

public class TryAcquireExample {
    public static void main(String[] args) {
        // 创建一个 Semaphore，允许最多 2 个线程同时访问
        Semaphore semaphore = new Semaphore(2);

        // 创建 5 个线程
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                if (semaphore.tryAcquire()) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 获取许可证，正在访问资源...");
                        Thread.sleep(2000); // 模拟资源访问
                        System.out.println(Thread.currentThread().getName() + " 释放许可证，资源访问结束。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " 未能获取许可证，执行其他逻辑...");
                }
            }, "Thread-" + i).start();
        }
    }
}
```

**输出结果：**
```
Thread-1 获取许可证，正在访问资源...
Thread-2 获取许可证，正在访问资源...
Thread-3 未能获取许可证，执行其他逻辑...
Thread-4 未能获取许可证，执行其他逻辑...
Thread-5 未能获取许可证，执行其他逻辑...
Thread-1 释放许可证，资源访问结束。
Thread-2 释放许可证，资源访问结束.
```

**说明：**
- 只有前两个线程成功获取许可证，其他线程执行了备用逻辑。

---

### Semaphore 的应用场景
1. **资源池管理**：如数据库连接池、线程池等。
2. **限流**：限制同时访问某个服务的请求数量。
3. **生产者-消费者模型**：控制生产者和消费者的并发数量。

---

### 总结
- `Semaphore` 是一种灵活的同步工具，用于控制对共享资源的并发访问。
- 通过 `acquire()` 和 `release()` 方法，可以实现对资源的限制访问。
- 支持公平模式和非公平模式，适用于多种并发场景。