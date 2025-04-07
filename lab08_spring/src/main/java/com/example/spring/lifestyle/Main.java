package com.example.spring.lifestyle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        CustomBean customBean = context.getBean(CustomBean.class);
        customBean.sayHello();
        context.close(); // 关闭容器，触发销毁阶段
    }

    // 1. 调用 CustomBean 的构造方法
    // ============================================================================ 各种 Aware 接口
    // 2. 调用 BeanNameAware 的 setBeanName 方法
    // 3. 调用 BeanFactoryAware 的 setBeanFactory 方法
    // 4. 调用 ApplicationContextAware 的 setApplicationContext 方法
    // ============================================================================ 构造完毕后执行自定义逻辑
    // 5. 调用 @PostConstruct 注解的方法
    // 6. 调用 InitializingBean 的 afterPropertiesSet 方法
    // 7. 自定义初始化方法：customInit 执行
    // ============================================================================ 让 BeanPostProcessor 生效
    // 8. AnotherBean 的构造方法
    // 9. 调用 BeanPostProcessor 的 postProcessBeforeInitialization 方法
    // 10. 调用 BeanPostProcessor 的 postProcessAfterInitialization 方法
    // ============================================================================ 进入使用阶段
    // 11. 调用 CustomBean 的 sayHello 方法
    // ============================================================================ 销毁前执行自定义逻辑
    // 12. 调用 @PreDestroy 注解的方法
    // 13. 调用 DisposableBean 的 destroy 方法
    // 14. 自定义销毁方法：customDestroy 执行
}
