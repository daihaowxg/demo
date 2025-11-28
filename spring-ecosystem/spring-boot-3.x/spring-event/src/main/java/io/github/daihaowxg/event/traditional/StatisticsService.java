package io.github.daihaowxg.event.traditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 统计服务（传统方式）
 *
 * @author wxg
 */
@Slf4j
@Service
public class StatisticsService {

    /**
     * 记录用户注册统计
     */
    public void recordRegistration(String username) {
        log.info("【统计服务】记录用户注册统计: {}", username);
        System.out.println(">> 记录用户注册统计: " + username);
    }
}
