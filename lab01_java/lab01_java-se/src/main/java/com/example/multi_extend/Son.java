package com.example.multi_extend;

public class Son implements Father, Mother {
    @Override
    public void show() {
        System.out.println("这里实现类必须实现 show() 方法，否则会编译报错");
    }
}
