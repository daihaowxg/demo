package com.crhms.cdmp.ds.config;

import com.crhms.cdmp.ds.manager.DynamicRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxg
 * @since 2025/2/5
 */
@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();

        // 定义多个数据源
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource master = createDataSource("jdbc:mysql://localhost:3306/master");
        DataSource slave = createDataSource("jdbc:mysql://localhost:3306/slave");

        targetDataSources.put("master", master);
        targetDataSources.put("slave", slave);

        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(master);
        dynamicDataSource.afterPropertiesSet();

        return dynamicDataSource;
    }

    private DataSource createDataSource(String url) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

}
