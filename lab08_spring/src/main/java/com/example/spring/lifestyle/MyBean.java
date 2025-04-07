package com.example.spring.lifestyle;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class MyBean implements BeanNameAware, InitializingBean, DisposableBean {
    private String name;
    private String beanName;

    // 构造方法
    public MyBean() {
        System.out.println("1. MyBean 实例化：构造方法被调用");
    }

    // 属性 setter
    public void setName(String name) {
        this.name = name;
        System.out.println("2. 属性填充：设置 name = " + name);
    }

    // BeanNameAware 接口方法
    @Override
    public void setBeanName(String beanName) {
        this.beanName = beanName;
        System.out.println("3. BeanNameAware：Bean 名称 = " + beanName);
    }

    // InitializingBean 接口方法
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("5. InitializingBean：afterPropertiesSet 方法执行");
    }

    // @PostConstruct 注解方法
    @PostConstruct
    public void postConstruct() {
        System.out.println("4. @PostConstruct：初始化前执行");
    }

    // 自定义初始化方法
    public void customInit() {
        System.out.println("6. 自定义初始化方法：customInit 执行");
    }

    // @PreDestroy 注解方法
    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy：销毁前执行");
    }

    // DisposableBean 接口方法
    @Override
    public void destroy() throws Exception {
        System.out.println("8. DisposableBean：destroy 方法执行");
    }

    // 自定义销毁方法
    public void customDestroy() {
        System.out.println("9. 自定义销毁方法：customDestroy 执行");
    }

    public void sayHello() {
        System.out.println("MyBean [" + beanName + "] 正在使用中，name = " + name);
    }
}
