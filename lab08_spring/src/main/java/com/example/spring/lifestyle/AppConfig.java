package com.example.spring.lifestyle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    public MyBean myBean() {
        MyBean bean = new MyBean();
        bean.setName("TestBean");
        return bean;
    }
}
