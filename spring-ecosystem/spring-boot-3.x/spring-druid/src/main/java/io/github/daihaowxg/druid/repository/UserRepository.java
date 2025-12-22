package io.github.daihaowxg.druid.repository;

import io.github.daihaowxg.druid.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户数据访问层
 * 演示使用 JdbcTemplate 配合 Druid 连接池进行数据库操作
 *
 * @author daihaowxg
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    /**
     * User 实体的 RowMapper
     */
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setAge(rs.getInt("age"));
        user.setCreatedTime(rs.getTimestamp("created_time").toLocalDateTime());
        user.setUpdatedTime(rs.getTimestamp("updated_time").toLocalDateTime());
        return user;
    };
    
    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        log.debug("执行查询所有用户: {}", sql);
        return jdbcTemplate.query(sql, USER_ROW_MAPPER);
    }
    
    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象，不存在则返回 null
     */
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        log.debug("执行根据ID查询用户: {}, 参数: {}", sql, id);
        List<User> users = jdbcTemplate.query(sql, USER_ROW_MAPPER, id);
        return users.isEmpty() ? null : users.get(0);
    }
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    public List<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username LIKE ?";
        log.debug("执行根据用户名查询: {}, 参数: {}", sql, username);
        return jdbcTemplate.query(sql, USER_ROW_MAPPER, "%" + username + "%");
    }
    
    /**
     * 插入用户
     *
     * @param user 用户对象
     * @return 影响的行数
     */
    public int insert(User user) {
        String sql = "INSERT INTO users (username, email, age) VALUES (?, ?, ?)";
        log.debug("执行插入用户: {}, 参数: {}", sql, user);
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getAge());
    }
    
    /**
     * 更新用户
     *
     * @param user 用户对象
     * @return 影响的行数
     */
    public int update(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, age = ? WHERE id = ?";
        log.debug("执行更新用户: {}, 参数: {}", sql, user);
        return jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getAge(), user.getId());
    }
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 影响的行数
     */
    public int deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        log.debug("执行删除用户: {}, 参数: {}", sql, id);
        return jdbcTemplate.update(sql, id);
    }
    
    /**
     * 统计用户数量
     *
     * @return 用户总数
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM users";
        log.debug("执行统计用户数量: {}", sql);
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
    
    /**
     * 批量插入用户
     *
     * @param users 用户列表
     * @return 影响的行数数组
     */
    public int[] batchInsert(List<User> users) {
        String sql = "INSERT INTO users (username, email, age) VALUES (?, ?, ?)";
        log.debug("执行批量插入用户: {}, 数量: {}", sql, users.size());
        
        List<Object[]> batchArgs = users.stream()
                .map(user -> new Object[]{user.getUsername(), user.getEmail(), user.getAge()})
                .toList();
        
        return jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
