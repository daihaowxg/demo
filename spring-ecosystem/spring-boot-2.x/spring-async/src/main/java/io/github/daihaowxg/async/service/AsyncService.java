package io.github.daihaowxg.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 异步服务示例
 * <p>
 * 演示 {@code @Async} 注解的各种使用场景
 * {@code @Async} 不仅可以用于事件监听，还可以用于任何需要异步执行的方法
 *
 * @author wxg
 */
@Slf4j
@Service
public class AsyncService {

    /**
     * 场景 1：无返回值的异步方法
     * <p>
     * 最常见的用法，适用于不需要关心执行结果的场景
     * 例如：发送邮件、记录日志、发送通知等
     */
    @Async
    public void sendEmailAsync(String to, String subject) {
        String threadName = Thread.currentThread().getName();
        log.info("【异步发送邮件】线程: {}, 收件人: {}, 主题: {}", threadName, to, subject);
        
        // 模拟耗时操作
        try {
            Thread.sleep(2000);
            log.info("【邮件发送成功】收件人: {}", to);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 场景 2：有返回值的异步方法
     * <p>
     * 使用 CompletableFuture 返回异步结果
     * 调用方可以通过 Future 获取执行结果
     *
     * @param userId 用户ID
     * @return 用户信息的 Future
     */
    @Async
    public CompletableFuture<String> getUserInfoAsync(Long userId) {
        String threadName = Thread.currentThread().getName();
        log.info("【异步查询用户】线程: {}, 用户ID: {}", threadName, userId);
        
        // 模拟数据库查询
        try {
            Thread.sleep(1000);
            String userInfo = "User-" + userId + " (Name: Alice)";
            log.info("【查询完成】用户信息: {}", userInfo);
            return CompletableFuture.completedFuture(userInfo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture.failedFuture(e);
        }
    }

    /**
     * 场景 3：指定线程池的异步方法
     * <p>
     * 可以为不同的异步方法指定不同的线程池
     * 实现资源隔离，避免相互影响
     *
     * @param taskName 任务名称
     */
    @Async("taskExecutor")  // 指定使用名为 taskExecutor 的线程池
    public void executeTaskAsync(String taskName) {
        String threadName = Thread.currentThread().getName();
        log.info("【异步执行任务】线程: {}, 任务: {}", threadName, taskName);
        
        try {
            Thread.sleep(1500);
            log.info("【任务完成】任务: {}", taskName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 场景 4：批量异步处理
     * <p>
     * 处理批量数据时，每条数据异步处理
     * 提高整体处理速度
     *
     * @param orderId 订单ID
     */
    @Async
    public void processOrderAsync(Long orderId) {
        String threadName = Thread.currentThread().getName();
        log.info("【异步处理订单】线程: {}, 订单ID: {}", threadName, orderId);
        
        // 模拟订单处理：库存扣减、支付、发货等
        try {
            Thread.sleep(500);
            log.info("【订单处理完成】订单ID: {}", orderId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
