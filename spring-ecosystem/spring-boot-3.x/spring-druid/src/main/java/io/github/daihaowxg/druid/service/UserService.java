package io.github.daihaowxg.druid.service;

import com.alibaba.druid.pool.DruidDataSource;
import io.github.daihaowxg.druid.entity.User;
import io.github.daihaowxg.druid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

/**
 * 用户服务层
 * 演示 Druid 连接池的使用和监控
 *
 * @author daihaowxg
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final DataSource dataSource;
    
    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        log.info("查询所有用户");
        return userRepository.findAll();
    }
    
    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    public User getUserById(Long id) {
        log.info("根据ID查询用户: {}", id);
        return userRepository.findById(id);
    }
    
    /**
     * 根据用户名搜索用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    public List<User> searchByUsername(String username) {
        log.info("根据用户名搜索: {}", username);
        return userRepository.findByUsername(username);
    }
    
    /**
     * 创建用户
     *
     * @param user 用户对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(User user) {
        log.info("创建用户: {}", user);
        int rows = userRepository.insert(user);
        return rows > 0;
    }
    
    /**
     * 更新用户
     *
     * @param user 用户对象
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        log.info("更新用户: {}", user);
        int rows = userRepository.update(user);
        return rows > 0;
    }
    
    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        log.info("删除用户: {}", id);
        int rows = userRepository.deleteById(id);
        return rows > 0;
    }
    
    /**
     * 批量创建用户
     *
     * @param users 用户列表
     * @return 成功插入的数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int batchCreateUsers(List<User> users) {
        log.info("批量创建用户，数量: {}", users.size());
        int[] rows = userRepository.batchInsert(users);
        return rows.length;
    }
    
    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    public long getUserCount() {
        log.info("获取用户总数");
        return userRepository.count();
    }
    
    /**
     * 获取 Druid 连接池统计信息
     * 演示如何获取 Druid 的运行时信息
     *
     * @return 连接池统计信息
     */
    public String getDruidStatistics() {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            StringBuilder stats = new StringBuilder();
            stats.append("=== Druid 连接池统计信息 ===\n");
            stats.append("活跃连接数: ").append(druidDataSource.getActiveCount()).append("\n");
            stats.append("空闲连接数: ").append(druidDataSource.getPoolingCount()).append("\n");
            stats.append("等待线程数: ").append(druidDataSource.getWaitThreadCount()).append("\n");
            stats.append("创建连接总数: ").append(druidDataSource.getCreateCount()).append("\n");
            stats.append("销毁连接总数: ").append(druidDataSource.getDestroyCount()).append("\n");
            stats.append("连接获取总数: ").append(druidDataSource.getConnectCount()).append("\n");
            stats.append("关闭连接总数: ").append(druidDataSource.getCloseCount()).append("\n");
            stats.append("最大活跃连接数: ").append(druidDataSource.getMaxActive()).append("\n");
            stats.append("初始化连接数: ").append(druidDataSource.getInitialSize()).append("\n");
            stats.append("最小空闲连接数: ").append(druidDataSource.getMinIdle()).append("\n");
            
            log.info("Druid 连接池统计:\n{}", stats);
            return stats.toString();
        }
        return "当前数据源不是 DruidDataSource";
    }
}
