package com.example.demo;

/**
 * 栈溢出：递归调用没有终止条件，导致栈空间耗尽
 */
public class StackOverflowTest {
    public static void recursiveMethod() {
        recursiveMethod(); // 无限递归
    }

    /**
     * java -Xss256k StackOverflowTest
     */
    public static void main(String[] args) {
        recursiveMethod();
    }
}
