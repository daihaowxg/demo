package io.github.daihaowxg.demo.future;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 取消任务：尝试取消正在执行的任务
 * @author wxg
 * @since 2025/3/19
 */
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

        // 调用 cancel(true) 尝试取消任务，若成功，isCancelled() 返回 true，后续 get() 会抛出 CancellationException。
    }

    // 取消成功: true
    // 任务已取消: true
    // 任务已完成: true
    // 异常: 任务已被取消
}
