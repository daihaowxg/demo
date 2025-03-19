线程池是 Java 中用于管理线程的一种机制，通过复用线程来减少线程创建和销毁的开销，提高性能和资源利用率。Java 的线程池主要通过 `java.util.concurrent` 包中的 `ExecutorService` 接口及其实现类（如 `ThreadPoolExecutor`）来实现。它是并发编程中的核心工具，广泛用于处理多任务场景。

下面我将详细介绍线程池的用法，包括基本概念、创建方式、常用方法和示例代码。

---

### 线程池的主要特点
1. **线程复用**：避免频繁创建和销毁线程，降低系统开销。
2. **任务队列**：未立即执行的任务可以排队等待，控制并发度。
3. **资源管理**：限制线程数量，防止资源耗尽。
4. **灵活配置**：支持自定义线程数、队列策略和拒绝策略。

---

### 线程池的核心类和接口
- **`Executor`**：顶层接口，定义了基本的 `execute(Runnable)` 方法。
- **`ExecutorService`**：扩展了 `Executor`，提供了提交任务、管理线程池生命周期的方法。
- **`ThreadPoolExecutor`**：线程池的具体实现类，支持自定义参数。
- **`Executors`**：工具类，提供创建常用线程池的工厂方法。

---

### 创建线程池的方式

#### 1. 使用 `Executors` 工厂方法
`Executors` 提供了几种预配置的线程池，适合常见场景：
- **`newFixedThreadPool(int nThreads)`**：固定大小的线程池，线程数不变。
- **`newCachedThreadPool()`**：缓存线程池，线程数动态调整，适合短时任务。
- **`newSingleThreadExecutor()`**：单线程池，保证任务顺序执行。
- **`newScheduledThreadPool(int corePoolSize)`**：支持定时和周期性任务的线程池。

#### 2. 使用 `ThreadPoolExecutor` 自定义
`ThreadPoolExecutor` 是最灵活的方式，允许自定义线程池参数：
```java
ThreadPoolExecutor executor = new ThreadPoolExecutor(
    int corePoolSize,           // 核心线程数
    int maximumPoolSize,        // 最大线程数
    long keepAliveTime,         // 空闲线程存活时间
    TimeUnit unit,              // 时间单位
    BlockingQueue<Runnable> workQueue, // 任务队列
    ThreadFactory threadFactory,       // 线程工厂
    RejectedExecutionHandler handler   // 拒绝策略
);
```

---

### 常用方法
- **提交任务**：
    - `execute(Runnable task)`：执行无返回值的任务。
    - `submit(Runnable task)`：提交任务，返回 `Future<?>`。
    - `submit(Callable<T> task)`：提交有返回值的任务，返回 `Future<T>`。
- **关闭线程池**：
    - `shutdown()`：平滑关闭，不接受新任务，等待现有任务完成。
    - `shutdownNow()`：立即关闭，尝试中断运行任务并返回未执行任务列表。
    - `isShutdown()`：检查是否已关闭。
    - `isTerminated()`：检查是否所有任务已完成。
- **其他**：
    - `awaitTermination(long timeout, TimeUnit unit)`：等待线程池终止。

---

### 使用示例

#### 1. 使用 `newFixedThreadPool`
创建一个固定大小的线程池：

```java
import java.util.concurrent.*;

public class FixedThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        // 创建固定 3 个线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交任务
        for (int i = 0; i < 5; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // 模拟耗时任务
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("所有任务完成");
    }
}
```

**运行结果示例**：
```
任务 0 执行，线程: pool-1-thread-1
任务 1 执行，线程: pool-1-thread-2
任务 2 执行，线程: pool-1-thread-3
(等待 1 秒)
任务 3 执行，线程: pool-1-thread-1
任务 4 执行，线程: pool-1-thread-2
(等待 1 秒)
所有任务完成
```

线程池复用了 3 个线程处理 5 个任务。

---

#### 2. 使用 `newCachedThreadPool`
动态调整线程数的线程池：

```java
import java.util.concurrent.*;

public class CachedThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < 5; i++) {
            int taskId = i;
            executor.execute(() -> {
                System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }
}
```

**运行结果示例**：
```
任务 0 执行，线程: pool-1-thread-1
任务 1 执行，线程: pool-1-thread-2
任务 2 执行，线程: pool-1-thread-3
任务 3 执行，线程: pool-1-thread-4
任务 4 执行，线程: pool-1-thread-5
```

线程数根据任务需求动态增加，空闲线程会被回收。

---

#### 3. 使用 `ThreadPoolExecutor` 自定义
自定义线程池参数：

```java
import java.util.concurrent.*;

public class CustomThreadPoolExample {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2, // 核心线程数
            4, // 最大线程数
            60L, // 空闲线程存活时间
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(2), // 任务队列容量
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );

        // 提交 6 个任务
        for (int i = 0; i < 6; i++) {
            int taskId = i;
            try {
                executor.execute(() -> {
                    System.out.println("任务 " + taskId + " 执行，线程: " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            } catch (RejectedExecutionException e) {
                System.out.println("任务 " + taskId + " 被拒绝");
            }
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
    }
}
```

**运行结果示例**：
```
任务 0 执行，线程: pool-1-thread-1
任务 1 执行，线程: pool-1-thread-2
任务 2 在队列中等待
任务 3 在队列中等待
任务 4 执行，线程: pool-1-thread-3
任务 5 被拒绝
```

核心线程 2 个，队列容量 2，最大线程 4，超出时触发拒绝策略。

---

#### 4. 使用 `submit` 获取结果
提交任务并通过 `Future` 获取结果：

```java
import java.util.concurrent.*;

public class FutureWithThreadPool {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(1000);
            return 42;
        });

        System.out.println("等待结果...");
        Integer result = future.get();
        System.out.println("结果: " + result);

        executor.shutdown();
    }
}
```

**运行结果**：
```
等待结果...
(等待 1 秒)
结果: 42
```

---

### 注意事项
1. **线程池关闭**：
    - 使用 `shutdown()` 平滑关闭，避免任务丢失。
    - `shutdownNow()` 会中断运行任务，谨慎使用。
2. **拒绝策略**：
    - `AbortPolicy`：拒绝并抛出异常（默认）。
    - `CallerRunsPolicy`：由提交任务的线程执行。
    - `DiscardPolicy`：静默丢弃。
    - `DiscardOldestPolicy`：丢弃队列中最老的任务。
3. **避免滥用 `Executors`**：
    - `newCachedThreadPool` 可能创建过多线程，导致资源耗尽。
    - `newFixedThreadPool` 默认队列无界，可能内存溢出。
    - 建议使用 `ThreadPoolExecutor` 自定义。
4. **异常处理**：`execute` 不抛出任务异常，需在任务内部处理；`submit` 可通过 `Future.get()` 获取异常。

---

### 总结
线程池是 Java 并发编程的重要工具，通过 `ExecutorService` 和 `ThreadPoolExecutor` 可以高效管理多线程任务。使用 `Executors` 提供的工厂方法可以快速创建线程池，而 `ThreadPoolExecutor` 则适合需要精细控制的场景。合理配置线程数、队列和拒绝策略，能显著提升程序性能和稳定性。

希望这对你理解和使用线程池有帮助！如果有其他问题，欢迎继续提问。