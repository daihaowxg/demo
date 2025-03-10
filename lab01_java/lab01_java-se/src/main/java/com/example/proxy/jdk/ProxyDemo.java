package com.example.proxy.jdk;

import java.lang.reflect.Proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        UserService target = new UserServiceImpl();

        // 生成代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new UserServiceProxyHandler(target)
        );

        proxy.login("Alice"); // 调用代理方法
    }
}