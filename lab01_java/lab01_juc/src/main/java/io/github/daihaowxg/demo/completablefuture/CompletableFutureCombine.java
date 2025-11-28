package io.github.daihaowxg.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 组合多个异步任务
 * @author wxg
 * @since 2025/3/19
 */
public class CompletableFutureCombine {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task 2";
        });

        // 等待所有任务完成
        CompletableFuture<Void> all = CompletableFuture.allOf(future1, future2);
        all.thenRun(() -> {
            try {
                String result = future1.get() + " & " + future2.get();
                System.out.println("所有任务完成: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).get();

        // 等待任一任务完成
        CompletableFuture<Object> any = CompletableFuture.anyOf(future1, future2);
        System.out.println("第一个完成的任务: " + any.get());
    }

    // 所有任务完成: Task 1 & Task 2
    // 第一个完成的任务: Task 1
}
