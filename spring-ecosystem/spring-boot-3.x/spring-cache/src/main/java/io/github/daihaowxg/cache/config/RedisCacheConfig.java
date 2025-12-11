package io.github.daihaowxg.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 缓存配置类（可选）
 * <p>
 * 使用条件：
 * - 需要在 application.yml 中配置 spring.cache.type=redis
 * - 需要启动 Redis 服务
 * <p>
 * Redis 适用场景：
 * 1. 分布式系统：多个应用实例共享缓存
 * 2. 大数据量：本地缓存容量有限
 * 3. 持久化需求：Redis 可以持久化缓存数据
 */
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class RedisCacheConfig {

    /**
     * 配置 Redis 缓存管理器
     *
     * @param connectionFactory Redis 连接工厂
     * @return CacheManager
     */
    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
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
                // 禁用缓存空值
                // .disableCachingNullValues()
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
}
