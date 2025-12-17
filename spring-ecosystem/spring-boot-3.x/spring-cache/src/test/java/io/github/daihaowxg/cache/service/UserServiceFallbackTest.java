package io.github.daihaowxg.cache.service;

import io.github.daihaowxg.cache.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        // 指向错误的 Redis 端口，模拟 Redis 不可用
        "spring.data.redis.port=6380",
        // 缩短连接超时，加快测试速度 (Lettuce 默认超时较长，可能影响测试体验)
        "spring.data.redis.timeout=100ms"
})
class UserServiceFallbackTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void testFallbackToCaffeine() {
        // 1. 验证 CacheManager 降级为 CaffeineCacheManager
        assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);
        System.out.println("成功降级为 CaffeineCacheManager");

        // 2. 验证缓存功能依然可用
        Long userId = 9999L;
        User user = new User(userId, "Fallback User", "fallback@test.com", 20);

        // 首次调用，走业务逻辑（写入缓存）
        userService.createUser(user);

        // 验证缓存中有数据
        Cache cache = cacheManager.getCache("users");
        assertThat(cache).isNotNull();
        assertThat(cache.get(userId)).isNotNull();

        // 验证查询不报错
        User cachedUser = userService.getUserById(userId);
        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getName()).isEqualTo("Fallback User");
    }
}
