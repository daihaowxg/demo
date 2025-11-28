package io.github.daihaowxg.event.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步配置
 * <p>
 * 启用 Spring 的异步方法执行功能
 * 使用 @Async 注解的方法将在独立的线程池中执行
 *
 * @author wxg
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 使用 Spring Boot 默认的异步配置
    // 如需自定义线程池，可以配置 TaskExecutor Bean
}
