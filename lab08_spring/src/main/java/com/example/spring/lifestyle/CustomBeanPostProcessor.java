package com.example.spring.lifestyle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("============================================================================ 前置处理");
        System.out.println("5. 调用 BeanPostProcessor 的 postProcessBeforeInitialization 方法");
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("============================================================================ 后置处理");
        System.out.println("9. 调用 BeanPostProcessor 的 postProcessAfterInitialization 方法");
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
