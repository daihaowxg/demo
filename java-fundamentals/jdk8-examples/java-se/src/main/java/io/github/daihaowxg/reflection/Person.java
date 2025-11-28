package io.github.daihaowxg.reflection;

class Person {
    private String name;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("姓名：" + name);
    }

    public void sayHello(String name) {
        System.out.println("Hello, " + name);
    }

    private void secret() {
        System.out.println("这是私有方法");
    }
}
