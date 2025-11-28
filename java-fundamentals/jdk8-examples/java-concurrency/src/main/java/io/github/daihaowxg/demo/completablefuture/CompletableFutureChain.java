package io.github.daihaowxg.demo.completablefuture;

import java.util.concurrent.CompletableFuture;

/**
 * 链式操作：对异步任务结果进行转换和处理
 * @author wxg
 * @since 2025/3/19
 */
public class CompletableFutureChain {
    public static void main(String[] args) throws Exception {
        // 使用 thenApply 可以链式地对结果进行处理
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                .thenApply(s -> s + ", World") // 转换结果
                .thenApply(String::toUpperCase); // 再次转换

        System.out.println("结果: " + future.get()); // 结果: HELLO, WORLD
    }
}