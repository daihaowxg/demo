package com.example.reflection;

import java.lang.reflect.Method;

/**
 * 调用 private 方法
 */
public class ReflectPrivateMethod {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.example.reflection.Person");
        Object obj = clazz.getDeclaredConstructor().newInstance();

        // 获取私有方法
        Method method = clazz.getDeclaredMethod("secret");

        // 设置可访问（绕过 private 限制）
        method.setAccessible(true);

        // 反射调用
        method.invoke(obj);
    }
}
