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

@SpringBootTest
@TestPropertySource(properties = {
        "spring.cache.type=redis",
        "spring.data.redis.host=localhost", // 默认连接本地
        "spring.data.redis.port=6379"
})
class UserServiceRedisTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private org.springframework.data.redis.core.StringRedisTemplate redisTemplate;

    @BeforeEach
    void setUp() {
        // 清理所有 key，保证测试隔离
        if (redisTemplate.getConnectionFactory() != null) {
            redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
        }
    }

    @Test
    void testCacheable_Redis() {
        Long userId = 1L;
        User user = new User(userId, "Redis User", "redis@test.com", 25);

        // 1. 保存 (Write Through / @CachePut)
        userService.createUser(user);

        // 2. 验证 Redis 中有数据
        // CacheManager 应该是 RedisCacheManager
        Cache cache = cacheManager.getCache("users");
        assertThat(cache).isNotNull();
        // Redis 序列化后，虽然是二进制，但 Spring Cache 抽象层能读出来
        assertThat(cache.get(userId)).isNotNull();
        User cachedUser = cache.get(userId, User.class);
        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getName()).isEqualTo("Redis User");

        // 3. 验证再次查询不走数据库
        User user1 = userService.getUserById(userId);
        assertThat(user1.getEmail()).isEqualTo("redis@test.com");
    }

    @Test
    void testCacheEvict_Redis() {
        Long userId = 2L;
        User user = new User(userId, "To Delete", "del@test.com", 30);
        userService.createUser(user);

        // 确保存在
        Cache cache = cacheManager.getCache("users");
        Assertions.assertNotNull(cache);
        assertThat(cache.get(userId)).isNotNull();

        // 删除
        userService.deleteUser(userId);

        // 验证消失
        assertThat(cache.get(userId)).isNull();
    }
}
