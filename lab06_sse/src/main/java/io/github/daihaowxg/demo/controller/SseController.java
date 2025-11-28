package io.github.daihaowxg.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SSE (Server-Sent Events) 控制器
 * 该控制器提供了一个 SSE 端点，客户端可以通过此端点接收服务器推送的事件。
 */
@RestController
public class SseController {

    /**
     * SSE 端点，客户端可以通过访问 "/sse" 订阅服务器推送的事件流。
     * 服务器每秒发送一个事件，持续 10 次后关闭连接。
     *
     * @return SseEmitter 实例，支持服务器推送消息到客户端
     */
    @GetMapping("/sse")
    public SseEmitter streamEvents() {
        // 创建一个 SseEmitter 实例，默认超时时间为 30 秒（可调整）
        SseEmitter emitter = new SseEmitter();

        // 使用单线程执行任务，避免阻塞主线程
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                // 模拟数据推送，每秒发送一条消息，共发送 10 次
                for (int i = 0; i < 10; i++) {
                    emitter.send("Event " + i); // 发送数据给客户端
                    Thread.sleep(1000); // 模拟间隔 1 秒
                }
                emitter.complete(); // 结束 SSE 连接
            } catch (Exception e) {
                emitter.completeWithError(e); // 发生异常时结束连接
            }
        });

        // 关闭线程池，避免资源泄漏（但可能导致任务未执行完就关闭）
        executor.shutdown();

        return emitter; // 返回 SseEmitter，客户端将持续接收数据
    }
}
