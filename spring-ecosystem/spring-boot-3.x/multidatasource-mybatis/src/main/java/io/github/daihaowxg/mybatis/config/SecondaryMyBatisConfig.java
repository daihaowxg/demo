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
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 第二个数据源 MyBatis 配置
 * 
 * <p>
 * 配置第二个数据源的 MyBatis 相关 Bean：
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
 * <li>使用 @MapperScan 指定 Mapper 接口所在的包（与主数据源不同）</li>
 * <li>通过 sqlSessionFactoryRef 关联到对应的 SqlSessionFactory</li>
 * <li>不使用 @Primary，需要通过 @Qualifier 明确指定</li>
 * </ul>
 *
 * @author daihaowxg
 */
@Configuration
@MapperScan(basePackages = "io.github.daihaowxg.mybatis.mapper.secondary", sqlSessionFactoryRef = "secondarySqlSessionFactory")
public class SecondaryMyBatisConfig {

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
     * 第二个数据源的 SQL 初始化
     * 
     * <p>
     * Spring Boot 的 spring.sql.init 只会在主数据源上执行，
     * </p>
     * <p>
     * 所以需要手动为第二个数据源初始化表结构
     * </p>
     */
    @Bean(name = "secondaryDataSourceInitializer")
    public DataSourceInitializer secondaryDataSourceInitializer(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema-secondary.sql"));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    /**
     * 第二个数据源的 SqlSessionFactory
     */
    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(
            @Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
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
     * 第二个数据源的 SqlSessionTemplate
     */
    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(
            @Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
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
