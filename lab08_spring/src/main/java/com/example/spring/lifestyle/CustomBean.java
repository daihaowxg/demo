package com.example.spring.lifestyle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class CustomBean  implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, BeanPostProcessor, InitializingBean, DisposableBean {
    public CustomBean() {
        System.out.println("1. 调用 CustomBean 的构造方法");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("============================================================================ 各种 Aware 接口");
        System.out.println("2. 调用 BeanNameAware 的 setBeanName 方法");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("3. 调用 BeanFactoryAware 的 setBeanFactory 方法");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("4. 调用 ApplicationContextAware 的 setApplicationContext 方法");
    }

    @PostConstruct
    public void init() {
        System.out.println("============================================================================ 构造完毕后执行自定义逻辑");
        System.out.println("5. 调用 @PostConstruct 注解的方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("6. 调用 InitializingBean 的 afterPropertiesSet 方法");
    }

    public void customInit() {
        System.out.println("7. 自定义初始化方法：customInit 执行");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("9. 调用 BeanPostProcessor 的 postProcessBeforeInitialization 方法");
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("10. 调用 BeanPostProcessor 的 postProcessAfterInitialization 方法");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    public void sayHello() {
        System.out.println("============================================================================ 进入使用阶段");
        System.out.println("11. 调用 CustomBean 的 sayHello 方法");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("============================================================================ 销毁前执行自定义逻辑");
        System.out.println("12. 调用 @PreDestroy 注解的方法");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("13. 调用 DisposableBean 的 destroy 方法");
    }

    public void customDestroy() {
        System.out.println("14. 自定义销毁方法：customDestroy 执行");
    }
}


