package com.example.demo;

/**
 * 线程数过多：无限创建新线程，耗尽系统资源
 */
public class ThreadOOM {
    public static void main(String[] args) {
        while (true) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000000); // 让线程保持运行状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}