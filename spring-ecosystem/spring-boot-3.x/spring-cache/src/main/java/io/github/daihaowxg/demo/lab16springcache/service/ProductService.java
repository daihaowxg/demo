package io.github.daihaowxg.demo.lab16springcache.service;

import io.github.daihaowxg.demo.lab16springcache.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品服务类 - 演示条件缓存和自定义 Key
 * 
 * 主要演示：
 * 1. 条件缓存（condition）
 * 2. 排除缓存（unless）
 * 3. 列表缓存
 * 4. 复杂的 SpEL 表达式
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "products")
public class ProductService {

    /**
     * 模拟数据库存储
     */
    private final Map<Long, Product> database = new HashMap<>();

    public ProductService() {
        // 初始化一些测试数据
        database.put(1L, new Product(1L, "iPhone 15", new BigDecimal("5999"), "电子产品"));
        database.put(2L, new Product(2L, "MacBook Pro", new BigDecimal("12999"), "电子产品"));
        database.put(3L, new Product(3L, "AirPods Pro", new BigDecimal("1999"), "电子产品"));
        database.put(4L, new Product(4L, "Java 编程思想", new BigDecimal("99"), "图书"));
        database.put(5L, new Product(5L, "Spring 实战", new BigDecimal("79"), "图书"));
    }

    /**
     * 根据 ID 查询商品 - 演示条件缓存
     * 
     * condition 属性说明：
     * - 在方法执行前判断，决定是否使用缓存
     * - #id > 0：只有当 id 大于 0 时才使用缓存
     * - 如果条件不满足，每次都会执行方法体
     * 
     * @param id 商品 ID
     * @return 商品信息
     */
    @Cacheable(key = "#id", condition = "#id > 0")
    public Product getProductById(Long id) {
        log.info("从数据库查询商品，ID: {}", id);
        simulateSlowQuery();
        return database.get(id);
    }

    /**
     * 根据 ID 查询商品 - 演示 unless 排除缓存
     * 
     * unless 属性说明：
     * - 在方法执行后判断，决定是否缓存结果
     * - #result == null：如果返回值为 null，则不缓存
     * - 适用于避免缓存空值
     * 
     * @param id 商品 ID
     * @return 商品信息
     */
    @Cacheable(key = "'product:' + #id", unless = "#result == null")
    public Product getProductByIdWithNullCheck(Long id) {
        log.info("从数据库查询商品（带空值检查），ID: {}", id);
        simulateSlowQuery();
        return database.get(id);
    }

    /**
     * 根据分类查询商品列表 - 演示列表缓存
     * 
     * @param category 商品分类
     * @return 商品列表
     */
    @Cacheable(key = "'category:' + #category")
    public List<Product> getProductsByCategory(String category) {
        log.info("从数据库查询商品列表，分类: {}", category);
        simulateSlowQuery();
        return database.values().stream()
                .filter(product -> product.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * 根据价格区间查询商品 - 演示复杂的缓存 key
     * 
     * SpEL 表达式说明：
     * - 'price:' + #minPrice + '-' + #maxPrice
     * - 最终 key 格式：price:100-1000
     * 
     * @param minPrice 最低价格
     * @param maxPrice 最高价格
     * @return 商品列表
     */
    @Cacheable(key = "'price:' + #minPrice + '-' + #maxPrice")
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("从数据库查询商品，价格区间: {} - {}", minPrice, maxPrice);
        simulateSlowQuery();
        return database.values().stream()
                .filter(product -> product.getPrice().compareTo(minPrice) >= 0
                        && product.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    /**
     * 组合条件查询 - 演示 condition 和 unless 同时使用
     * 
     * @param category 分类
     * @param maxPrice 最高价格
     * @return 商品列表
     */
    @Cacheable(key = "'combo:' + #category + ':' + #maxPrice", condition = "#category != null && #maxPrice != null", // 参数不为空才缓存
            unless = "#result.isEmpty()" // 结果为空不缓存
    )
    public List<Product> getProductsByCategoryAndPrice(String category, BigDecimal maxPrice) {
        log.info("组合查询商品，分类: {}, 最高价格: {}", category, maxPrice);
        simulateSlowQuery();
        return database.values().stream()
                .filter(product -> product.getCategory().equals(category)
                        && product.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有商品（不缓存）
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(database.values());
    }

    /**
     * 模拟慢查询
     */
    private void simulateSlowQuery() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
