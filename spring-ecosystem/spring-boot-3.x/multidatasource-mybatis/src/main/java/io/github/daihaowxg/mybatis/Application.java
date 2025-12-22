package io.github.daihaowxg.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatis 多数据源示例应用
 * 
 * <p>演示如何在 Spring Boot 中配置和使用 MyBatis 多数据源</p>
 * 
 * <p><b>核心配置：</b></p>
 * <ul>
 *   <li>PrimaryMyBatisConfig - 主数据源配置</li>
 *   <li>SecondaryMyBatisConfig - 第二个数据源配置</li>
 * </ul>
 * 
 * <p><b>使用方式：</b></p>
 * <ul>
 *   <li>直接注入 Mapper 接口使用</li>
 *   <li>使用 @Transactional 时需指定 transactionManager</li>
 * </ul>
 *
 * @author daihaowxg
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        
        System.out.println("\n========================================");
        System.out.println("MyBatis 多数据源示例应用已启动");
        System.out.println("========================================");
        System.out.println("配置说明：");
        System.out.println("  - 主数据源: primaryDataSource");
        System.out.println("  - 第二个数据源: secondaryDataSource");
        System.out.println("\n运行测试：");
        System.out.println("  mvn test");
        System.out.println("========================================\n");
    }
}
