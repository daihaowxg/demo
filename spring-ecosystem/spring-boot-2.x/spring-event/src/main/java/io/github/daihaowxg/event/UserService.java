package io.github.daihaowxg.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 用户服务
 * <p>
 * 负责用户注册等核心业务逻辑，通过事件机制实现与其他业务模块的解耦
 *
 * @author wxg
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Spring 事件发布器
     * 用于发布应用事件，实现业务解耦
     */
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 用户注册
     * <p>
     * 执行用户注册的核心逻辑，并发布用户注册事件
     * 后续的邮件发送、积分赠送等操作由事件监听器异步处理
     *
     * @param username 用户名
     */
    public void register(String username) {
        log.info("执行用户注册逻辑: {}", username);

        // 执行注册核心逻辑（如：数据库操作、数据校验等）
        System.out.println(">> 用户 " + username + " 注册成功");

        // 发布用户注册事件，通知其他模块执行后续操作
        eventPublisher.publishEvent(new UserRegisterEvent(this, username));
    }
}
