`Future` 是 Java 并发包（`java.util.concurrent`）中的一个接口，引入于 Java 5，用于表示异步计算的结果。它提供了一种机制，让调用者可以在任务执行完成后获取结果，或者检查任务的状态。`Future` 通常与线程池（如 `ExecutorService`）结合使用，是异步编程的基础工具之一。不过，相较于 `CompletableFuture`，它的功能较为简单，主要用于基本的异步任务管理。

下面我将详细介绍 `Future` 的用法，并提供示例代码。

---

### Future 的主要特点
1. **异步结果**：`Future` 表示一个尚未完成的任务的结果，允许调用者稍后获取。
2. **阻塞式获取**：通过 `get()` 方法获取结果时，如果任务未完成，会阻塞当前线程。
3. **状态检查**：可以检查任务是否完成或被取消。
4. **取消任务**：支持尝试取消尚未完成的任务。
5. **简单性**：功能有限，适合基本的异步场景，但不支持回调或链式操作。

---

### 基本方法
- **`get()`**：获取任务的结果，如果任务未完成则阻塞。
- **`get(long timeout, TimeUnit unit)`**：带超时的获取结果，超时未完成则抛出 `TimeoutException`。
- **`isDone()`**：检查任务是否已完成（无论成功还是异常）。
- **`isCancelled()`**：检查任务是否已被取消。
- **`cancel(boolean mayInterruptIfRunning)`**：尝试取消任务，参数决定是否中断正在执行的线程。

---

### 使用示例

#### 1. 基本用法
通过 `ExecutorService` 提交任务并使用 `Future` 获取结果：

```java
import java.util.concurrent.*;

public class FutureBasic {
    public static void main(String[] args) throws Exception {
        // 创建线程池
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // 提交任务
        Future<String> future = executor.submit(() -> {
            Thread.sleep(1000); // 模拟耗时操作
            return "Task Completed";
        });

        System.out.println("主线程继续工作...");

        // 获取结果（阻塞）
        String result = future.get();
        System.out.println("结果: " + result);

        // 关闭线程池
        executor.shutdown();
    }
}
```

**运行结果**：
```
主线程继续工作...
(等待 1 秒)
结果: Task Completed
```

提交任务后，主线程可以通过 `future.get()` 获取结果，但会阻塞直到任务完成。

---

#### 2. 检查任务状态
检查任务是否完成，并处理可能的异常：

```java
import java.util.concurrent.*;

public class FutureStatus {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(500);
            if (true) {
                throw new RuntimeException("任务失败");
            }
            return 42;
        });

        while (!future.isDone()) {
            System.out.println("任务尚未完成...");
            Thread.sleep(100);
        }

        try {
            Integer result = future.get();
            System.out.println("结果: " + result);
        } catch (ExecutionException e) {
            System.out.println("异常: " + e.getCause().getMessage());
        }

        executor.shutdown();
    }
}
```

**运行结果**：
```
任务尚未完成...
任务尚未完成...
任务尚未完成...
异常: 任务失败
```

使用 `isDone()` 检查任务状态，`get()` 时捕获 `ExecutionException` 处理异常。

---

#### 3. 带超时的获取
设置超时，避免无限等待：

```java
import java.util.concurrent.*;

public class FutureTimeout {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            Thread.sleep(2000); // 模拟长时间任务
            return "Task Done";
        });

        try {
            String result = future.get(1, TimeUnit.SECONDS); // 最多等待 1 秒
            System.out.println("结果: " + result);
        } catch (TimeoutException e) {
            System.out.println("任务超时");
        }

        executor.shutdown();
    }
}
```

**运行结果**：
```
任务超时
```

如果任务未在 1 秒内完成，`get()` 会抛出 `TimeoutException`。

---

#### 4. 取消任务
尝试取消正在执行的任务：

```java
import java.util.concurrent.*;

public class FutureCancel {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            Thread.sleep(2000);
            return "Task Done";
        });

        // 尝试取消任务
        boolean cancelled = future.cancel(true); // true 表示中断运行中的线程
        System.out.println("取消成功: " + cancelled);
        System.out.println("任务已取消: " + future.isCancelled());
        System.out.println("任务已完成: " + future.isDone());

        try {
            future.get(); // 获取已取消任务的结果
        } catch (CancellationException e) {
            System.out.println("异常: 任务已被取消");
        }

        executor.shutdown();
    }
}
```

**运行结果**：
```
取消成功: true
任务已取消: true
任务已完成: true
异常: 任务已被取消
```

调用 `cancel(true)` 尝试取消任务，若成功，`isCancelled()` 返回 `true`，后续 `get()` 会抛出 `CancellationException`。

---

### 注意事项
1. **阻塞问题**：`get()` 是阻塞式的，调用时需谨慎，避免影响主线程性能。
2. **异常处理**：
    - 任务抛出的异常被封装在 `ExecutionException` 中，通过 `get()` 抛出。
    - 如果任务被取消，`get()` 抛出 `CancellationException`。
3. **取消限制**：
    - `cancel()` 只能取消尚未开始或可中断的任务。如果任务已完成，取消无效。
    - 参数 `mayInterruptIfRunning` 为 `true` 时，仅对支持中断的线程（如通过 `Thread.sleep`）有效。
4. **资源管理**：使用 `ExecutorService` 时，记得调用 `shutdown()` 关闭线程池，避免资源泄漏。

---

### 与 CompletableFuture 的对比
- **`Future`**：
    - 功能简单，仅支持结果获取、状态检查和取消。
    - 获取结果必须通过阻塞式 `get()`。
    - 不支持回调或任务链。
- **`CompletableFuture`**：
    - 支持非阻塞回调（如 `thenApply`、`thenAccept`）。
    - 可手动完成、组合多个任务。
    - 更适合复杂异步场景。

---

### 总结
`Future` 是一个基础的异步工具，适用于简单的异步任务管理场景。通过与 `ExecutorService` 配合，它可以方便地提交任务并获取结果。然而，由于其阻塞式获取结果和功能有限的特性，在需要复杂异步流程时，推荐使用更现代的 `CompletableFuture`。如果你的需求仅是提交任务并稍后获取结果，`Future` 是一个轻量且易用的选择。

希望这对你理解和使用 `Future` 有帮助！如果有其他问题，欢迎继续提问。