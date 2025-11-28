package io.github.daihaowxg.event.sync;

import io.github.daihaowxg.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 统计监听器
 * <p>
 * 监听用户注册事件，负责记录用户注册统计数据
 * 使用 @Order(3) 设置执行顺序为第三位
 *
 * @author wxg
 */
@Slf4j
@Component
public class StatisticsListener {

    /**
     * 记录用户注册统计
     * <p>
     * 当用户注册成功后，自动记录统计数据
     *
     * @param event 用户注册事件
     */
    @Order(3)
    @EventListener
    public void recordRegistration(UserRegisterEvent event) {
        log.info("【统计服务】记录用户注册统计: {}", event.getUsername());
        // 实际项目中这里会调用统计服务记录数据
        System.out.println(">> 记录用户注册统计: " + event.getUsername());
    }
}
