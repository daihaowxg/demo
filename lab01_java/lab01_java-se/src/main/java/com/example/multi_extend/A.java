package com.example.multi_extend;

public interface A {
    default void show() {
        System.out.println("show A");
    }
}
