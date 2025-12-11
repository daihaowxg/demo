package io.github.daihaowxg.multilevel.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 自定义二级缓存管理器
 */
@RequiredArgsConstructor
public class MultiLevelCacheManager implements CacheManager {

    private final RedisTemplate<Object, Object> redisTemplate;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, this::createCache);
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(cacheMap.keySet());
    }

    private Cache createCache(String name) {
        // 创建 L1 Caffeine Cache
        // 这里硬编码了配置，实际可以做成可配置的
        com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES) // L1 过期时间短一些
                .recordStats()
                .build();

        return new MultiLevelCache(name, caffeineCache, redisTemplate);
    }
}
