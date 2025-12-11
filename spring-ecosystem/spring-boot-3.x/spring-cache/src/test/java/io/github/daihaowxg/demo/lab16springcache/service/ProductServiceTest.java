package io.github.daihaowxg.demo.lab16springcache.service;

import io.github.daihaowxg.demo.lab16springcache.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = "spring.cache.type=redis")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager; // 这里会根据配置注入，默认为 CaffeineCacheManager

    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache("products");
        if (cache != null) {
            cache.clear();
        }
    }

    @Test
    void testCondition() {
        // case 1: ID > 0, 应该缓存
        Long validId = 1L;
        productService.getProductById(validId);

        Cache cache = cacheManager.getCache("products");
        assertThat(cache.get(validId)).isNotNull();

        // case 2: ID <= 0, 不应该缓存
        Long invalidId = -1L;
        // 注意：这里需要确保 database 里没有 -1 的数据不会报错，或者我们主要验证它不进缓存即可
        // 实际上 database.get(-1) 会返回 null，但 condition 的判断是在调用前。
        // 原代码逻辑：condition = "#id > 0"。
        try {
            productService.getProductById(invalidId);
        } catch (Exception ignored) {
        }

        assertThat(cache.get(invalidId)).isNull();
    }

    @Test
    void testUnless() {
        // case 1: 结果不为 null，应该缓存
        // 我们用一个存在的 ID
        Long existId = 2L;
        productService.getProductByIdWithNullCheck(existId);

        Cache cache = cacheManager.getCache("products");
        // key pattern: 'product:' + id
        assertThat(cache.get("product:" + existId)).isNotNull();

        // case 2: 结果为 null，不应该缓存
        // 用一个不存在的 ID
        Long notExistId = 999L;
        productService.getProductByIdWithNullCheck(notExistId);

        assertThat(cache.get("product:" + notExistId)).isNull();
    }
}
