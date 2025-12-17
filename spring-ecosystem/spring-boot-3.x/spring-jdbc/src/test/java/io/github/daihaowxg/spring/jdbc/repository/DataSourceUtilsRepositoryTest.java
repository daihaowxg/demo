package io.github.daihaowxg.spring.jdbc.repository;

import io.github.daihaowxg.spring.jdbc.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DataSourceUtils 使用示例测试。
 * <p>
 * 这些测试演示了 {@link DataSourceUtilsRepository} 中各种场景的实际运行效果。
 */
@SpringBootTest
@Sql(scripts = "/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class DataSourceUtilsRepositoryTest {

    @Autowired
    private DataSourceUtilsRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // 清理数据
        jdbcTemplate.execute("DELETE FROM users");
    }

    /**
     * 测试示例 1：基本查询操作。
     */
    @Test
    void testFindById() {
        // 准备数据
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            1L, "Alice", "alice@example.com");

        // 执行查询
        Optional<User> user = repository.findById(1L);

        // 验证结果
        assertTrue(user.isPresent());
        assertEquals("Alice", user.get().getName());
        assertEquals("alice@example.com", user.get().getEmail());
    }

    /**
     * 测试示例 2：事务中的连接复用。
     * <p>
     * 这个测试演示了在同一个事务中，多次调用 DataSourceUtils.getConnection
     * 会返回同一个连接对象。
     */
    @Test
    void testTransferUserData() {
        // 准备数据
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            1L, "Alice", "alice@example.com");
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            2L, "Bob", "bob@example.com");

        // 执行转移操作（在事务中）
        repository.transferUserData(1L, 2L);

        // 验证结果 - Bob 的邮箱应该被更新
        String email = jdbcTemplate.queryForObject(
            "SELECT email FROM users WHERE id = ?",
            String.class,
            2L
        );
        assertEquals("transferred@example.com", email);
    }

    /**
     * 测试示例 3：事务回滚。
     * <p>
     * 当方法抛出异常时，事务应该回滚，数据库不应该有任何变化。
     */
    @Test
    void testSaveUserWithRollback() {
        // 创建一个会触发异常的用户
        User user = new User(null, "error-user", "error@example.com");

        // 执行保存操作 - 应该抛出异常
        assertThrows(RuntimeException.class, () -> {
            repository.saveUserWithRollback(user);
        });

        // 验证结果 - 由于事务回滚，用户不应该被保存
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE name = ?",
            Integer.class,
            "error-user"
        );
        assertEquals(0, count);
    }

    /**
     * 测试示例 3：正常保存（不触发回滚）。
     */
    @Test
    void testSaveUserWithoutRollback() {
        // 创建一个正常的用户
        User user = new User(null, "Charlie", "charlie@example.com");

        // 执行保存操作 - 应该成功
        repository.saveUserWithRollback(user);

        // 验证结果 - 用户应该被保存
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE name = ?",
            Integer.class,
            "Charlie"
        );
        assertEquals(1, count);
    }

    /**
     * 测试示例 4：批量插入。
     */
    @Test
    void testBatchInsert() {
        // 准备批量数据
        List<User> users = Arrays.asList(
            new User(null, "User1", "user1@example.com"),
            new User(null, "User2", "user2@example.com"),
            new User(null, "User3", "user3@example.com")
        );

        // 执行批量插入
        repository.batchInsert(users);

        // 验证结果
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users",
            Integer.class
        );
        assertEquals(3, count);
    }

    /**
     * 测试示例 5：查询所有用户（非事务环境）。
     */
    @Test
    void testFindAll() {
        // 准备数据
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            1L, "Alice", "alice@example.com");
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            2L, "Bob", "bob@example.com");

        // 执行查询
        List<User> users = repository.findAll();

        // 验证结果
        assertEquals(2, users.size());
    }

    /**
     * 测试示例 6：演示连接信息。
     * <p>
     * 这个测试只是演示方法的执行，实际的连接信息会打印到控制台。
     */
    @Test
    void testDemonstrateConnectionInfo() {
        // 执行演示方法
        repository.demonstrateConnectionInfo();
        
        // 这个测试主要是为了演示，查看控制台输出
        // 应该会看到 "Is connection transactional? true"
    }

    /**
     * 测试示例 7：演示 releaseConnection 在不同环境下的行为。
     * <p>
     * 这个测试展示了 releaseConnection 在事务和非事务环境中的不同行为。
     */
    @Test
    void testDemonstrateReleaseConnectionBehavior() {
        // 执行演示方法（非事务环境）
        repository.demonstrateReleaseConnectionBehavior();
        
        // 这个测试主要是为了演示，查看控制台输出
        // 应该会看到非事务环境下连接被真正关闭的日志
    }

    /**
     * 综合测试：演示事务的 ACID 特性。
     * <p>
     * 测试批量插入在事务中的行为：batchInsert 方法本身是 @Transactional 的，
     * 所以它会在自己的事务中完成，要么全部成功，要么全部失败。
     */
    @Test
    void testTransactionACID() {
        // 准备初始数据
        jdbcTemplate.update("INSERT INTO users (id, name, email) VALUES (?, ?, ?)",
            1L, "Alice", "alice@example.com");

        // 创建批量数据（不包含会触发异常的数据）
        List<User> users = Arrays.asList(
            new User(null, "User1", "user1@example.com"),
            new User(null, "User2", "user2@example.com"),
            new User(null, "User3", "user3@example.com")
        );

        // 执行批量插入 - 应该成功
        // batchInsert 是 @Transactional 的，会在自己的事务中完成
        repository.batchInsert(users);

        // 验证结果 - 应该有 Alice + 3个批量插入的用户 = 4条记录
        Integer count = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users",
            Integer.class
        );
        assertEquals(4, count); // Alice + User1 + User2 + User3
        
        // 验证批量插入的数据都存在
        Integer batchCount = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE name LIKE 'User%'",
            Integer.class
        );
        assertEquals(3, batchCount);
    }
}
