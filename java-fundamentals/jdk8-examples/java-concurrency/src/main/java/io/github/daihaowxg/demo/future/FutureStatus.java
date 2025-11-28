package io.github.daihaowxg.demo.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 检查任务状态：检查任务是否完成，并处理可能的异常
 * @author wxg
 * @since 2025/3/19
 */
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

    // 任务尚未完成...
    // 任务尚未完成...
    // 任务尚未完成...
    // 任务尚未完成...
    // 任务尚未完成...
    // 异常: 任务失败
}
