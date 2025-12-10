package io.github.daihaowxg.demo.lab16springcache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

/**
 * 缓存配置类 - 配置 Caffeine 缓存管理器
 * 
 * Caffeine 是一个高性能的本地缓存库，基于 Google Guava Cache 改进
 * 特点：
 * 1. 高性能：使用 Window TinyLFU 淘汰算法
 * 2. 灵活配置：支持多种过期策略和大小限制
 * 3. 统计功能：可以统计缓存命中率等指标
 */
@Configuration
@EnableCaching // 启用 Spring Cache 支持
public class CacheConfig {

    /**
     * 配置 Caffeine 缓存管理器
     * 
     * @return CacheManager
     */
    @Bean
    @Primary // 设置为主要的 CacheManager
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
     * 自定义 KeyGenerator
     * 
     * 用于生成复杂的缓存 key
     * 格式：类名:方法名:参数1:参数2:...
     * 
     * @return KeyGenerator
     */
    @Bean("customKeyGenerator")
    public KeyGenerator customKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName()).append(":");
            sb.append(method.getName()).append(":");
            for (Object param : params) {
                sb.append(param != null ? param.toString() : "null").append(":");
            }
            // 移除最后一个冒号
            if (sb.length() > 0) {
                sb.setLength(sb.length() - 1);
            }
            return sb.toString();
        };
    }

    /**
     * 配置用户缓存（自定义配置）
     * 
     * 可以为不同的缓存设置不同的过期时间和大小限制
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
