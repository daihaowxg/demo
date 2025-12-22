package io.github.daihaowxg.mybatis.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 主数据源 MyBatis 配置
 * 
 * <p>
 * 配置主数据源的 MyBatis 相关 Bean：
 * </p>
 * <ul>
 * <li>DataSource - 数据源</li>
 * <li>SqlSessionFactory - MyBatis 会话工厂</li>
 * <li>SqlSessionTemplate - MyBatis 会话模板</li>
 * <li>TransactionManager - 事务管理器</li>
 * </ul>
 * 
 * <p>
 * <b>关键点：</b>
 * </p>
 * <ul>
 * <li>使用 @MapperScan 指定 Mapper 接口所在的包</li>
 * <li>通过 sqlSessionFactoryRef 关联到对应的 SqlSessionFactory</li>
 * <li>使用 @Primary 标记为主数据源</li>
 * </ul>
 *
 * @author daihaowxg
 */
@Configuration
@MapperScan(basePackages = "io.github.daihaowxg.mybatis.mapper.primary", sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryMyBatisConfig {

    /**
     * 主数据源配置
     * 
     * <p>
     * 使用 @Primary 标记为默认数据源
     * </p>
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
     * 主数据源的 SqlSessionFactory
     * 
     * <p>
     * MyBatis 的核心配置，用于创建 SqlSession
     * </p>
     */
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(
            @Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        // 可以设置 MyBatis 配置
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setMapUnderscoreToCamelCase(true); // 下划线转驼峰
        configuration.setLogImpl(org.apache.ibatis.logging.slf4j.Slf4jImpl.class); // 日志实现
        bean.setConfiguration(configuration);

        return bean.getObject();
    }

    /**
     * 主数据源的 SqlSessionTemplate
     * 
     * <p>
     * 线程安全的 SqlSession，可以直接注入使用
     * </p>
     */
    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(
            @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 主数据源的事务管理器
     */
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
