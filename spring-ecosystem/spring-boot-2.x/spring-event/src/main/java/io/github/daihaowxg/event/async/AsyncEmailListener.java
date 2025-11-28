package io.github.daihaowxg.event.async;

import io.github.daihaowxg.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步邮件监听器
 * <p>
 * 使用 @Async 注解实现异步处理，不阻塞主流程
 * 适用于耗时操作，如发送邮件、调用第三方 API 等
 * <p>
 * 优点：
 * 1. 不阻塞主流程，提升响应速度
 * 2. 即使邮件发送失败，也不影响用户注册
 * 3. 可以并发处理多个事件
 *
 * @author wxg
 */
@Slf4j
@Component
public class AsyncEmailListener {

    /**
     * 异步发送欢迎邮件
     * <p>
     * 使用 @Async 注解，该方法会在独立的线程中执行
     * 不会阻塞用户注册的主流程
     *
     * @param event 用户注册事件
     */
    @Async
    @EventListener
    public void sendWelcomeEmailAsync(UserRegisterEvent event) {
        String threadName = Thread.currentThread().getName();
        log.info("【异步邮件服务】在线程 [{}] 中发送欢迎邮件给: {}", threadName, event.getUsername());
        
        // 模拟耗时操作（如调用邮件服务 API）
        try {
            Thread.sleep(2000); // 模拟发送邮件耗时 2 秒
            System.out.println(">> [异步] 欢迎邮件发送成功: " + event.getUsername() + " (线程: " + threadName + ")");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("邮件发送被中断", e);
        }
    }
}
