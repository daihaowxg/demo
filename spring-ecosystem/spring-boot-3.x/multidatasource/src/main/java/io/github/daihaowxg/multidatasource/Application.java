package io.github.daihaowxg.multidatasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 多数据源示例应用
 * <p>
 * 演示如何在 Spring Boot 中配置和使用多个数据源。
 * <p>
 * <b>核心特性:</b>
 * <ul>
 * <li>配置多个独立的数据源</li>
 * <li>为每个数据源创建独立的 JdbcTemplate</li>
 * <li>为每个数据源配置独立的事务管理器</li>
 * <li>演示跨数据源操作</li>
 * </ul>
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
