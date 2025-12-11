package io.github.daihaowxg.multilevel.config;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;

/**
 * 自定义二级缓存实现
 * L1: Caffeine (Process Local)
 * L2: Redis (Global Shared)
 */
@Slf4j
public class MultiLevelCache extends AbstractValueAdaptingCache {

    private final String name;
    private final Cache<Object, Object> caffeineCache;
    private final RedisTemplate<Object, Object> redisTemplate;

    public MultiLevelCache(String name, Cache<Object, Object> caffeineCache,
            RedisTemplate<Object, Object> redisTemplate) {
        // 允许缓存 null 值
        super(true);
        this.name = name;
        this.caffeineCache = caffeineCache;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Object lookup(Object key) {
        // 1. 查 L1 (Caffeine)
        Object value = caffeineCache.getIfPresent(key);
        if (value != null) {
            log.debug("[L1 Hit] key={}", key);
            return value;
        }

        // 2. 查 L2 (Redis)
        String redisKey = getRedisKey(key);
        value = redisTemplate.opsForValue().get(redisKey);

        if (value != null) {
            log.debug("[L2 Hit] key={}, 回填 L1", key);
            // 3. 回填 L1
            caffeineCache.put(key, value);
            return value;
        }

        log.debug("[Miss] key={}", key);
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        // 简单实现：这里为了演示方便，不处理复杂的竞争条件
        Object value = lookup(key);
        if (value != null) {
            return (T) fromStoreValue(value);
        }

        try {
            // 执行业务方法加载数据
            T loadedValue = valueLoader.call();
            // 存入缓存
            put(key, loadedValue);
            return loadedValue;
        } catch (Exception e) {
            throw new ValueRetrievalException(key, valueLoader, e);
        }
    }

    @Override
    public void put(Object key, Object value) {
        // 同时写入 L1 和 L2
        log.debug("[Put] key={}", key);
        caffeineCache.put(key, toStoreValue(value));

        String redisKey = getRedisKey(key);
        redisTemplate.opsForValue().set(redisKey, toStoreValue(value));
    }

    @Override
    public void evict(Object key) {
        // 同时删除 L1 和 L2
        log.debug("[Evict] key={}", key);
        caffeineCache.invalidate(key);

        String redisKey = getRedisKey(key);
        redisTemplate.delete(redisKey);
    }

    @Override
    public void clear() {
        // 清空 L1
        caffeineCache.invalidateAll();
        // 清空 L2 (注意：这里使用 pattern 删除是危险操作，生产环境需谨慎)
        // Set<Object> keys = redisTemplate.keys(name + ":*");
        // if (keys != null && !keys.isEmpty()) {
        // redisTemplate.delete(keys);
        // }
        log.warn("clear() method called but only L1 cleared for safety");
    }

    private String getRedisKey(Object key) {
        // Redis Key 增加 CacheName 前缀，防止冲突
        return this.name + ":" + key.toString();
    }
}
