package io.github.daihaowxg.multidatasource.repository;

import io.github.daihaowxg.multidatasource.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 多数据源 JdbcTemplate 使用示例
 * <p>
 * 演示如何在同一个 Repository 中使用多个 JdbcTemplate 访问不同的数据源。
 * <p>
 * <b>核心要点:</b>
 * <ul>
 * <li>使用 {@code @Qualifier} 注解注入不同的 JdbcTemplate</li>
 * <li>每个 JdbcTemplate 对应一个独立的数据源</li>
 * <li>使用 {@code @Transactional} 时需要指定事务管理器</li>
 * <li>可以在同一个方法中操作多个数据源(分布式事务需要额外配置)</li>
 * </ul>
 */
@Slf4j
@Repository
public class MultiDataSourceRepository {

    // 注入主数据源的 JdbcTemplate
    private final JdbcTemplate primaryJdbcTemplate;

    // 注入第二个数据源的 JdbcTemplate
    private final JdbcTemplate secondaryJdbcTemplate;

    /**
     * 构造函数注入
     * <p>
     * 使用 @Qualifier 注解指定要注入的具体 Bean
     *
     * @param primaryJdbcTemplate   主数据源的 JdbcTemplate
     * @param secondaryJdbcTemplate 第二个数据源的 JdbcTemplate
     */
    public MultiDataSourceRepository(
            @Qualifier("primaryJdbcTemplate") JdbcTemplate primaryJdbcTemplate,
            @Qualifier("secondaryJdbcTemplate") JdbcTemplate secondaryJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
    }

    // ==================== 主数据源操作 ====================

    /**
     * 从主数据源查询用户
     * <p>
     * 使用 primaryJdbcTemplate 访问主数据库
     */
    public Optional<User> findUserFromPrimary(Long id) {
        log.info("从主数据源查询用户: id={}", id);

        String sql = "SELECT id, name, email FROM users WHERE id = ?";

        return primaryJdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
            return Optional.empty();
        }, id);
    }

    /**
     * 向主数据源插入用户
     * <p>
     * 使用 @Transactional 并指定主数据源的事务管理器
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public void saveUserToPrimary(User user) {
        log.info("向主数据源插入用户: {}", user);

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        primaryJdbcTemplate.update(sql, user.getName(), user.getEmail());
    }

    /**
     * 从主数据源查询所有用户
     */
    public List<User> findAllUsersFromPrimary() {
        log.info("从主数据源查询所有用户");

        String sql = "SELECT id, name, email FROM users";

        return primaryJdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")));
    }

    // ==================== 第二个数据源操作 ====================

    /**
     * 从第二个数据源查询用户
     * <p>
     * 使用 secondaryJdbcTemplate 访问第二个数据库
     */
    public Optional<User> findUserFromSecondary(Long id) {
        log.info("从第二个数据源查询用户: id={}", id);

        String sql = "SELECT id, name, email FROM users WHERE id = ?";

        return secondaryJdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
            return Optional.empty();
        }, id);
    }

    /**
     * 向第二个数据源插入用户
     * <p>
     * 使用 @Transactional 并指定第二个数据源的事务管理器
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public void saveUserToSecondary(User user) {
        log.info("向第二个数据源插入用户: {}", user);

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        secondaryJdbcTemplate.update(sql, user.getName(), user.getEmail());
    }

    /**
     * 从第二个数据源查询所有用户
     */
    public List<User> findAllUsersFromSecondary() {
        log.info("从第二个数据源查询所有用户");

        String sql = "SELECT id, name, email FROM users";

        return secondaryJdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")));
    }

    // ==================== 跨数据源操作 ====================

    /**
     * 跨数据源数据同步
     * <p>
     * <b>注意:</b> 这个方法演示了如何在同一个方法中操作多个数据源。
     * <p>
     * <b>重要提示:</b>
     * <ul>
     * <li>这里没有使用 @Transactional,因为涉及多个数据源</li>
     * <li>如果需要分布式事务,需要使用 JTA 或其他分布式事务解决方案</li>
     * <li>当前实现中,两个数据源的操作是独立的,不保证原子性</li>
     * </ul>
     *
     * @param id 用户 ID
     */
    public void syncUserBetweenDataSources(Long id) {
        log.info("开始跨数据源同步用户: id={}", id);

        // 从主数据源查询用户
        Optional<User> userOpt = findUserFromPrimary(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            log.info("从主数据源获取到用户: {}", user);

            // 保存到第二个数据源
            // 注意: 这里没有事务保证,如果保存失败,主数据源的数据不会回滚
            saveUserToSecondary(user);

            log.info("用户同步完成");
        } else {
            log.warn("主数据源中未找到用户: id={}", id);
        }
    }

    /**
     * 统计两个数据源的用户总数
     * <p>
     * 演示如何同时查询多个数据源并汇总结果
     */
    public int getTotalUserCount() {
        log.info("统计两个数据源的用户总数");

        // 查询主数据源的用户数
        Integer primaryCount = primaryJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users",
                Integer.class);

        // 查询第二个数据源的用户数
        Integer secondaryCount = secondaryJdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users",
                Integer.class);

        int total = (primaryCount != null ? primaryCount : 0) +
                (secondaryCount != null ? secondaryCount : 0);

        log.info("主数据源: {} 个用户, 第二个数据源: {} 个用户, 总计: {} 个用户",
                primaryCount, secondaryCount, total);

        return total;
    }

    /**
     * 演示批量操作 - 分别向两个数据源插入数据
     *
     * @param users 用户列表
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public void batchInsertToPrimary(List<User> users) {
        log.info("批量插入 {} 个用户到主数据源", users.size());

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        primaryJdbcTemplate.batchUpdate(sql, users, users.size(), (ps, user) -> {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
        });

        log.info("批量插入完成");
    }

    /**
     * 演示如何使用不同的 JdbcTemplate 执行相同的查询
     * <p>
     * 这个方法展示了如何根据参数动态选择数据源
     *
     * @param id               用户 ID
     * @param usePrimarySource 是否使用主数据源
     * @return 用户信息
     */
    public Optional<User> findUserDynamically(Long id, boolean usePrimarySource) {
        JdbcTemplate jdbcTemplate = usePrimarySource ? primaryJdbcTemplate : secondaryJdbcTemplate;
        String sourceName = usePrimarySource ? "主数据源" : "第二个数据源";

        log.info("从 {} 查询用户: id={}", sourceName, id);

        String sql = "SELECT id, name, email FROM users WHERE id = ?";

        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")));
            }
            return Optional.empty();
        }, id);
    }
}
