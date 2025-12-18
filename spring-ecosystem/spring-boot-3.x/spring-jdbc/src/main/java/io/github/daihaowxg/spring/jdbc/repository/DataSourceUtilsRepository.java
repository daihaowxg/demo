package io.github.daihaowxg.spring.jdbc.repository;

import io.github.daihaowxg.spring.jdbc.entity.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DataSourceUtils 使用示例。
 * <p>
 * {@link DataSourceUtils} 是 Spring 提供的工具类，用于在事务环境中正确获取和释放数据库连接。
 * 它解决了以下关键问题：
 * <ul>
 * <li><b>事务感知</b>：在事务中获取的连接会参与到当前事务，确保事务的一致性</li>
 * <li><b>连接复用</b>：同一个事务中多次调用 getConnection 会返回同一个连接</li>
 * <li><b>正确释放</b>：使用 releaseConnection 而不是 close，避免在事务中提前关闭连接</li>
 * </ul>
 * <p>
 * <b>核心原则：</b>
 * 
 * <pre>
 * // 获取连接
 * Connection con = DataSourceUtils.getConnection(dataSource);
 * try {
 *     // 使用连接进行数据库操作
 * } finally {
 *     // 释放连接（注意：不是 close！）
 *     DataSourceUtils.releaseConnection(con, dataSource);
 * }
 * </pre>
 * <p>
 * <b>对比 {@code dataSource.getConnection()}：</b>
 * <ul>
 * <li>{@code dataSource.getConnection()} - 总是创建新连接，不感知 Spring 事务</li>
 * <li>{@code DataSourceUtils.getConnection()} - 事务感知，在事务中复用连接</li>
 * </ul>
 */
@Repository
public class DataSourceUtilsRepository {

    @NonNull
    private final DataSource dataSource;

