package io.github.daihaowxg.event.sync;

import io.github.daihaowxg.event.UserRegisterEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * VIP 用户监听器
 * <p>
 * 监听用户注册事件，使用条件监听只处理 VIP 用户
 * 演示了 Spring 事件机制的条件监听功能
 *
 * @author wxg
 */
@Slf4j
@Component
public class VipUserListener {

    /**
     * 处理 VIP 用户注册
     * <p>
     * 使用 SpEL 表达式实现条件监听，只处理用户名以 "vip_" 开头的用户
     * 当 VIP 用户注册时，赠送专属礼包
     *
     * @param event 用户注册事件
     */
    @EventListener(condition = "#event.username.startsWith('vip_')")
    public void handleVipUser(UserRegisterEvent event) {
        log.info("【VIP服务】检测到 VIP 用户注册: {}", event.getUsername());
        // 实际项目中这里会调用 VIP 服务赠送专属礼包
        System.out.println(">> VIP 用户注册，赠送专属礼包: " + event.getUsername());
    }
}
