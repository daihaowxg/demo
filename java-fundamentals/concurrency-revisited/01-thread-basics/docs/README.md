# 线程基础

## 核心概念

### 1. 什么是线程

线程是操作系统能够进行运算调度的最小单位，它被包含在进程之中，是进程中的实际运作单位。

### 2. 线程的创建方式

Java 中创建线程主要有以下几种方式：

#### 2.1 继承 Thread 类
```java
class MyThread extends Thread {
    @Override
    public void run() {
        // 线程执行的代码
    }
}
```

#### 2.2 实现 Runnable 接口
```java
class MyRunnable implements Runnable {
    @Override
    public void run() {
        // 线程执行的代码
    }
}
```

#### 2.3 实现 Callable 接口
```java
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        // 线程执行的代码，可以有返回值
        return "result";
    }
}
```

#### 2.4 使用线程池
```java
ExecutorService executor = Executors.newFixedThreadPool(5);
executor.submit(() -> {
    // 线程执行的代码
});
```

### 3. 线程的生命周期

线程在其生命周期中会经历以下状态：

1. **NEW（新建）**: 线程被创建但还未调用 `start()` 方法
2. **RUNNABLE（可运行）**: 调用 `start()` 后，线程可能正在运行或等待 CPU 时间片
3. **BLOCKED（阻塞）**: 线程等待获取监视器锁
4. **WAITING（等待）**: 线程无限期等待另一个线程执行特定操作
5. **TIMED_WAITING（超时等待）**: 线程等待另一个线程执行操作，但有时间限制
6. **TERMINATED（终止）**: 线程执行完毕或异常退出

### 4. 线程的常用方法

- `start()`: 启动线程
- `run()`: 线程执行的代码
- `sleep(long millis)`: 线程休眠
- `join()`: 等待线程终止
- `interrupt()`: 中断线程
- `isInterrupted()`: 检查线程是否被中断
- `yield()`: 提示调度器当前线程愿意让出 CPU 使用权

### 5. 守护线程 vs 用户线程

- **用户线程**: JVM 会等待所有用户线程执行完毕才会退出
- **守护线程**: JVM 不会等待守护线程执行完毕，当所有用户线程结束时，守护线程会自动终止

```java
Thread daemon = new Thread(() -> {
    // 守护线程代码
});
daemon.setDaemon(true);  // 设置为守护线程
daemon.start();
```

## 学习要点

- [ ] 理解线程与进程的区别
- [ ] 掌握线程的创建方式及其优缺点
- [ ] 理解线程的生命周期和状态转换
- [ ] 掌握线程的常用方法
- [ ] 理解守护线程的作用和使用场景
- [ ] 掌握线程中断机制

## 代码示例

参考 `src/main/java/io/github/daihaowxg/concurrency/basics/` 目录下的示例代码。
