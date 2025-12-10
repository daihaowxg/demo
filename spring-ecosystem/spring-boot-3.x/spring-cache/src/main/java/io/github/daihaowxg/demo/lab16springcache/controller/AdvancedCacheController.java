package io.github.daihaowxg.demo.lab16springcache.controller;

import io.github.daihaowxg.demo.lab16springcache.service.AdvancedCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 进阶缓存控制器 - 测试高级缓存特性
 */
@RestController
@RequestMapping("/advanced")
@RequiredArgsConstructor
public class AdvancedCacheController {

    private final AdvancedCacheService advancedCacheService;

    /**
     * 测试缓存同步
     * 可以使用多个并发请求测试 sync = true 的效果
     */
    @GetMapping("/expensive/{key}")
    public String getExpensiveData(@PathVariable String key) {
        return advancedCacheService.getExpensiveData(key);
    }

    /**
     * 测试自定义 KeyGenerator
     */
    @GetMapping("/custom-key")
    public String getDataWithCustomKey(
            @RequestParam String param1,
            @RequestParam Integer param2) {
        return advancedCacheService.getDataWithCustomKey(param1, param2);
    }

    /**
     * 测试缓存空值（防止缓存穿透）
     */
    @GetMapping("/nullable/{id}")
    public String getDataAllowNull(@PathVariable Long id) {
        return advancedCacheService.getDataAllowNull(id);
    }

    /**
     * 测试分类数据缓存
     */
    @GetMapping("/category/{category}")
    public String getCategoryData(@PathVariable String category) {
        return advancedCacheService.getCategoryData(category);
    }

    /**
     * 测试热点数据（防止缓存击穿）
     */
    @GetMapping("/hot/{hotKey}")
    public String getHotData(@PathVariable String hotKey) {
        return advancedCacheService.getHotData(hotKey);
    }
}
