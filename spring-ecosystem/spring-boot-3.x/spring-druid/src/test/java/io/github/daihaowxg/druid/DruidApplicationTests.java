package io.github.daihaowxg.druid;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.daihaowxg.druid.entity.User;
import io.github.daihaowxg.druid.repository.UserRepository;
import io.github.daihaowxg.druid.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Druid 数据源测试类
 * 演示 Druid 连接池的各种功能
 *
 * @author daihaowxg
 */
@SpringBootTest
class DruidApplicationTests {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    /**
     * 测试数据源是否为 DruidDataSource
     */
    @Test
    void testDataSourceType() {
        System.out.println("数据源类型: " + dataSource.getClass().getName());
        assertTrue(dataSource instanceof DruidDataSource, "数据源应该是 DruidDataSource 类型");
    }
    
    /**
     * 测试 Druid 连接池配置
     */
    @Test
    void testDruidConfiguration() {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            System.out.println("\n=== Druid 连接池配置信息 ===");
            System.out.println("初始化连接数: " + druidDataSource.getInitialSize());
            System.out.println("最小空闲连接数: " + druidDataSource.getMinIdle());
            System.out.println("最大活跃连接数: " + druidDataSource.getMaxActive());
            System.out.println("最大等待时间: " + druidDataSource.getMaxWait() + "ms");
            System.out.println("验证查询SQL: " + druidDataSource.getValidationQuery());
            System.out.println("是否开启 testWhileIdle: " + druidDataSource.isTestWhileIdle());
            
            // 验证配置
            assertEquals(5, druidDataSource.getInitialSize());
            assertEquals(5, druidDataSource.getMinIdle());
            assertEquals(20, druidDataSource.getMaxActive());
            assertEquals(60000, druidDataSource.getMaxWait());
        }
    }
    
    /**
     * 测试查询所有用户
     */
    @Test
    void testFindAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("\n=== 查询所有用户 ===");
        users.forEach(user -> System.out.println(user));
        
        assertNotNull(users);
        assertTrue(users.size() >= 5, "应该至少有5个用户");
    }
    
    /**
     * 测试根据ID查询用户
     */
    @Test
    void testFindUserById() {
        User user = userRepository.findById(1L);
        System.out.println("\n=== 根据ID查询用户 ===");
        System.out.println(user);
        
        assertNotNull(user);
        assertEquals("张三", user.getUsername());
    }
    
    /**
     * 测试根据用户名搜索
     */
    @Test
    void testSearchByUsername() {
        List<User> users = userRepository.findByUsername("张");
        System.out.println("\n=== 根据用户名搜索 ===");
        users.forEach(user -> System.out.println(user));
        
        assertNotNull(users);
        assertTrue(users.size() > 0);
    }
    
    /**
     * 测试插入用户
     */
    @Test
    void testInsertUser() {
        User newUser = new User();
        newUser.setUsername("测试用户");
        newUser.setEmail("test@example.com");
        newUser.setAge(20);
        
        int rows = userRepository.insert(newUser);
        System.out.println("\n=== 插入用户 ===");
        System.out.println("影响行数: " + rows);
        
        assertEquals(1, rows);
    }
    
    /**
     * 测试更新用户
     */
    @Test
    void testUpdateUser() {
        User user = userRepository.findById(1L);
        assertNotNull(user);
        
        user.setAge(26);
        int rows = userRepository.update(user);
        System.out.println("\n=== 更新用户 ===");
        System.out.println("影响行数: " + rows);
        
        assertEquals(1, rows);
        
        User updatedUser = userRepository.findById(1L);
        assertEquals(26, updatedUser.getAge());
    }
    
    /**
     * 测试批量插入
     */
    @Test
    void testBatchInsert() {
        List<User> users = Arrays.asList(
                createUser("批量用户1", "batch1@example.com", 21),
                createUser("批量用户2", "batch2@example.com", 22),
                createUser("批量用户3", "batch3@example.com", 23)
        );
        
        int[] rows = userRepository.batchInsert(users);
        System.out.println("\n=== 批量插入用户 ===");
        System.out.println("插入数量: " + rows.length);
        
        assertEquals(3, rows.length);
    }
    
    /**
     * 测试统计用户数量
     */
    @Test
    void testCountUsers() {
        long count = userRepository.count();
        System.out.println("\n=== 统计用户数量 ===");
        System.out.println("用户总数: " + count);
        
        assertTrue(count > 0);
    }
    
    /**
     * 测试事务回滚
     */
    @Test
    void testTransactionRollback() {
        long beforeCount = userRepository.count();
        
        try {
            User user = createUser("事务测试", "transaction@example.com", 25);
            userRepository.insert(user);
            
            // 模拟异常
            throw new RuntimeException("模拟异常，触发回滚");
        } catch (Exception e) {
            System.out.println("\n=== 事务回滚测试 ===");
            System.out.println("捕获异常: " + e.getMessage());
        }
        
        long afterCount = userRepository.count();
        // 注意：这里不会回滚，因为异常在测试方法中被捕获了
        // 真正的事务回滚需要在 Service 层的 @Transactional 方法中测试
        System.out.println("回滚前数量: " + beforeCount);
        System.out.println("回滚后数量: " + afterCount);
    }
    
    /**
     * 测试获取 Druid 统计信息
     */
    @Test
    void testDruidStatistics() {
        // 先执行一些数据库操作
        userRepository.findAll();
        userRepository.findById(1L);
        userRepository.count();
        
        String stats = userService.getDruidStatistics();
        System.out.println("\n" + stats);
        
        assertNotNull(stats);
        assertTrue(stats.contains("Druid 连接池统计信息"));
    }
    
    /**
     * 测试慢 SQL（模拟）
     * 注意：H2 数据库可能不支持 SLEEP 函数，这里仅作演示
     */
    @Test
    void testSlowSql() {
        System.out.println("\n=== 慢 SQL 测试 ===");
        System.out.println("执行一个复杂查询...");
        
        // 执行一个稍微复杂的查询
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            userRepository.findById(user.getId());
        });
        
        System.out.println("查询完成，可以在 Druid 监控页面查看 SQL 执行统计");
    }
    
    /**
     * 辅助方法：创建用户对象
     */
    private User createUser(String username, String email, Integer age) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAge(age);
        return user;
    }
}
