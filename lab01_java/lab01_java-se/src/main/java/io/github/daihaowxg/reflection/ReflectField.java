package io.github.daihaowxg.reflection;

import java.lang.reflect.Field;

/**
 * 修改 public 字段
 */
public class ReflectField {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("com.example.reflection.Person2");
        Object obj = clazz.getDeclaredConstructor().newInstance();

        // 获取字段
        Field field = clazz.getField("name");

        // 获取字段值
        System.out.println("修改前：" + field.get(obj));

        // 修改字段值
        field.set(obj, "张三");

        // 获取修改后的值
        System.out.println("修改后：" + field.get(obj));
    }
}


class Person2 {
    public String name = "默认值";
}