    public DataSourceUtilsRepository(@NonNull DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 示例 1：基本用法 - 单个查询操作。
     * <p>
     * 演示如何使用 DataSourceUtils 获取连接并执行查询。
     */
    public Optional<User> findById(Long id) {
        // 使用 DataSourceUtils 获取连接（事务感知）
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            String sql = "SELECT id, name, email FROM users WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new User(
                                rs.getLong("id"),
                                rs.getString("name"),
                                rs.getString("email")));
                    }
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by id: " + id, e);
        } finally {
            // 重要：使用 releaseConnection 而不是 con.close()
            // 如果在事务中，这不会真正关闭连接，而是将其返回给事务管理器
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * 示例 2：在事务中使用 - 多个操作共享同一连接。
     * <p>
     * 在 @Transactional 方法中，多次调用 DataSourceUtils.getConnection 会返回同一个连接。
     * 这确保了所有操作在同一个事务中执行。
     * <p>
     * <b>重要说明：</b>在事务中，{@code DataSourceUtils.releaseConnection()} 不会真正关闭连接！
     * <ul>
     * <li>在事务中：releaseConnection 只是减少引用计数，连接仍然绑定在事务上下文中</li>
     * <li>非事务中：releaseConnection 会真正关闭连接并归还给连接池</li>
     * </ul>
     */
    @Transactional
    public void transferUserData(Long fromId, Long toId) {
        System.out.println("\n========== 开始演示事务中的连接管理 ==========");

        // 第一次获取连接
        Connection con1 = DataSourceUtils.getConnection(dataSource);
        boolean isTx1 = DataSourceUtils.isConnectionTransactional(con1, dataSource);
        System.out.println("1️⃣ 第一次获取连接:");
        System.out.println("   - 连接对象: " + con1);
        System.out.println("   - 是否在事务中: " + isTx1);

        try {
            // 查询源用户
            User fromUser = findUserWithConnection(con1, fromId);
            System.out.println("   - 查询结果: " + fromUser);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("   - 调用 releaseConnection(con1) - 注意：在事务中不会真正关闭！");
            DataSourceUtils.releaseConnection(con1, dataSource);
            System.out.println("   - releaseConnection 完成");
        }

        System.out.println("\n2️⃣ 第二次获取连接（在第一次 release 之后）:");
        // 第二次获取连接 - 在同一个事务中，这会返回相同的连接对象
        // 即使我们已经调用了 releaseConnection(con1)，连接仍然在事务中可用
        Connection con2 = DataSourceUtils.getConnection(dataSource);
        boolean isTx2 = DataSourceUtils.isConnectionTransactional(con2, dataSource);
        System.out.println("   - 连接对象: " + con2);
        System.out.println("   - 是否在事务中: " + isTx2);
        System.out.println("   - con1 == con2? " + (con1 == con2) + " ✅ 同一个连接对象！");

        try {
            // 验证连接仍然可用（没有被关闭）
            System.out.println("   - 验证连接是否可用: " + !con2.isClosed());

            // 更新目标用户
            updateUserEmailWithConnection(con2, toId, "transferred@example.com");
            System.out.println("   - 更新操作成功");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("   - 调用 releaseConnection(con2)");
            DataSourceUtils.releaseConnection(con2, dataSource);
        }

        System.out.println("\n3️⃣ 第三次获取连接（验证仍然可以获取）:");
        Connection con3 = DataSourceUtils.getConnection(dataSource);
        System.out.println("   - 连接对象: " + con3);
        System.out.println("   - con1 == con3? " + (con1 == con3) + " ✅ 还是同一个连接！");
        try {
            System.out.println("   - 连接仍然可用: " + !con3.isClosed());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(con3, dataSource);
        }

        System.out.println("\n========== 事务方法结束，Spring 会自动提交并关闭连接 ==========\n");
        // 当事务方法结束时，Spring 事务管理器会：
        // 1. 提交事务（如果没有异常）
        // 2. 真正关闭连接并归还给连接池
    }

    /**
     * 示例 3：演示事务回滚。
     * <p>
     * 当方法抛出异常时，Spring 会自动回滚事务，所有操作都不会生效。
     */
    @Transactional
    public void saveUserWithRollback(User user) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            // 插入用户
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getLong(1));
                        System.out.println("Inserted user with id: " + user.getId());
                    }
                }
            }

            // 模拟业务异常 - 这会导致事务回滚
            if (user.getName().contains("error")) {
                throw new RuntimeException("Business error - transaction will rollback");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user", e);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * 示例 4：批量操作。
     * <p>
     * 在事务中执行批量操作，确保要么全部成功，要么全部失败。
     */
    @Transactional
    public void batchInsert(List<User> users) {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (User user : users) {
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getEmail());
                    ps.addBatch();
                }

                int[] results = ps.executeBatch();
                System.out.println("Batch insert completed: " + results.length + " rows affected");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to batch insert users", e);
        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * 示例 5：非事务环境中使用。
     * <p>
     * 在没有 @Transactional 的方法中，DataSourceUtils 会从连接池获取新连接，
     * 并在 releaseConnection 时真正关闭它。
     */
    public List<User> findAll() {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            String sql = "SELECT id, name, email FROM users";
            List<User> users = new ArrayList<>();

            try (PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email")));
                }
            }

            return users;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        } finally {
            // 在非事务环境中，这会真正关闭连接并归还给连接池
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * 示例 6：检查连接是否在事务中。
     * <p>
     * 演示如何使用 DataSourceUtils 的其他实用方法。
     */
    @Transactional
    public void demonstrateConnectionInfo() {
        Connection con = DataSourceUtils.getConnection(dataSource);
        try {
            // 检查连接是否是事务性的
            boolean isTransactional = DataSourceUtils.isConnectionTransactional(con, dataSource);
            System.out.println("Is connection transactional? " + isTransactional); // true

            // 在事务方法中，这应该返回 true
            System.out.println("Connection is from transaction: " + isTransactional);

        } finally {
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    /**
     * 示例 7：对比事务和非事务环境下的 releaseConnection 行为。
     * <p>
     * 这个方法演示了 {@code DataSourceUtils.releaseConnection()} 在不同环境下的行为差异：
     * <ul>
     * <li><b>非事务环境</b>：releaseConnection 会真正关闭连接</li>
     * <li><b>事务环境</b>：releaseConnection 不会关闭连接，连接仍然可用</li>
     * </ul>
     */
    public void demonstrateReleaseConnectionBehavior() {
        System.out.println("\n========== 演示非事务环境下的连接释放 ==========");

        Connection con1 = DataSourceUtils.getConnection(dataSource);
        boolean isTx1 = DataSourceUtils.isConnectionTransactional(con1, dataSource);
        System.out.println("1️⃣ 非事务环境 - 第一次获取连接:");
        System.out.println("   - 连接对象: " + con1);
        System.out.println("   - 是否在事务中: " + isTx1 + " (false)");

        try {
            System.out.println("   - 连接是否关闭: " + con1.isClosed() + " (false - 连接打开)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("   - 调用 releaseConnection(con1)");
        DataSourceUtils.releaseConnection(con1, dataSource);
        System.out.println("   - releaseConnection 完成");

        try {
            // 在非事务环境中，连接应该被关闭了
            boolean isClosed = con1.isClosed();
            System.out.println("   - 连接是否关闭: " + isClosed + " (true - 连接已关闭！)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("\n2️⃣ 非事务环境 - 第二次获取连接:");
        Connection con2 = DataSourceUtils.getConnection(dataSource);
        System.out.println("   - 连接对象: " + con2);
        System.out.println("   - con1 == con2? " + (con1 == con2) + " (false - 不同的连接！)");

        try {
            System.out.println("   - con2 是否关闭: " + con2.isClosed() + " (false - 新连接)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(con2, dataSource);
        }

        System.out.println("\n========== 对比总结 ==========");
        System.out.println("✅ 事务环境: releaseConnection 不关闭连接，可以多次获取同一连接");
        System.out.println("❌ 非事务环境: releaseConnection 真正关闭连接，每次获取新连接\n");
    }

    // ========== 辅助方法 ==========

    private User findUserWithConnection(Connection con, Long id) throws SQLException {
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"));
                }
            }
        }
        throw new RuntimeException("User not found: " + id);
    }

    private void updateUserEmailWithConnection(Connection con, Long id, String email) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setLong(2, id);
            ps.executeUpdate();
        }
    }
}
