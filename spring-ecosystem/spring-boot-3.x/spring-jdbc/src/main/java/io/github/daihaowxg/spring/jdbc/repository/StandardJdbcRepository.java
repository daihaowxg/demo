package io.github.daihaowxg.spring.jdbc.repository;

import io.github.daihaowxg.spring.jdbc.entity.User;
import org.springframework.stereotype.Repository;

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
 * 标准 JDBC 用户仓库实现。
 * <p>
 * 此类演示了如何使用标准的 Java JDBC API ({@link java.sql}) 进行数据库操作。
 * 它展示了在没有 Spring JDBC 等框架帮助下，我们必须手动处理的繁琐工作，例如：
 * <ul>
 *   <li>获取连接 (Connection)</li>
 *   <li>创建语句 (PreparedStatement)</li>
 *   <li>设置参数</li>
 *   <li>执行查询/更新</li>
 *   <li>处理结果集 (ResultSet)</li>
 *   <li>此过程中极其重要的：手动关闭资源 (try-with-resources)</li>
 *   <li>处理受检异常 (SQLException)</li>
 * </ul>
 */
@Repository
public class StandardJdbcRepository {

    private final DataSource dataSource;

    public StandardJdbcRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 保存用户。
     * <p>
     * 演示了插入数据并获取自动生成的主键。
     * 注意这里大量的样板代码：
     * 1. try-with-resources 确保 Connection 和 PreparedStatement 被关闭。
     * 2. 定义 SQL。
     * 3. 设置参数 (index 从 1 开始)。
     * 4. 显式请求 RETURN_GENERATED_KEYS。
     * 5. 手动遍历 ResultSet 获取主键。
     * 6. 异常处理 (SQLException 是受检异常，通常需要包装为运行时异常)。
     */
    public User save(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        // 1. 获取连接 (Connection) 和 预编译语句 (PreparedStatement)
        // 使用 try-with-resources 自动关闭资源，这是 JDBC 最佳实践
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // 2. 设置参数
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            
            // 3. 执行更新
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // 4. 获取生成的主键
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return user;
        } catch (SQLException e) {
            // 5. 异常转换：将 SQL 异常转换为运行时异常
            throw new RuntimeException("Error saving user", e);
        }
    }

    /**
     * 根据 ID 查询用户。
     * <p>
     * 演示了基本的 SELECT 查询。
     * 需要手动从 ResultSet 中提取每一列的数据并组装成对象。
     */
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // 手动判断是否有结果
                if (rs.next()) {
                    // 手动映射结果集到对象
                    return Optional.of(new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user", e);
        }
        return Optional.empty();
    }

    /**
     * 查询所有用户。
     * <p>
     * 演示了查询列表。需要手动循环 ResultSet。
     */
    public List<User> findAll() {
        String sql = "SELECT id, name, email FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // 循环遍历结果集
            while (rs.next()) {
                users.add(new User(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }
        return users;
    }

    /**
     * 更新用户。
     */
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setLong(3, user.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    /**
     * 根据 ID 删除用户。
     */
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, id);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }
}
