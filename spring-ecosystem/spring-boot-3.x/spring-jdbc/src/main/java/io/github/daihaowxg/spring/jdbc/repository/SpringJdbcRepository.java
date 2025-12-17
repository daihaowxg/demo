package io.github.daihaowxg.spring.jdbc.repository;

import io.github.daihaowxg.spring.jdbc.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Spring JDBC 用户仓库实现。
 * <p>
 * 此类演示了如何使用 Spring 的 {@link JdbcTemplate} 进行数据库操作。
 * 相比 {@link StandardJdbcRepository}，请注意代码是多么的简洁：
 * <ul>
 *   <li>不需要手动管理 Connection/Statement 的打开和关闭（资源管理）</li>
 *   <li>不需要手动处理 SQLException（Spring 自动转换为非受检的 DataAccessException）</li>
 *   <li>RowMapper 复用，简化了 ResultSet 到对象的映射</li>
 * </ul>
 */
@Repository
public class SpringJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public SpringJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper 定义了如何将 ResultSet 的一行映射为 User 对象
    // 这可以被多个查询方法复用，避免了代码重复
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getString("email")
    );

    /**
     * 保存用户。
     * <p>
     * 即使是复杂的获取生成主键的操作，Spring 也提供了 KeyHolder 辅助类来简化。
     */
    public User save(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //即使需要使用回调来创建 Statement，依然不需要关心资源的关闭
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }
        return user;
    }

    /**
     * 根据 ID 查询用户。
     * <p>
     * 简单的查询只需要一行代码，结合 RowMapper 自动映射结果。
     */
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, name, email FROM users WHERE id = ?";
        // query 方法返回一个列表，我们取第一个即可
        List<User> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.stream().findFirst();
    }

    /**
     * 查询所有用户。
     */
    public List<User> findAll() {
        String sql = "SELECT id, name, email FROM users";
        // 直接返回映射后的对象列表
        return jdbcTemplate.query(sql, userRowMapper);
    }

    /**
     * 更新用户。
     * <p>
     * update 方法用于执行 INSERT, UPDATE, DELETE 语句。
     * 参数只需按顺序传入即可。
     */
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getId());
    }

    /**
     * 根据 ID 删除用户。
     */
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
