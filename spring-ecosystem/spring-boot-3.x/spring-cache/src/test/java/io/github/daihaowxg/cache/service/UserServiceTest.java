package io.github.daihaowxg.cache.service;

import io.github.daihaowxg.cache.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserService 单元测试类
 * <p>
 * 本通常用于验证 Spring Cache 核心注解的实际行为，确保缓存逻辑符合预期。
 * 使用 {@code spring.cache.type=caffeine} 属性强制使用 Caffeine 作为缓存实现，
 * 避免受环境（如是否有 Redis）影响，保证测试的独立性和稳定性。
 * <p>
 * 测试涵盖的注解：
 * <ul>
 * <li>{@link org.springframework.cache.annotation.Cacheable @Cacheable}</li>
 * <li>{@link org.springframework.cache.annotation.CachePut @CachePut}</li>
 * <li>{@link org.springframework.cache.annotation.CacheEvict @CacheEvict}</li>
 * </ul>
 */
@SpringBootTest
@TestPropertySource(properties = "spring.cache.type=caffeine")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 测试前置准备
     * <p>
     * 这里的逻辑是：每个测试方法执行前，都先清空 "users" 缓存。
     * 这样做是为了防止不同测试方法之间的缓存数据互相干扰（保证测试隔离性）。
     */
    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache("users");
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 测试 @Cacheable (查询缓存)
     * <p>
     * 场景说明：
     * 1. 调用 createUser (带 @CachePut) -> 数据入库并同时入缓存。
     * 2. 调用 getUserById (带 @Cacheable) -> 应该直接从缓存命中，不再查库。
     * <p>
     * 验证点：
     * 1. 业务方法返回结果正常。
     * 2. 缓存容器中确实存在对应的 key-value。
     * 3. 缓存中的数据与业务对象一致。
     */
    @Test
    void testCacheable() {
        Long userId = 1L;
        // 准备数据
        User user = new User(userId, "Cache User", "cache@test.com", 18);

        // 1. 保存用户
        // UserService.createUser 方法上有 @CachePut 注解
        // 执行后，Spring 会将返回的 user 对象放入 "users" 缓存，key 为 user.id (即 1L)
        userService.createUser(user);

        // 2. 查询用户
        // UserService.getUserById 方法上有 @Cacheable 注解
        // 因为上一步已经缓存了 key=1 的数据，这里会直接从缓存获取，不会打印 "从数据库查询用户..." 的日志
        User user1 = userService.getUserById(userId);
        assertThat(user1).isNotNull();

        // 3. 验证缓存状态
        // 获取 "users" 缓存容器
        Cache cache = cacheManager.getCache("users");
        Assertions.assertNotNull(cache, "Cache 'users' should exist");

        // 验证缓存中包含 key=1 的数据
        assertThat(cache.get(userId)).isNotNull();

        // 取出缓存中的具体对象进行比对
        User cachedUser = cache.get(userId, User.class);
        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getName()).isEqualTo(user1.getName());
    }

    /**
     * 测试 @CachePut (更新/写入缓存)
     * <p>
     * 场景说明：
     * 
     * @CachePut 的特点是：方法始终会被执行（不会像 @Cacheable 那样被跳过），
     *           并且执行后的返回值会自动被更新到缓存中。
     *           <p>
     *           验证点：
     *           1. 方法执行后，缓存中立即有了对应的数据。
     *           2. 验证存入的数据是否正确。
     */
    @Test
    void testCachePut() {
        // 创建新用户对象
        User user = new User();
        user.setId(100L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        // 执行创建操作
        // 这里会触发 @CachePut，将 user 放入缓存 key=100L 的位置
        userService.createUser(user);

        // 验证缓存是否存在
        Cache cache = cacheManager.getCache("users");
        Assertions.assertNotNull(cache, "Cache 'users' should exist");

        // 验证 key=100L 的数据是否确实在缓存中
        assertThat(cache.get(100L)).isNotNull();

        // 验证缓存内容是否即为我们刚才保存的对象
        User cachedUser = cache.get(100L, User.class);
        Assertions.assertNotNull(cachedUser);
        assertThat(cachedUser.getName()).isEqualTo("Test User");
    }

    /**
     * 测试 @CacheEvict (驱逐/删除缓存)
     * <p>
     * 场景说明：
     * 当数据被删除时，缓存也应该同步被清理，否则会产生脏数据。
     * 
     * @CacheEvict 就是用来做这个的。
     *             <p>
     *             验证流程：
     *             1. 手动向缓存塞入一条数据（模拟已有缓存）。
     *             2. 确认缓存存在。
     *             3. 调用 deleteUser 方法（带 @CacheEvict）。
     *             4. 再次检查缓存，确认该 key 对应的数据已被移除。
     */
    @Test
    void testCacheEvict() {
        Long userId = 200L;
        User user = new User(userId, "To Delete", "delete@example.com", 20);

        // 1. 获取缓存容器
        Cache cache = cacheManager.getCache("users");
        Assertions.assertNotNull(cache, "Cache 'users' should exist");

        // 2. 手动直接写入缓存 (模拟之前的查询已经建立了缓存)
        cache.put(userId, user);

        // 3. 确保缓存存在 (Pre-check)
        assertThat(cache.get(userId)).isNotNull();

        // 4. 执行删除业务操作
        // UserService.deleteUser 方法上有 @CacheEvict(key = "#id")
        // 执行后应该将 key=200L 的缓存条目移除
        userService.deleteUser(userId);

        // 5. 验证缓存已被清除 (Post-check)
        assertThat(cache.get(userId)).isNull();
    }
}
