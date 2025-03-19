`CyclicBarrier` 是 Java 并发包（`java.util.concurrent`）中的一种同步工具，中文常译为“循环屏障”。它允许一组线程在某个共同点（屏障点）上相互等待，直到所有线程都到达该点后，再一起继续执行。与 `CountDownLatch` 不同，`CyclicBarrier` 是可重用的（循环使用），适用于需要多次同步的场景。

下面我将详细介绍 `CyclicBarrier` 的用法，并提供示例代码。

---

### CyclicBarrier 的主要特点
1. **屏障等待**：指定数量的线程（称为“参与者”）必须全部到达屏障点后，才能集体通过。
2. **可重用**：一旦所有线程通过屏障，`CyclicBarrier` 会重置状态，可以再次使用，而 `CountDownLatch` 是一次性的。
3. **可选的屏障动作**：可以在所有线程到达屏障时执行一个自定义的动作（通过 `Runnable` 指定）。
4. **异常处理**：如果某个线程在等待过程中被中断或超时，屏障会“破裂”（broken），所有等待线程会抛出异常。

---

### 基本方法
- **构造方法**：
    - `CyclicBarrier(int parties)`：指定参与者的数量。
    - `CyclicBarrier(int parties, Runnable barrierAction)`：指定参与者数量，并在所有线程到达屏障时执行指定的 `barrierAction`。
- **核心方法**：
    - `await()`：当前线程等待，直到所有参与者到达屏障点。
    - `await(long timeout, TimeUnit unit)`：带超时的等待，如果超时未到达所有参与者，会抛出 `TimeoutException`。
    - `reset()`：重置屏障，恢复初始状态（可能中断正在等待的线程）。
    - `isBroken()`：检查屏障是否已破裂（例如因中断或超时）。
    - `getNumberWaiting()`：获取当前正在等待的线程数。

---

### 使用示例

#### 1. 基本用法
以下示例模拟多个线程在某个点上等待，然后一起继续执行：

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {
    private static final int THREAD_COUNT = 3;
    private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始准备...");
                Thread.sleep((long) (Math.random() * 1000)); // 模拟准备时间
                System.out.println(Thread.currentThread().getName() + " 准备完成，到达屏障");
                barrier.await(); // 等待所有线程到达
                System.out.println(Thread.currentThread().getName() + " 通过屏障，继续执行");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // 创建并启动线程
        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}
```

**运行结果示例**：
```
Thread-0 开始准备...
Thread-1 开始准备...
Thread-2 开始准备...
Thread-1 准备完成，到达屏障
Thread-0 准备完成，到达屏障
Thread-2 准备完成，到达屏障
Thread-0 通过屏障，继续执行
Thread-1 通过屏障，继续执行
Thread-2 通过屏障，继续执行
```

所有线程在到达 `barrier.await()` 时等待，直到 3 个线程都到达后一起继续执行。

---

#### 2. 带屏障动作的用法
可以在所有线程到达屏障时执行一个额外的动作：

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierWithAction {
    private static final int THREAD_COUNT = 3;
    private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () -> {
        System.out.println("所有线程已到达屏障，执行屏障动作！");
    });

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 开始工作...");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(Thread.currentThread().getName() + " 到达屏障");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " 继续工作");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Thread(task, "Thread-" + i).start();
        }
    }
}
```

**运行结果示例**：
```
Thread-0 开始工作...
Thread-1 开始工作...
Thread-2 开始工作...
Thread-2 到达屏障
Thread-0 到达屏障
Thread-1 到达屏障
所有线程已到达屏障，执行屏障动作！
Thread-0 继续工作
Thread-1 继续工作
Thread-2 继续工作
```

屏障动作会在所有线程到达后、继续执行前由最后一个到达的线程执行。

---

#### 3. 可重用性示例
`CyclicBarrier` 可以循环使用，以下示例展示多次同步：

```java
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierReuse {
    private static final int THREAD_COUNT = 2;
    private static final CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT);

    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    System.out.println(Thread.currentThread().getName() + " 开始第 " + i + " 次任务");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println(Thread.currentThread().getName() + " 到达第 " + i + " 次屏障");
                    barrier.await();
                    System.out.println(Thread.currentThread().getName() + " 完成第 " + i + " 次任务");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(task, "Thread-A").start();
        new Thread(task, "Thread-B").start();
    }
}
```

**运行结果示例**：
```
Thread-A 开始第 1 次任务
Thread-B 开始第 1 次任务
Thread-A 到达第 1 次屏障
Thread-B 到达第 1 次屏障
Thread-A 完成第 1 次任务
Thread-B 完成第 1 次任务
Thread-A 开始第 2 次任务
Thread-B 开始第 2 次任务
...
```

屏障在每次所有线程到达后自动重置，可以重复使用。

---

### 注意事项
1. **线程数匹配**：调用 `await()` 的线程数必须等于构造时指定的 `parties`，否则会导致死锁。
2. **异常处理**：
    - 如果某个线程在 `await()` 时被中断，会抛出 `InterruptedException`，屏障变为“破裂”状态，其他等待线程会抛出 `BrokenBarrierException`。
    - 如果超时（使用 `await(timeout, unit)`），会抛出 `TimeoutException`，同样导致屏障破裂。
3. **屏障动作的执行者**：屏障动作由最后一个到达屏障的线程执行。
4. **重置的影响**：调用 `reset()` 会中断当前等待的线程，并抛出 `BrokenBarrierException`，需谨慎使用。

---

### 与 CountDownLatch 的对比
- **用途**：
    - `CountDownLatch`：用于等待一组事件完成（倒计数），一次性使用。
    - `CyclicBarrier`：用于多线程在某个点上同步，可重复使用。
- **计数方向**：
    - `CountDownLatch`：从初始值减到 0。
    - `CyclicBarrier`：等待线程数达到指定值。
- **重用性**：
    - `CountDownLatch`：不可重用。
    - `CyclicBarrier`：可重用。

---

### 总结
`CyclicBarrier` 是一个灵活的同步工具，适合需要多线程在某个点上集合后再继续执行的场景，例如并行计算、阶段性任务同步等。它的可重用性和可选屏障动作使其在某些场景下比 `CountDownLatch` 更实用。通过合理使用 `await()` 和异常处理，可以确保线程间的协调高效且安全。

如果还有其他问题，欢迎继续提问！