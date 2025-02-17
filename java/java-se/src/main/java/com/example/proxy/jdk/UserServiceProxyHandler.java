package com.example.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserServiceProxyHandler implements InvocationHandler {
    private final Object target; // 被代理的对象

    public UserServiceProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("日志记录：调用方法 " + method.getName());
        Object result = method.invoke(target, args); // 调用目标方法
        System.out.println("方法执行完毕");
        return result;
    }
}