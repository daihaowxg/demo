package io.github.daihaowxg.demo.interrupt;

public class InterruptDemo {
    // 子线程启动...
    // 主线程中断子线程...
    // 子线程检测到中断标志（isInterrupted = true），准备退出
    // 子线程结束...
    // 主线程结束...
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("子线程启动...");

            // 一直循环，直到中断
            while (true) {
                // 检查是否被中断，不会清除标志位
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("子线程检测到中断标志（isInterrupted = true），准备退出");
                    break;
                }

                // 使用 interrupted() 会清除中断状态，只会打印一次
                if (Thread.interrupted()) {
                    System.out.println("子线程第一次检测到中断（interrupted = true），并清除了中断标志");
                }
            }

            System.out.println("子线程结束...");
        });

        t.start();

        Thread.sleep(1000); // 主线程休眠 1 秒，确保子线程已启动

        System.out.println("主线程中断子线程...");
        t.interrupt(); // 设置中断标志位

        t.join(); // 等待子线程结束

        System.out.println("主线程结束...");
    }
}