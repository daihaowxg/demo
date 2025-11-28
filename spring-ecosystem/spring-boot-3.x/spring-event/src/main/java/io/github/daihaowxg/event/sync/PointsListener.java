package io.github.daihaowxg.event.sync;

import io.github.daihaowxg.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 积分监听器
 * <p>
 * 监听用户注册事件，负责赠送新用户积分
 * 使用 @Order(2) 设置执行顺序为第二位
 *
 * @author wxg
 */
@Slf4j
@Component
public class PointsListener {

    /**
     * 赠送新用户积分
     * <p>
     * 当用户注册成功后，自动赠送新人积分
     *
     * @param event 用户注册事件
     */
    @Order(2)
    @EventListener
    public void grantNewUserPoints(UserRegisterEvent event) {
        log.info("【积分服务】赠送新人积分给: {}", event.getUsername());
        // 实际项目中这里会调用积分服务进行积分赠送
        System.out.println(">> 赠送 100 积分给新用户: " + event.getUsername());
    }
}
