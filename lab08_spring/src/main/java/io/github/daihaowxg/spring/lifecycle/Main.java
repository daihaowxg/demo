package io.github.daihaowxg.spring.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Configuration.class);
        CustomBean customBean = context.getBean(CustomBean.class);
        customBean.sayHello();
        context.close(); // 关闭容器，触发销毁阶段
    }

    // ============================================================================ 实例化：调用构造方法
    // 1. 调用 CustomBean 的构造方法
    // ============================================================================ 属性填充：注入依赖
    // ============================================================================ Aware 接口回调
    // 2. 调用 BeanNameAware 的 setBeanName 方法
    // 3. 调用 BeanFactoryAware 的 setBeanFactory 方法
    // 4. 调用 ApplicationContextAware 的 setApplicationContext 方法
    // ============================================================================ 前置处理
    // 5. 调用 BeanPostProcessor 的 postProcessBeforeInitialization 方法
    // ============================================================================ 初始化
    // 6. 调用 @PostConstruct 注解的方法
    // 7. 调用 InitializingBean 的 afterPropertiesSet 方法
    // 8. 自定义初始化方法：customInit 执行
    // ============================================================================ 后置处理
    // 9. 调用 BeanPostProcessor 的 postProcessAfterInitialization 方法
    // ============================================================================ 使用阶段
    // 10. 调用 CustomBean 的 sayHello 方法
    // ============================================================================ 销毁阶段
    // 11. 调用 @PreDestroy 注解的方法
    // 12. 调用 DisposableBean 的 destroy 方法
    // 13. 自定义销毁方法：customDestroy 执行
}
