`CountDownLatch` 是 Java 并发包（`java.util.concurrent`）中的一种同步工具，中文常译为“倒计数锁存器”。它允许一个或多个线程等待一组操作（事件）完成后再继续执行。核心思想是通过一个计数器来控制线程的等待，当计数器减到 0 时，所有等待的线程会被唤醒并继续运行。

下面我将详细介绍 `CountDownLatch` 的用法，并提供示例代码。

---

### CountDownLatch 的主要特点
1. **倒计数机制**：初始化时指定一个计数器值，每次完成一个操作就减少计数，直到计数器为 0。
2. **一次性使用**：与 `CyclicBarrier` 不同，`CountDownLatch` 是一次性的，计数器减到 0 后无法重置。
3. **等待与触发**：
    - 一个或多个线程可以通过 `await()` 方法等待计数器归零。
    - 其他线程通过 `countDown()` 方法减少计数器。
4. **简单高效**：适用于主线程等待子任务完成，或协调多个线程的启动。

---

### 基本方法
- **构造方法**：
    - `CountDownLatch(int count)`：指定初始计数器值，必须大于等于 0。
- **核心方法**：
    - `await()`：当前线程等待，直到计数器减到 0。
    - `await(long timeout, TimeUnit unit)`：带超时的等待，如果超时计数器仍未归零，返回 `false`。
    - `countDown()`：将计数器减 1，如果减到 0，则唤醒所有等待的线程。
    - `getCount()`：获取当前计数器的值（用于调试或检查状态）。

---

### 使用示例

#### 1. 主线程等待子任务完成
以下示例展示主线程等待多个子线程完成任务：

```java
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {
    private static final int THREAD_COUNT = 3;
    private static final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始执行任务...");
                Thread.sleep((long) (Math.random() * 1000)); // 模拟任务耗时
                System.out.println(Thread.currentThread().getName() + " 任务完成");
                latch.countDown(); // 计数器减 1
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // 启动子线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }

        System.out.println("主线程等待所有子线程完成...");
        latch.await(); // 主线程等待计数器归零
        System.out.println("所有子线程已完成，主线程继续执行");
    }
}
```

**运行结果示例**：
```
主线程等待所有子线程完成...
Thread-0 开始执行任务...
Thread-1 开始执行任务...
Thread-2 开始执行任务...
Thread-1 任务完成
Thread-0 任务完成
Thread-2 任务完成
所有子线程已完成，主线程继续执行
```

主线程在 `await()` 处阻塞，直到所有子线程调用 `countDown()` 使计数器归零。

---

#### 2. 协调线程统一开始
以下示例展示如何使用 `CountDownLatch` 让多个线程在某个时刻同时开始执行：

```java
import java.util.concurrent.CountDownLatch;

public class StartTogetherExample {
    private static final int THREAD_COUNT = 3;
    private static final CountDownLatch startLatch = new CountDownLatch(1); // 用于统一开始

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 准备就绪");
                startLatch.await(); // 等待主线程信号
                System.out.println(Thread.currentThread().getName() + " 开始执行");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        // 启动工作线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }

        Thread.sleep(1000); // 模拟主线程准备时间
        System.out.println("主线程发出开始信号");
        startLatch.countDown(); // 计数器减到 0，触发所有线程开始
    }
}
```

**运行结果示例**：
```
Thread-0 准备就绪
Thread-1 准备就绪
Thread-2 准备就绪
主线程发出开始信号
Thread-0 开始执行
Thread-1 开始执行
Thread-2 开始执行
```

所有工作线程在 `await()` 处等待，直到主线程调用 `countDown()` 触发它们同时开始。

---

#### 3. 带超时的等待
以下示例展示如何设置等待超时：

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TimeoutExample {
    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws InterruptedException {
        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(2000); // 模拟长时间任务
                System.out.println("任务完成");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        worker.start();

        System.out.println("主线程等待，最多 1 秒...");
        boolean completed = latch.await(1, TimeUnit.SECONDS); // 等待 1 秒
        if (completed) {
            System.out.println("任务按时完成");
        } else {
            System.out.println("超时，任务未完成");
        }
    }
}
```

**运行结果示例**：
```
主线程等待，最多 1 秒...
超时，任务未完成
任务完成
```

如果任务未在指定时间内完成，`await()` 返回 `false`，主线程可以提前继续执行。

---

### 注意事项
1. **计数器不可重用**：一旦计数器减到 0，`CountDownLatch` 无法重置，后续的 `countDown()` 调用无效。
2. **线程数匹配**：需要确保 `countDown()` 的调用次数与初始计数匹配，否则可能导致死锁（等待永远不结束）。
3. **异常处理**：
    - 如果等待线程被中断，会抛出 `InterruptedException`。
    - 如果计数器初始值为负数，构造时会抛出 `IllegalArgumentException`。
4. **性能**：`CountDownLatch` 基于 `AbstractQueuedSynchronizer`（AQS），性能高效，适用于短时同步。

---

### 与 CyclicBarrier 的对比
- **用途**：
    - `CountDownLatch`：等待一组事件完成，一次性使用。
    - `CyclicBarrier`：多线程在某个点上同步，可重复使用。
- **计数方向**：
    - `CountDownLatch`：从初始值减到 0。
    - `CyclicBarrier`：等待线程数达到指定值。
- **重用性**：
    - `CountDownLatch`：不可重用。
    - `CyclicBarrier`：可重用。

---

### 总结
`CountDownLatch` 是一个简单而强大的同步工具，适用于主线程等待子任务完成或协调多线程统一开始的场景。它的核心是通过倒计数机制控制线程的等待和释放，易于使用且高效。如果需要一次性等待事件完成，`CountDownLatch` 是理想选择；如果需要循环同步，则可以考虑 `CyclicBarrier`。

希望这对你理解和使用 `CountDownLatch` 有帮助！如果有其他问题，欢迎继续提问。