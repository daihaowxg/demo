package io.github.daihaowxg.cache.service;

import io.github.daihaowxg.cache.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.cache.type=redis")
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        // 每个测试前清空缓存
        Cache cache = cacheManager.getCache("users");
        if (cache != null) {
            cache.clear();
        }
    }

    @Test
    void testCacheable() {
        Long userId = 1L;
        // 准备数据
        User user = new User(userId, "Cache User", "cache@test.com", 18);
        userService.createUser(user);

        // 第一次查询：应该执行方法体（在这里是查库）
        User user1 = userService.getUserById(userId);
        assertThat(user1).isNotNull();

        // 验证放入了缓存
        Cache cache = cacheManager.getCache("users");
        assertThat(cache.get(userId)).isNotNull();
        User cachedUser = cache.get(userId, User.class);
        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getName()).isEqualTo(user1.getName());

        // 第二次查询：应该直接走缓存（如果 UserService 内部有日志打印，此时不应再次打印）
        // 这里的验证有点弱，通常结合 Mockito Spy 验证方法调用次数更严谨，
        // 但为了简单演示缓存存在性，检查 CacheManager 也可以。
        User user2 = userService.getUserById(userId);
        assertThat(user2).isEqualTo(user1);
    }

    @Test
    void testCachePut() {
        // 创建新用户
        User user = new User();
        user.setId(100L);
        user.setName("Test User");
        user.setEmail("test@example.com");

        // 执行创建操作，应该触发 @CachePut
        userService.createUser(user);

        // 验证缓存是否存在
        Cache cache = cacheManager.getCache("users");
        assertThat(cache.get(100L)).isNotNull();
        User cachedUser = cache.get(100L, User.class);
        assertThat(cachedUser.getName()).isEqualTo("Test User");
    }

    @Test
    void testCacheEvict() {
        // 先手动放入一个缓存
        Long userId = 200L;
        User user = new User(userId, "To Delete", "delete@example.com", 20);
        Cache cache = cacheManager.getCache("users");
        cache.put(userId, user);

        // 确保缓存存在
        assertThat(cache.get(userId)).isNotNull();

        // 执行删除操作
        userService.deleteUser(userId);

        // 验证缓存已被清除
        assertThat(cache.get(userId)).isNull();
    }
}
