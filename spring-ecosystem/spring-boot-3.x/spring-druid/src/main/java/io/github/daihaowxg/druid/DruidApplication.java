package io.github.daihaowxg.druid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Druid 示例应用启动类
 * 
 * <p>本示例演示了 Alibaba Druid 连接池的使用，包括：</p>
 * <ul>
 *   <li>Druid 连接池的配置</li>
 *   <li>SQL 监控统计</li>
 *   <li>Web 监控页面</li>
 *   <li>SQL 防火墙</li>
 *   <li>慢 SQL 记录</li>
 * </ul>
 * 
 * <p>启动后可以访问以下地址：</p>
 * <ul>
 *   <li>监控页面: <a href="http://localhost:8080/druid">http://localhost:8080/druid</a> (用户名: admin, 密码: admin123)</li>
 *   <li>API 接口: <a href="http://localhost:8080/api/users">http://localhost:8080/api/users</a></li>
 *   <li>连接池统计: <a href="http://localhost:8080/api/users/druid/stats">http://localhost:8080/api/users/druid/stats</a></li>
 * </ul>
 *
 * @author daihaowxg
 */
@SpringBootApplication
public class DruidApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class, args);
        System.out.println("\n" +
                "==============================================\n" +
                "  Druid 示例应用启动成功！\n" +
                "==============================================\n" +
                "  监控页面: http://localhost:8080/druid\n" +
                "  用户名: admin\n" +
                "  密码: admin123\n" +
                "----------------------------------------------\n" +
                "  API 接口:\n" +
                "  - 获取所有用户: GET http://localhost:8080/api/users\n" +
                "  - 获取单个用户: GET http://localhost:8080/api/users/{id}\n" +
                "  - 搜索用户: GET http://localhost:8080/api/users/search?username=张\n" +
                "  - 连接池统计: GET http://localhost:8080/api/users/druid/stats\n" +
                "==============================================\n");
    }
}
