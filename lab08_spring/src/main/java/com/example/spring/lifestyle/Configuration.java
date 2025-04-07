package com.example.spring.lifestyle;

import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public CustomBean customBean() {
        return new CustomBean();
    }

    @Bean
    public AnotherBean anotherBean() {
        return new AnotherBean();
    }
}

class AnotherBean {
    public AnotherBean() {
        System.out.println("============================================================================ 让 BeanPostProcessor 生效");
        System.out.println("8. AnotherBean 的构造方法");
    }
}