package io.github.daihaowxg.multidatasource.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 多数据源配置示例
 * <p>
 * 演示如何配置多个数据源和对应的 JdbcTemplate。
 * <p>
 * <b>核心要点:</b>
 * <ul>
 * <li>每个数据源需要独立的 {@link DataSource} Bean</li>
 * <li>每个数据源需要独立的 {@link JdbcTemplate} Bean</li>
 * <li>每个数据源需要独立的 {@link PlatformTransactionManager} Bean</li>
 * <li>使用 {@link Primary} 注解标记主数据源</li>
 * <li>使用 {@link Qualifier} 注解区分不同的数据源</li>
 * </ul>
 * <p>
 * <b>配置文件示例 (application.yml):</b>
 *
 * <pre>
 * spring:
 *   datasource:
 *     primary:
 *       jdbc-url: jdbc:h2:mem:primarydb
 *       username: sa
 *       password:
 *       driver-class-name: org.h2.Driver
 *     secondary:
 *       jdbc-url: jdbc:h2:mem:secondarydb
 *       username: sa
 *       password:
 *       driver-class-name: org.h2.Driver
 * </pre>
 * 
 * <p>
 * <b>注意：</b>此配置类已禁用，因为项目使用 MyBatis 多数据源配置。
 * </p>
 */
@Configuration // ← 已禁用，避免与 MyBatis 配置冲突
public class MultiDataSourceConfig {

    /**
     * 主数据源配置
     * <p>
     * 使用 @Primary 标记为默认数据源,当没有指定 @Qualifier 时使用此数据源
     */
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 主数据源的 JdbcTemplate
     * <p>
     * 使用 @Primary 标记为默认 JdbcTemplate
     */
    @Primary
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 主数据源的事务管理器
     * <p>
     * 使用 @Primary 标记为默认事务管理器
     */
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // ==================== 第二个数据源配置 ====================

    /**
     * 第二个数据源配置
     */
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 第二个数据源的 JdbcTemplate
     */
    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 第二个数据源的事务管理器
     */
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
