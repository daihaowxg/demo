package io.github.daihaowxg.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 进阶缓存服务 - 演示高级特性
 * 
 * 主要演示：
 * 1. 缓存同步（sync = true）
 * 2. 自定义 KeyGenerator
 * 3. 缓存穿透、雪崩、击穿的解决方案
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "advanced")
public class AdvancedCacheService {

    /**
     * 缓存同步 - 演示 sync 属性
     * 
     * sync = true 说明：
     * - 在高并发场景下，多个线程同时请求同一个 key 时
     * - 只有一个线程会执行方法体，其他线程等待
     * - 避免缓存击穿（大量请求同时穿透缓存访问数据库）
     * - 注意：sync = true 时不能使用 unless 属性
     * 
     * @param key 缓存键
     * @return 计算结果
     */
    @Cacheable(key = "#key", sync = true)
    public String getExpensiveData(String key) {
        log.info("执行耗时操作，key: {}", key);
        // 模拟耗时操作（如复杂计算、远程调用等）
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Result for " + key + " at " + System.currentTimeMillis();
    }

    /**
     * 使用自定义 KeyGenerator
     * 
     * keyGenerator 属性说明：
     * - 指定自定义的 KeyGenerator Bean 名称
     * - 适用于复杂的 key 生成逻辑
     * - 不能与 key 属性同时使用
     * 
     * @param param1 参数1
     * @param param2 参数2
     * @return 结果
     */
    @Cacheable(keyGenerator = "customKeyGenerator")
    public String getDataWithCustomKey(String param1, Integer param2) {
        log.info("使用自定义 KeyGenerator，参数: {}, {}", param1, param2);
        simulateSlowQuery();
        return String.format("Data: %s-%d", param1, param2);
    }

    /**
     * 防止缓存穿透 - 缓存空值
     * 
     * 缓存穿透：
     * - 查询一个不存在的数据，数据库返回 null
     * - 如果不缓存 null，每次请求都会打到数据库
     * 
     * 解决方案：
     * - 缓存空值（但要设置较短的过期时间）
     * - 或使用布隆过滤器
     * 
     * @param id 数据 ID
     * @return 数据或 null
     */
    @Cacheable(key = "'nullable:' + #id")
    public String getDataAllowNull(Long id) {
        log.info("查询数据（允许缓存空值），ID: {}", id);
        simulateSlowQuery();
        // 模拟数据不存在的情况
        if (id < 0) {
            return null; // 空值也会被缓存
        }
        return "Data-" + id;
    }

    /**
     * 防止缓存雪崩 - 设置随机过期时间
     * 
     * 缓存雪崩：
     * - 大量缓存同时过期，导致大量请求打到数据库
     * 
     * 解决方案：
     * - 设置随机的过期时间（在配置类中实现）
     * - 使用多级缓存
     * - 限流降级
     * 
     * @param category 分类
     * @return 数据列表
     */
    @Cacheable(key = "'category:' + #category")
    public String getCategoryData(String category) {
        log.info("查询分类数据: {}", category);
        simulateSlowQuery();
        return "Category data: " + category;
    }

    /**
     * 热点数据 - 使用 sync 防止缓存击穿
     * 
     * 缓存击穿：
     * - 热点数据的缓存过期时，大量并发请求同时访问
     * - 导致大量请求打到数据库
     * 
     * 解决方案：
     * - 使用 sync = true 实现缓存同步
     * - 热点数据永不过期（定期主动更新）
     * - 使用互斥锁
     * 
     * @param hotKey 热点数据键
     * @return 热点数据
     */
    @Cacheable(key = "'hot:' + #hotKey", sync = true)
    public String getHotData(String hotKey) {
        log.info("查询热点数据: {}", hotKey);
        simulateSlowQuery();
        return "Hot data: " + hotKey;
    }

    /**
     * 模拟慢查询
     */
    private void simulateSlowQuery() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
