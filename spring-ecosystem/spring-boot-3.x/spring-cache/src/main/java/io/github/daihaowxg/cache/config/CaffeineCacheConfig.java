package io.github.daihaowxg.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存配置类
 * <p>
 * 只有当 spring.cache.type=caffeine (或者未配置) 时才加载
 */
@Configuration
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "caffeine", matchIfMissing = true)
public class CaffeineCacheConfig {

    /**
     * 配置 Caffeine 缓存管理器
     *
     * @return CacheManager
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 设置 Caffeine 配置
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 初始容量
                .initialCapacity(100)
                // 最大容量（超过会根据 LFU 算法淘汰）
                .maximumSize(1000)
                // 写入后过期时间
                .expireAfterWrite(10, TimeUnit.MINUTES)
                // 访问后过期时间（可选，与 expireAfterWrite 二选一）
                // .expireAfterAccess(5, TimeUnit.MINUTES)
                // 启用统计功能
                .recordStats());

        return cacheManager;
    }

    /**
     * 配置用户缓存（自定义配置）
     *
     * @return CacheManager
     */
    @Bean("userCacheManager")
    public CacheManager userCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.MINUTES) // 用户缓存 30 分钟过期
                .recordStats());
        return cacheManager;
    }

    /**
     * 配置商品缓存（自定义配置）
     *
     * @return CacheManager
     */
    @Bean("productCacheManager")
    public CacheManager productCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("products");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(2000)
                .expireAfterWrite(5, TimeUnit.MINUTES) // 商品缓存 5 分钟过期
                .recordStats());
        return cacheManager;
    }
}
