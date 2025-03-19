package com.example.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 基本异步任务：异步执行一个任务并获取结果
 * @author wxg
 * @since 2025/3/19
 */
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

    // 主线程继续工作...
    // (等待 1 秒)
    // 结果: Hello, CompletableFuture!
}
