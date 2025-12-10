package io.github.daihaowxg.demo.lab16springcache.service;

import io.github.daihaowxg.demo.lab16springcache.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务类 - 演示 Spring Cache 基础注解
 * 
 * 主要演示：
 * 1. @Cacheable - 缓存查询结果
 * 2. @CachePut - 更新缓存
 * 3. @CacheEvict - 清除缓存
 * 4. @Caching - 组合多个缓存操作
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "users") // 类级别的缓存配置，指定默认缓存名称
public class UserService {

    /**
     * 模拟数据库存储
     */
    private final Map<Long, User> database = new HashMap<>();

    /**
     * 查询用户 - 演示 @Cacheable
     * 
     * @Cacheable 注解说明：
     *            - value/cacheNames: 缓存名称
     *            - key: 缓存的 key，支持 SpEL 表达式，#id 表示方法参数 id
     *            - 首次调用会执行方法体并缓存结果
     *            - 后续调用如果缓存存在则直接返回缓存，不执行方法体
     * 
     * @param id 用户 ID
     * @return 用户信息
     */
    @Cacheable(key = "#id")
    public User getUserById(Long id) {
        log.info("从数据库查询用户，ID: {}", id);
        // 模拟数据库查询耗时
        simulateSlowQuery();
        return database.get(id);
    }

    /**
     * 创建用户 - 演示 @CachePut
     * 
     * @CachePut 注解说明：
     *           - 无论缓存是否存在，都会执行方法体
     *           - 执行完成后将返回值更新到缓存中
     *           - 适用于新增和更新操作
     *           - key 使用返回值的 id 属性：#result.id
     * 
     * @param user 用户信息
     * @return 创建的用户
     */
    @CachePut(key = "#result.id")
    public User createUser(User user) {
        log.info("创建用户: {}", user);
        database.put(user.getId(), user);
        return user;
    }

    /**
     * 更新用户 - 演示 @CachePut
     * 
     * @param user 用户信息
     * @return 更新后的用户
     */
    @CachePut(key = "#user.id")
    public User updateUser(User user) {
        log.info("更新用户: {}", user);
        database.put(user.getId(), user);
        return user;
    }

    /**
     * 删除用户 - 演示 @CacheEvict
     * 
     * @CacheEvict 注解说明：
     *             - 执行方法后清除指定的缓存
     *             - key: 要清除的缓存 key
     *             - allEntries: 是否清除所有缓存条目（默认 false）
     *             - beforeInvocation: 是否在方法执行前清除缓存（默认 false，即方法执行后清除）
     * 
     * @param id 用户 ID
     */
    @CacheEvict(key = "#id")
    public void deleteUser(Long id) {
        log.info("删除用户，ID: {}", id);
        database.remove(id);
    }

    /**
     * 清除所有用户缓存 - 演示 @CacheEvict 的 allEntries 属性
     */
    @CacheEvict(allEntries = true)
    public void clearAllCache() {
        log.info("清除所有用户缓存");
    }

    /**
     * 刷新用户缓存 - 演示 @Caching 组合注解
     * 
     * @Caching 注解说明：
     *          - 可以组合多个缓存操作
     *          - evict: 先清除旧缓存
     *          - put: 再更新新缓存
     * 
     * @param id 用户 ID
     * @return 用户信息
     */
    @CachePut(key = "#id")
    public User refreshUser(Long id) {
        log.info("刷新用户缓存，ID: {}", id);
        // 模拟从数据库重新加载
        simulateSlowQuery();
        return database.get(id);
    }

    /**
     * 根据邮箱查询用户 - 演示自定义缓存 key
     * 
     * 使用 SpEL 表达式自定义 key：
     * - 'email:' 是字符串常量
     * - #email 是方法参数
     * - 最终 key 格式为：email:xxx@example.com
     * 
     * @param email 邮箱
     * @return 用户信息
     */
    @Cacheable(key = "'email:' + #email")
    public User getUserByEmail(String email) {
        log.info("根据邮箱查询用户: {}", email);
        simulateSlowQuery();
        return database.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取数据库中的所有用户（不缓存）
     * 用于测试和验证
     */
    public Map<Long, User> getAllUsers() {
        return new HashMap<>(database);
    }

    /**
     * 模拟慢查询
     */
    private void simulateSlowQuery() {
        try {
            Thread.sleep(1000); // 模拟数据库查询耗时 1 秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
