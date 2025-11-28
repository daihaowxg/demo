package io.github.daihaowxg.spring.lifecycle;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public CustomBean customBean() {
        return new CustomBean();
    }

    @Bean
    public CustomBeanPostProcessor customBeanPostProcessor() {
        return new CustomBeanPostProcessor();
    }
}