package io.github.daihaowxg.mybatis.service;

import io.github.daihaowxg.mybatis.entity.User;
import io.github.daihaowxg.mybatis.mapper.primary.PrimaryUserMapper;
import io.github.daihaowxg.mybatis.mapper.secondary.SecondaryUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis 多数据源使用示例
 * 
 * <p>
 * 演示如何在 Service 层使用多个数据源的 Mapper
 * </p>
 * 
 * <p>
 * <b>关键点：</b>
 * </p>
 * <ul>
 * <li>直接注入不同数据源的 Mapper</li>
 * <li>使用 @Transactional 时需要指定 transactionManager</li>
 * <li>跨数据源操作不在同一个事务中</li>
 * </ul>
 *
 * @author daihaowxg
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyBatisMultiDataSourceService {

    private final PrimaryUserMapper primaryUserMapper;
    private final SecondaryUserMapper secondaryUserMapper;

    // ==================== 主数据源操作 ====================

    /**
     * 从主数据源查询所有用户
     */
    public List<User> findAllFromPrimary() {
        log.info("从主数据源查询所有用户");
        return primaryUserMapper.findAll();
    }

    /**
     * 从主数据源根据 ID 查询用户
     */
    public Optional<User> findByIdFromPrimary(Long id) {
        log.info("从主数据源查询用户，ID: {}", id);
        return primaryUserMapper.findById(id);
    }

    /**
     * 保存用户到主数据源
     * 
     * <p>
     * 使用主数据源的事务管理器
     * </p>
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public User saveUserToPrimary(User user) {
        log.info("保存用户到主数据源: {}", user);
        primaryUserMapper.insert(user);
        log.info("用户已保存到主数据源，生成的 ID: {}", user.getId());
        return user;
    }

    /**
     * 更新主数据源中的用户
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public int updateUserInPrimary(User user) {
        log.info("更新主数据源中的用户: {}", user);
        return primaryUserMapper.update(user);
    }

    /**
     * 从主数据源删除用户
     */
    @Transactional(transactionManager = "primaryTransactionManager")
    public int deleteUserFromPrimary(Long id) {
        log.info("从主数据源删除用户，ID: {}", id);
        return primaryUserMapper.deleteById(id);
    }

    // ==================== 第二个数据源操作 ====================

    /**
     * 从第二个数据源查询所有用户
     */
    public List<User> findAllFromSecondary() {
        log.info("从第二个数据源查询所有用户");
        return secondaryUserMapper.findAll();
    }

    /**
     * 从第二个数据源根据 ID 查询用户
     */
    public Optional<User> findByIdFromSecondary(Long id) {
        log.info("从第二个数据源查询用户，ID: {}", id);
        return secondaryUserMapper.findById(id);
    }

    /**
     * 保存用户到第二个数据源
     * 
     * <p>
     * 使用第二个数据源的事务管理器
     * </p>
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public User saveUserToSecondary(User user) {
        log.info("保存用户到第二个数据源: {}", user);
        secondaryUserMapper.insert(user);
        log.info("用户已保存到第二个数据源，生成的 ID: {}", user.getId());
        return user;
    }

    /**
     * 更新第二个数据源中的用户
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public int updateUserInSecondary(User user) {
        log.info("更新第二个数据源中的用户: {}", user);
        return secondaryUserMapper.update(user);
    }

    /**
     * 从第二个数据源删除用户
     */
    @Transactional(transactionManager = "secondaryTransactionManager")
    public int deleteUserFromSecondary(Long id) {
        log.info("从第二个数据源删除用户，ID: {}", id);
        return secondaryUserMapper.deleteById(id);
    }

    // ==================== 跨数据源操作 ====================

    /**
     * 同步用户数据：从主数据源复制到第二个数据源
     * 
     * <p>
     * <b>注意：</b>这个操作涉及两个数据源，不在同一个事务中！
     * </p>
     * <p>
     * 如果需要保证事务一致性，需要使用分布式事务（如 JTA、Seata）
     * </p>
     */
    public void syncUserFromPrimaryToSecondary(Long id) {
        log.info("开始同步用户数据，ID: {}", id);

        // 从主数据源查询
        Optional<User> userOpt = primaryUserMapper.findById(id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 重置 ID，让第二个数据源自动生成
            user.setId(null);

            // 保存到第二个数据源
            secondaryUserMapper.insert(user);
            log.info("用户数据已同步到第二个数据源，新 ID: {}", user.getId());
        } else {
            log.warn("主数据源中未找到 ID 为 {} 的用户", id);
        }
    }

    /**
     * 获取两个数据源的用户总数
     */
    public String getUserCountSummary() {
        long primaryCount = primaryUserMapper.count();
        long secondaryCount = secondaryUserMapper.count();

        return String.format("主数据源用户数: %d, 第二个数据源用户数: %d",
                primaryCount, secondaryCount);
    }

    /**
     * 批量同步用户：从主数据源同步所有用户到第二个数据源
     * 
     * <p>
     * <b>警告：</b>此操作不保证事务一致性！
     * </p>
     */
    public int syncAllUsersFromPrimaryToSecondary() {
        log.info("开始批量同步用户数据");

        List<User> users = primaryUserMapper.findAll();
        int count = 0;

        for (User user : users) {
            // 重置 ID
            user.setId(null);
            secondaryUserMapper.insert(user);
            count++;
        }

        log.info("批量同步完成，共同步 {} 个用户", count);
        return count;
    }
}
