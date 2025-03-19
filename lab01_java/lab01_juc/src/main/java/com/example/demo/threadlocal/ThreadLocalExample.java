package com.example.demo.threadlocal;

/**
 * @author wxg
 * @since 2025/3/14
 */
public class ThreadLocalExample {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            MyRunnable runnable = new MyRunnable("user" + i);
            new Thread(runnable).start();
        }
    }
}
