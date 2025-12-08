# Java 并发基础：Thread、Runnable 与 Callable 的区别

在 Java 并发编程中，创建线程主要有三种方式：继承 `Thread` 类、实现 `Runnable` 接口以及实现 `Callable` 接口。本文将详细对比这三者的区别及适用场景。

## 1. 核心区别对比表

| 特性 | Thread | Runnable | Callable |
| :--- | :--- | :--- | :--- |
| **类型** | 类 (`java.lang.Thread`) | 函数式接口 (`java.lang.Runnable`) | 函数式接口 (`java.util.concurrent.Callable`) |
| **实现方式** | 继承 (`extends`) | 实现 (`implements`) | 实现 (`implements`) |
| **返回值** | 无 | 无 (`void`) | 有 (泛型 `V`) |
| **异常处理** | run() 不能抛出受检异常 | run() 不能抛出受检异常 | call() 可以抛出 `Exception` |
| **启动方式** | `new MyThread().start()` | `new Thread(new MyRunnable()).start()` | 需配合 `FutureTask` 或线程池使用 |
| **多继承限制** | 此时无法继承其他类 | 不受限制，可继承其他类 | 不受限制，可继承其他类 |
| **适用场景** | 这里的线程逻辑无法分离，不推荐 | 简单的异步任务，无需返回值 | 需要返回值或可能抛出异常的任务 |

## 2. 详细解析

### 2.1 Thread 类
`Thread` 是 Java 中对线程的抽象。通过继承 `Thread` 类并重写 `run()` 方法来定义线程执行的逻辑。

**缺点**：
- **单继承限制**：Java 不支持多继承，继承了 `Thread` 就无法继承其他业务类。
- **耦合度高**：任务逻辑（`run` 方法）与线程控制逻辑（`Thread` 类）耦合在一起，不利于资源共享。

### 2.2 Runnable 接口
`Runnable` 只有一个抽象方法 `run()`，用于定义任务逻辑。

**优点**：
- **避免单继承局限**：实现接口的同时可以继承其他类。
- **解耦**：将任务（`Runnable`）与执行机制（`Thread`）分离。
- **资源共享**：同一个 `Runnable` 实例可以被多个线程共享。

**缺点**：
- 任务执行完毕后**无法返回结果**。
- `run()` 方法签名上没有 `throws`，**无法抛出受检异常**（Checked Exception），只能在内部捕获处理。

### 2.3 Callable 接口
`Callable` 是 JDK 1.5 引入的，位于 `java.util.concurrent` 包下。它类似于 `Runnable`，但功能更强大。

**优点**：
- **有返回值**：`call()` 方法可以返回执行结果。
- **支持异常**：`call()` 方法可以抛出异常，配合 `Future` 可以捕获任务执行过程中的错误。

**用法**：
通常需要配合 `FutureTask` 或线程池（`ExecutorService`）使用。

## 3. 代码示例

### 3.1 继承 Thread (不推荐)
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running");
    }
}

// 启动
new MyThread().start();
```

### 3.2 实现 Runnable (推荐用于无结果任务)
```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Runnable running");
    }
}

// 启动
new Thread(new MyRunnable()).start();

// 或者使用 Lambda (最常见)
new Thread(() -> System.out.println("Lambda Runnable")).start();
```

### 3.3 实现 Callable (推荐用于有结果任务)
```java
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ExecutionException;

class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(100); // 模拟耗时
        return "Task Result";
    }
}

// 启动方式 1: 使用 FutureTask
FutureTask<String> task = new FutureTask<>(new MyCallable());
new Thread(task).start();

try {
    // get() 会阻塞直到任务完成
    String result = task.get();
    System.out.println(result);
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}
```

## 4. 最佳实践

1. **优先选择接口而不是继承**：除非你需要修改 `Thread` 类的底层行为（极少见），否则应始终优先实现 `Runnable` 或 `Callable` 接口。这符合"组合优于继承"的设计原则。
2. **使用线程池**：在实际生产环境中，避免显式地 `new Thread()`。应该使用 `ExecutorService` 来管理线程生命周期和复用线程资源。
   - 提交 `Runnable` 任务：`executor.execute(runnable)`
   - 提交 `Callable` 任务：`Future<T> future = executor.submit(callable)`
3. **根据需求选择接口**：
   - 只需要执行动作，不关心结果 -> `Runnable`
   - 需要计算结果或处理异常 -> `Callable`

## 5. 异常处理详解 (新增)

如果任务执行过程中发生异常，处理方式如下：

### 5.1 Runnable / Thread 异常处理
由于 `run()` 方法签名没有声明抛出异常，因此：
- **受检异常 (Checked Exception)**: 必须在 `run()` 内部使用 `try-catch` 捕获处理，无法抛出。
- **运行时异常 (RuntimeException)**: 如果未捕获，会导致线程终止。JVM 会将堆栈信息打印到控制台，但这不会影响主线程或其他线程的运行。

**解决方案**:
1. **Try-Catch**: 在 `run()` 方法内部包裹 try-catch（最常用）。
2. **UncaughtExceptionHandler**: 为线程设置 `UncaughtExceptionHandler` 来处理未捕获的运行时异常。

```java
Thread t = new Thread(() -> {
    throw new RuntimeException("Error");
});
t.setUncaughtExceptionHandler((thread, e) -> {
    System.out.println("线程 " + thread.getName() + " 发生异常: " + e.getMessage());
});
t.start();
```

### 5.2 Callable 异常处理
`Callable` 的 `call()` 方法签名声明了 `throws Exception`，因此：
- 它可以直接抛出受检异常或运行时异常。
- 异常会被封装在 `java.util.concurrent.ExecutionException` 中。
- 当调用 `Future.get()` 获取结果时，会抛出 `ExecutionException`，调用者需要在这里处理异常。

```java
Future<Object> future = executor.submit(() -> {
    throw new Exception("Error");
});

try {
    future.get();
} catch (ExecutionException e) {
    // 真正的异常包含在 cause 中
    Throwable cause = e.getCause();
    System.out.println("捕获到任务异常: " + cause.getMessage());
}
```
