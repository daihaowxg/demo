package io.github.daihaowxg.annotation;

import java.lang.annotation.*;
import java.lang.reflect.*;

// 1. 定义一个运行时注解
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@interface MyAnnotation {
    String value();
}

// 2. 使用注解
@MyAnnotation(value = "示例类")
class Example {
    @MyAnnotation(value = "示例字段")
    private String field;

    @MyAnnotation(value = "示例方法")
    public void test() {
        System.out.println("执行 test 方法");
    }
}

// 3. 通过反射获取注解信息
public class AnnotationReflection {
    public static void main(String[] args) throws Exception {
        // 获取类注解
        Class<?> clazz = Example.class;
        if (clazz.isAnnotationPresent(MyAnnotation.class)) {
            System.out.println("类注解: " + clazz.getAnnotation(MyAnnotation.class).value());
        }

        // 获取字段注解
        Field field = clazz.getDeclaredField("field");
        if (field.isAnnotationPresent(MyAnnotation.class)) {
            System.out.println("字段注解: " + field.getAnnotation(MyAnnotation.class).value());
        }

        // 获取方法注解
        Method method = clazz.getMethod("test");
        if (method.isAnnotationPresent(MyAnnotation.class)) {
            System.out.println("方法注解: " + method.getAnnotation(MyAnnotation.class).value());
        }
    }
}