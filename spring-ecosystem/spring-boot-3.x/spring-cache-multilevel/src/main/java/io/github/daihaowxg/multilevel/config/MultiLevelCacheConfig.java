package io.github.daihaowxg.multilevel.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 二级缓存配置类
 */
@Configuration
public class MultiLevelCacheConfig {

    /**
     * 配置 RedisTemplate
     * 用于二级缓存的底层存储
     */
    @Bean
    public RedisTemplate<Object, Object> multiLevelRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用 JSON 序列化
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);

        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置二级缓存管理器
     * 
     * @param redisTemplate 注入根据上面配置创建的 RedisTemplate
     */
    @Bean("multiLevelCacheManager")
    public CacheManager multiLevelCacheManager(RedisTemplate<Object, Object> multiLevelRedisTemplate) {
        return new MultiLevelCacheManager(multiLevelRedisTemplate);
    }
}
