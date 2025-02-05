package com.crhms.cdmp.ds.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wxg
 * @since 2025/2/5
 */
@Configuration
@MapperScan("com.crhms.cdmp.*.mapper")
public class MyBatisConfig {
}
