package io.github.daihaowxg.cache.controller;

import io.github.daihaowxg.cache.entity.Product;
import io.github.daihaowxg.cache.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品控制器 - 测试列表缓存和条件缓存
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 根据 ID 查询商品
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * 根据 ID 查询商品（带空值检查）
     */
    @GetMapping("/{id}/null-check")
    public Product getProductByIdWithNullCheck(@PathVariable Long id) {
        return productService.getProductByIdWithNullCheck(id);
    }

    /**
     * 根据分类查询商品列表
     * 演示列表缓存
     */
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    /**
     * 根据价格区间查询商品
     * 演示复杂的缓存 key
     */
    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }

    /**
     * 组合条件查询
     * 演示 condition 和 unless 同时使用
     */
    @GetMapping("/combo")
    public List<Product> getProductsByCategoryAndPrice(
            @RequestParam String category,
            @RequestParam BigDecimal maxPrice) {
        return productService.getProductsByCategoryAndPrice(category, maxPrice);
    }

    /**
     * 获取所有商品（不走缓存）
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
}
