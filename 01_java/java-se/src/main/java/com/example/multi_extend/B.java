package com.example.multi_extend;

public interface B {
    default void show() {
        System.out.println("show B");
    }
}
