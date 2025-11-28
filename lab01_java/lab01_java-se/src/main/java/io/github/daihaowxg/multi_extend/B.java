package io.github.daihaowxg.multi_extend;

public interface B {
    default void show() {
        System.out.println("show B");
    }
}
