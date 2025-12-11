package io.github.daihaowxg.demo.lab16springcache.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 二级缓存演示 Controller
 */
@Slf4j
@RestController
@RequestMapping("/multilevel")
public class MultiLevelDemoController {

    private final AtomicInteger dbCounter = new AtomicInteger(0);

    /**
     * 获取数据（支持二级缓存）
     * 指定 cacheManager = "multiLevelCacheManager"
     */
    @GetMapping("/{id}")
    @Cacheable(cacheNames = "demo-l2", key = "#id", cacheManager = "multiLevelCacheManager")
    public String getData(@PathVariable String id) {
        log.info("[DB Query] 模拟查询数据库, id={}", id);
        return "Value-" + id + "-Ver" + dbCounter.incrementAndGet();
    }

    /**
     * 更新数据（同时更新 L1 和 L2）
     */
    @PutMapping("/{id}")
    @CachePut(cacheNames = "demo-l2", key = "#id", cacheManager = "multiLevelCacheManager")
    public String updateData(@PathVariable String id) {
        log.info("[DB Update] 模拟更新数据库, id={}", id);
        return "Value-" + id + "-Ver" + dbCounter.incrementAndGet();
    }

    /**
     * 删除数据（同时清除 L1 和 L2）
     */
    @DeleteMapping("/{id}")
    @CacheEvict(cacheNames = "demo-l2", key = "#id", cacheManager = "multiLevelCacheManager")
    public void deleteData(@PathVariable String id) {
        log.info("[DB Delete] 模拟删除数据, id={}", id);
    }
}
