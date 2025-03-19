package com.example.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 异常处理
 * @author wxg
 * @since 2025/3/19
 */
public class CompletableFutureException {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (true) {
                throw new RuntimeException("任务失败");
            }
            return "Success";
        }).exceptionally(throwable -> {
            // 使用 exceptionally 可以在任务失败时返回默认值
            System.out.println("异常: " + throwable.getMessage());
            return "Default Value"; // 提供默认值
        });

        System.out.println("结果: " + future.get());
    }

    // 异常: java.lang.RuntimeException: 任务失败
    // 结果: Default Value
}
