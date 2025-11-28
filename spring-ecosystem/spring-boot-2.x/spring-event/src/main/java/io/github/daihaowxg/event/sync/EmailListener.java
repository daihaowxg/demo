package io.github.daihaowxg.event.sync;

import io.github.daihaowxg.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 邮件监听器
 * <p>
 * 监听用户注册事件，负责发送欢迎邮件
 * 使用 @Order(1) 设置为最高优先级，优先执行
 *
 * @author wxg
 */
@Slf4j
@Component
public class EmailListener {

    /**
     * 发送欢迎邮件
     * <p>
     * 当用户注册成功后，自动发送欢迎邮件
     *
     * @param event 用户注册事件
     */
    @Order(1)
    @EventListener
    public void sendWelcomeEmail(UserRegisterEvent event) {
        log.info("【邮件服务】发送欢迎邮件给: {}", event.getUsername());
        // 实际项目中这里会调用邮件服务发送邮件
        System.out.println(">> 发送欢迎邮件给: " + event.getUsername());
    }
}
