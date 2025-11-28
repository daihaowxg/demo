package io.github.daihaowxg.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 通过 Constructor 创建对象
 */
public class ReflectCreateObject {
    public static void main(String[] args) throws Exception {
        // 获取 Class 对象
        Class<?> clazz = Class.forName("com.example.reflection.Person");

        // 获取带参数的构造方法
        Constructor<?> constructor = clazz.getConstructor(String.class);

        // 通过构造方法创建对象
        Object obj = constructor.newInstance("张三");

        // 调用方法
        Method method = clazz.getMethod("show");
        method.invoke(obj);
    }
}

