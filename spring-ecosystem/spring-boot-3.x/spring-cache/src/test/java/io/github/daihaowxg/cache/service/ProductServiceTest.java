package io.github.daihaowxg.cache.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductService 单元测试类
 * <p>
 * 本通常用于验证 Spring Cache 高级特性，包括条件缓存、结果排除等。
 * 同样使用 Caffeine 作为测试环境的缓存实现。
 * <p>
 * 测试涵盖的特性：
 * <ul>
 * <li>{@code condition} 属性: 调用前判断，决定是否启用缓存机制</li>
 * <li>{@code unless} 属性: 调用后判断，决定是否将结果放入缓存</li>
 * </ul>
 */
@SpringBootTest
@TestPropertySource(properties = "spring.cache.type=caffeine")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager; // 这里会根据配置注入，默认为 CaffeineCacheManager

    /**
     * 测试前置准备
     * <p>
     * 每次测试前清空 "products" 缓存，保证测试环境干净。
     */
    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache("products");
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 测试 condition 属性 (条件缓存)
     * <p>
     * 场景说明：
     * ProductService.getProductById 方法上定义了 condition = "#id > 0"。
     * 只有当传入的參數 id 大于 0 时，Spring Cache 才会介入（查询缓存或写入缓存）。
     * <p>
     * 验证点：
     * 1. 传入 id=1 (满足条件) -> 执行后，结果应该被放入缓存。
     * 2. 传入 id=-1 (不满足条件) -> 执行后，结果不应该出现在缓存中。
     */
    @Test
    void testCondition() {
        // case 1: ID > 0, 满足 condition 条件
        Long validId = 1L;
        // 方法执行后，结果被缓存，并在控制台打印：从数据库查询商品，ID: 1
        productService.getProductById(validId);

        // 验证缓存中有数据
        Cache cache = cacheManager.getCache("products");
        assertThat(cache).isNotNull();
        // key 默认为参数值，即 1L
        assertThat(cache.get(validId)).isNotNull();

        // case 2: ID <= 0, 不满足 condition 条件
        // 预期行为：Spring Cache 完全不介入，方法正常执行，但结果不会存入缓存
        Long invalidId = -1L;

        // 调用业务方法 (database.get(-1) 返回 null，这是符合预期的业务行为)
        try {
            // 控制台打印：从数据库查询商品，ID: -1
            productService.getProductById(invalidId);
        } catch (Exception ignored) {
            // 忽略业务异常，只关注缓存行为
        }

        // 验证缓存中确实没有 key=-1 的数据
        assertThat(cache.get(invalidId)).isNull();
    }

    /**
     * 测试 unless 属性 (结果排除)
     * <p>
     * 场景说明：
     * ProductService.getProductByIdWithNullCheck 方法上定义了 unless = "#result == null"。
     * 这是一个"否决"条件：如果表达式为 true，则**不**缓存。
     * 也就是说：当返回值为 null 时，不进行缓存；返回值非 null 时，才缓存。
     * <p>
     * 验证点：
     * 1. 查询存在的 ID (返回非空对象) -> 应该被缓存。
     * 2. 查询不存在的 ID (返回 null) -> 不应该被缓存 (避免缓存穿透或缓存无效的 null 值)。
     */
    @Test
    void testUnless() {
        // case 1: 结果不为 null，应该缓存
        // 我们用一个存在的 ID (database 中预置了 ID=2 的数据)
        Long existId = 2L;
        // 控制台打印：从数据库查询商品（带空值检查），ID: 2
        productService.getProductByIdWithNullCheck(existId);

        Cache cache = cacheManager.getCache("products");
        assertThat(cache).isNotNull();

        // 注意：该方法自定义了 key = "'product:' + #id"
        // 所以缓存 key 是 "product:2" 而不是 2L
        assertThat(cache.get("product:" + existId)).isNotNull();

        // case 2: 结果为 null，不应该缓存 (因为 unless = "#result == null" 成立)
        // 用一个不存在的 ID，业务方法会返回 null
        Long notExistId = 999L;
        productService.getProductByIdWithNullCheck(notExistId);

        // 验证缓存中没有 key="product:999" 的数据
        assertThat(cache.get("product:" + notExistId)).isNull();
    }
}
