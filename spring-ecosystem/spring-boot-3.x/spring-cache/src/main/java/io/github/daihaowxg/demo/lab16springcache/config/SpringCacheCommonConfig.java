package io.github.daihaowxg.demo.lab16springcache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cache 通用配置类
 * 存放所有缓存实现（Caffeine, Redis, MultiLevel）共用的配置
 */
@Configuration
@EnableCaching // 启用 Spring Cache 支持
public class SpringCacheCommonConfig {

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
}
