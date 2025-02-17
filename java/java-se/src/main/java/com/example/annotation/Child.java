package com.example.annotation;

class Parent {
    void display() {}
}

public class Child extends Parent {
    @Override  // 编译时检查是否正确重写父类方法
    void display() {
        System.out.println("子类重写 display 方法");
    }
}
