package com.example.spring.lifestyle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // 创建 Spring 容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 获取 Bean 并使用
        MyBean myBean = context.getBean(MyBean.class);
        myBean.sayHello();

        // 关闭容器，触发销毁
        context.close();
    }

    // 1. MyBean 实例化：构造方法被调用
    // 2. 属性填充：设置 name = TestBean
    // 3. BeanNameAware：Bean 名称 = myBean
    // 4. @PostConstruct：初始化前执行
    // 5. InitializingBean：afterPropertiesSet 方法执行
    // 6. 自定义初始化方法：customInit 执行
    //     MyBean [myBean] 正在使用中，name = TestBean
    // 7. @PreDestroy：销毁前执行
    // 8. DisposableBean：destroy 方法执行
    // 9. 自定义销毁方法：customDestroy 执行
}
