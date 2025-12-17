package io.github.daihaowxg.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 混合缓存配置：Redis 优先，Caffeine 兜底
 */
@Slf4j
@Configuration
public class HybridCacheConfig {

    /**
     * 动态配置 CacheManager
     * 启动时检测 Redis 连接，如果可用则使用 Redis，否则降级为 Caffeine
     */
    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        try {
            // 尝试连接 Redis 进行检测
            connectionFactory.getConnection().ping();
            log.info("Redis连接成功，使用 RedisCacheManager");
            return createRedisCacheManager(connectionFactory);
        } catch (Exception e) {
            log.warn("Redis连接失败或不可用，降级使用 CaffeineCacheManager", e);
            return createCaffeineCacheManager();
        }
    }

    private RedisCacheManager createRedisCacheManager(RedisConnectionFactory connectionFactory) {
        // 配置 JSON 序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);

        // 配置缓存
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存过期时间（10 分钟）
                .entryTtl(Duration.ofMinutes(10))
                // 设置 key 序列化方式（String）
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer()))
                // 设置 value 序列化方式（JSON）
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        // 创建 RedisCacheManager
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                // 可以为不同的缓存设置不同的过期时间
                .withCacheConfiguration("users",
                        config.entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("products",
                        config.entryTtl(Duration.ofMinutes(5)))
                .build();
    }

    private CaffeineCacheManager createCaffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }
}
