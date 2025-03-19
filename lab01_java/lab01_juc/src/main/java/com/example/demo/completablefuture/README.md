`CompletableFuture` 是 Java 8 引入的 `java.util.concurrent` 包中的一个类，扩展了 `Future` 的功能，提供了更强大的异步编程能力。它允许开发者以声明式的方式处理异步任务，支持任务的链式调用、异常处理、结果组合以及多任务协调等特性。相较于传统的 `Future`，`CompletableFuture` 更灵活，特别适合处理复杂的异步流程。

下面我将详细介绍 `CompletableFuture` 的用法，并提供示例代码。

---

### CompletableFuture 的主要特点
1. **异步执行**：任务可以在单独的线程中运行，不会阻塞主线程。
2. **链式操作**：支持通过方法链（如 `thenApply`、`thenAccept`）处理任务结果。
3. **异常处理**：提供 `exceptionally` 和 `handle` 方法处理异步任务中的异常。
4. **任务组合**：支持多个 `CompletableFuture` 的组合操作（如 `allOf`、`anyOf`）。
5. **手动完成**：可以通过 `complete` 或 `completeExceptionally` 手动设置结果或异常。
6. **非阻塞获取结果**：与 `Future.get()` 的阻塞式不同，`CompletableFuture` 提供回调式处理。

---

### 基本方法
- **创建 CompletableFuture**：
    - `CompletableFuture<T>`：构造一个空的 `CompletableFuture`，需要手动完成。
    - `supplyAsync(Supplier<T>)`：异步执行一个返回结果的任务。
    - `runAsync(Runnable)`：异步执行一个无返回值的任务。
- **处理结果**：
    - `thenApply(Function<T, U>)`：对结果应用转换函数，返回新的 `CompletableFuture`。
    - `thenAccept(Consumer<T>)`：消费结果，无返回值。
    - `thenRun(Runnable)`：在任务完成后执行一个动作，不依赖结果。
- **异常处理**：
    - `exceptionally(Function<Throwable, T>)`：处理异常并返回默认值。
    - `handle(BiFunction<T, Throwable, U>)`：同时处理结果和异常。
- **组合操作**：
    - `thenCompose(Function<T, CompletableFuture<U>>)`：将一个任务的结果传递给另一个异步任务。
    - `allOf(CompletableFuture<?>...)`：等待所有任务完成。
    - `anyOf(CompletableFuture<?>...)`：等待任一任务完成。
- **获取结果**：
    - `get()`：阻塞式获取结果（继承自 `Future`）。
    - `join()`：类似 `get()`，但抛出未检查异常。
    - `getNow(T defaultValue)`：立即获取结果，若未完成则返回默认值。

---

### 使用示例

#### 1. 基本异步任务
异步执行一个任务并获取结果：

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureBasic {
    public static void main(String[] args) throws Exception {
        // 异步计算
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000); // 模拟耗时操作
                return "Hello, CompletableFuture!";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 主线程继续执行
        System.out.println("主线程继续工作...");

        // 获取结果（阻塞）
        String result = future.get();
        System.out.println("结果: " + result);
    }
}
```

**运行结果**：
```
主线程继续工作...
(等待 1 秒)
结果: Hello, CompletableFuture!
```

---

#### 2. 链式操作
对异步任务结果进行转换和处理：

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureChain {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + ", World") // 转换结果
                .thenApply(String::toUpperCase); // 再次转换

        System.out.println("结果: " + future.get());
    }
}
```

**运行结果**：
```
结果: HELLO, WORLD
```

使用 `thenApply` 可以链式地对结果进行处理。

---

#### 3. 异常处理
处理异步任务中的异常：

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureException {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("任务失败");
            }
            return "Success";
        }).exceptionally(throwable -> {
            System.out.println("异常: " + throwable.getMessage());
            return "Default Value"; // 提供默认值
        });

        System.out.println("结果: " + future.get());
    }
}
```

**运行结果**：
```
异常: java.lang.RuntimeException: 任务失败
结果: Default Value
```

使用 `exceptionally` 可以在任务失败时返回默认值。

---

#### 4. 任务组合
组合多个异步任务：

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureCombine {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 2";
        });

        // 等待所有任务完成
        CompletableFuture<Void> all = CompletableFuture.allOf(future1, future2);
        all.thenRun(() -> {
            try {
                String result = future1.get() + " & " + future2.get();
                System.out.println("所有任务完成: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).get();

        // 等待任一任务完成
        CompletableFuture<Object> any = CompletableFuture.anyOf(future1, future2);
        System.out.println("第一个完成的任务: " + any.get());
    }
}
```

**运行结果**：
```
第一个完成的任务: Task 2
所有任务完成: Task 1 & Task 2
```

`allOf` 等待所有任务完成，`anyOf` 返回最快完成的任务结果。

---

#### 5. 手动完成
手动控制 `CompletableFuture` 的完成状态：

```java
import java.util.concurrent.CompletableFuture;

public class CompletableFutureManual {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();

        // 在另一个线程中手动完成
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                future.complete("Manually Completed"); // 手动设置结果
            } catch (InterruptedException e) {
                future.completeExceptionally(e); // 手动设置异常
            }
        }).start();

        System.out.println("等待结果...");
        System.out.println("结果: " + future.get());
    }
}
```

**运行结果**：
```
等待结果...
(等待 1 秒)
结果: Manually Completed
```

---

### 注意事项
1. **线程池**：默认情况下，`supplyAsync` 和 `runAsync` 使用 `ForkJoinPool.commonPool()`。可以通过重载方法指定自定义线程池（如 `supplyAsync(Supplier<T>, Executor)`）。
2. **异常传播**：未处理的异常会存储在 `CompletableFuture` 中，使用 `get()` 或 `join()` 时抛出。
3. **阻塞与非阻塞**：尽量避免直接调用 `get()` 或 `join()`，优先使用回调方法（如 `thenApply`、`thenAccept`）实现非阻塞处理。
4. **超时支持**：Java 9 引入了 `orTimeout` 和 `completeOnTimeout` 方法，用于设置超时。

---

### 与 Future 的对比
- **Future**：
    - 只能通过 `get()` 阻塞式获取结果。
    - 不支持回调或链式操作。
    - 无法手动完成。
- **CompletableFuture**：
    - 支持非阻塞回调和链式处理。
    - 可手动完成或组合多个任务。
    - 更适合现代异步编程。

---

### 总结
`CompletableFuture` 是 Java 中强大的异步编程工具，适用于需要处理复杂任务依赖、异常恢复或多任务协调的场景。通过链式调用、异常处理和任务组合，它大大简化了异步代码的编写。如果你的应用涉及异步计算或并行处理，`CompletableFuture` 是一个非常值得掌握的工具。

希望这对你理解和使用 `CompletableFuture` 有帮助！如果有其他问题，欢迎继续提问。