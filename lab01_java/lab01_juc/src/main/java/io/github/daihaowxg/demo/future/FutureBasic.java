package io.github.daihaowxg.demo.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 基本用法
 * @author wxg
 * @since 2025/3/19
 */
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

        // 获取结果（阻塞）：提交任务后，主线程可以通过 future.get() 获取结果，但会阻塞直到任务完成。
        String result = future.get();
        System.out.println("结果: " + result);

        // 关闭线程池
        executor.shutdown();
    }

    // 主线程继续工作...
    // (等待 1 秒)
    // 结果: Task Completed
}
