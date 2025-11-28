package io.github.daihaowxg.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 用户注册事件
 * <p>
 * 当用户注册成功后发布此事件，通知其他组件执行后续操作
 * 例如：发送欢迎邮件、赠送积分、记录统计等
 *
 * @author wxg
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {
    
    /**
     * 注册的用户名
     */
    private final String username;

    /**
     * 构造用户注册事件
     *
     * @param source   事件源（通常是发布事件的对象）
     * @param username 注册的用户名
     */
    public UserRegisterEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
