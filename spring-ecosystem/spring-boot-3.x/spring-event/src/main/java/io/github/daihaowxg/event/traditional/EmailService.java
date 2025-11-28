package io.github.daihaowxg.event.traditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 邮件服务（传统方式）
 *
 * @author wxg
 */
@Slf4j
@Service
public class EmailService {

    /**
     * 发送欢迎邮件
     */
    public void sendWelcomeEmail(String username) {
        log.info("【邮件服务】发送欢迎邮件给: {}", username);
        System.out.println(">> 发送欢迎邮件给: " + username);
    }
}
