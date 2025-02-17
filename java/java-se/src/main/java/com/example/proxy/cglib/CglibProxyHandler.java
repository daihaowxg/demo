package com.example.proxy.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.Enhancer;
import java.lang.reflect.Method;

public class CglibProxyHandler implements MethodInterceptor {
    private final Object target;

    public CglibProxyHandler(Object target) {
        this.target = target;
    }

    public Object createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("日志记录：调用方法 " + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("方法执行完毕");
        return result;
    }
}
