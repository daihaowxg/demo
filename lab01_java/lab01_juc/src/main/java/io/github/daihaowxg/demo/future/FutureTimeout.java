package io.github.daihaowxg.demo.future;

import java.util.concurrent.*;

/**
 * 带超时的获取
 * @author wxg
 * @since 2025/3/19
 */
public class FutureTimeout {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<String> future = executor.submit(() -> {
            Thread.sleep(2000); // 模拟长时间任务
            return "Task Done";
        });

        try {
            // 如果任务未在 1 秒内完成，get() 会抛出 TimeoutException。
            String result = future.get(1, TimeUnit.SECONDS); // 最多等待 1 秒
            System.out.println("结果: " + result);
        } catch (TimeoutException e) {
            System.out.println("任务超时");
        }

        executor.shutdown();
    }

    // 任务超时
}
