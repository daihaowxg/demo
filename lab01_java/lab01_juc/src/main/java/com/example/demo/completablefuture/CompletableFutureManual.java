package com.example.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 手动控制 CompletableFuture 的完成状态
 * @author wxg
 * @since 2025/3/19
 */
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

    // 等待结果...
    // (等待 1 秒)
    // 结果: Manually Completed
}
