package io.github.daihaowxg.concurrency.basics;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 演示 Thread/Runnable 中的异常处理
 *
 * 主要包含：
 * 1. 为什么 run() 方法不能抛出受检异常
 * 2. 如何使用 try-catch 在内部处理
 * 3. 如何使用 UncaughtExceptionHandler 处理未捕获异常
 * 4. 对比 Callable 的异常处理
 *
 * @author wxg
 * @since 2025/12/08
 */
public class ThreadExceptionExample {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 线程异常处理示例 ===\n");

        // 1. 演示默认行为：异常导致线程终止，控制台打印堆栈
        demonstrateDefaultException();

        Thread.sleep(1000); // 等待上面执行完
        System.out.println("\n--------------------\n");

        // 2. 演示正确做法：在 run 内部 try-catch
        demonstrateTryCatch();

        Thread.sleep(1000);
        System.out.println("\n--------------------\n");

        // 3. 演示 UncaughtExceptionHandler：捕获漏网之鱼
        demonstrateUncaughtExceptionHandler();

        Thread.sleep(1000);
        System.out.println("\n--------------------\n");

        // 4. 演示 Callable 异常处理：在 get() 时抛出
        demonstrateCallableException();
    }

    /**
     * 场景1: 这里演示如果 run() 里面抛出 RuntimeException 且没捕获
     * 结果: 线程直接结束，默认会在控制台打印堆栈信息（System.err）。
     * 主线程不会受影响。
     */
    private static void demonstrateDefaultException() {
        System.out.println("1. [Default] 启动一个会抛异常的线程...");
        Thread t = new Thread(() -> {
            throw new RuntimeException("这是线程内部抛出的运行时异常");
        }, "Exception-Thread");
        t.start();
    }

    /**
     * 场景2: 推荐做法 - 在 run() 内部进行 try-catch
     */
    private static void demonstrateTryCatch() {
        System.out.println("2. [Try-Catch] 启动内部捕获异常的线程...");
        Thread t = new Thread(() -> {
            try {
                // 模拟业务逻辑
                throw new IllegalStateException("业务逻辑出错");
            } catch (Exception e) {
                // 在这里处理异常，比如记录日志、清理资源
                System.out.println("   [已捕获] 线程内捕获到异常: " + e.getMessage());
            }
        }, "TryCatch-Thread");
        t.start();
    }

    /**
     * 场景3: 使用 UncaughtExceptionHandler 全局或单独处理
     * 当 run() 抛出未捕获异常时，JVM 会回调这个 Handler
     */
    private static void demonstrateUncaughtExceptionHandler() {
        System.out.println("3. [Handler] 使用 UncaughtExceptionHandler...");
        Thread t = new Thread(() -> {
            throw new RuntimeException("逃过了 try-catch 的异常");
        }, "Handler-Thread");

        // 设置该线程的异常处理器
        t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("   [Handler捕获] 线程 '" + t.getName() + "' 挂了, 原因: " + e.getMessage());
                // 这里可以做告警、重启线程等兜底操作
            }
        });

        t.start();
    }

    /**
     * 场景4: Callable 的异常会被封装在 ExecutionException 中
     */
    private static void demonstrateCallableException() {
        System.out.println("4. [Callable] 演示 Callable 异常...");
        Callable<String> task = () -> {
            throw new Exception("Callable 允许抛出受检异常");
        };

        FutureTask<String> futureTask = new FutureTask<>(task);
        new Thread(futureTask).start();

        try {
            futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("   [Main线程捕获] 拿到 ExecutionException, 原始异常是: " + e.getCause().getMessage());
        }
    }
}
