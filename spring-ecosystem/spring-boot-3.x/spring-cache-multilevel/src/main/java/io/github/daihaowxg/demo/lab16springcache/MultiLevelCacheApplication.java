package io.github.daihaowxg.demo.lab16springcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MultiLevelCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiLevelCacheApplication.class, args);
    }
}
