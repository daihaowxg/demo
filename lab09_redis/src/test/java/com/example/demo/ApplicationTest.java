package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.Set;

/**
 * @author wxg
 * @since 2025/4/11
 */
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 测试 Redis 连通性
     */
    @Test
    void testRedisConnection() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null) {
            System.out.println("打印所有的key：" + Arrays.toString(keys.toArray()));
        } else {
            System.out.println("没有key");
        }
    }
}
