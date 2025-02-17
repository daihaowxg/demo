package com.example.reflection;

import java.lang.reflect.Method;

/**
 * 调用 public 方法
 */
public class ReflectInvokeMethod {
    public static void main(String[] args) throws Exception {
        // 获取 Class 对象
        Class<?> clazz = Class.forName("com.example.reflection.Person");

        // 创建对象
        Object obj = clazz.getDeclaredConstructor().newInstance();

        // 获取方法对象
        Method method = clazz.getMethod("sayHello", String.class);

        // 反射调用方法
        method.invoke(obj, "张三");
    }
}
