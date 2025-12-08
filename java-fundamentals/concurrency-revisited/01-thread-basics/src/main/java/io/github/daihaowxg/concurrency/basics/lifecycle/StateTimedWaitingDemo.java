package io.github.daihaowxg.concurrency.basics.lifecycle;

import java.util.concurrent.TimeUnit;

/**
 * 演示 TIMED_WAITING 状态
 * 
 * 场景：
 * 线程 A 调用 Thread.sleep(time) 进入限时等待
 */
public class StateTimedWaitingDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 状态演示: TIMED_WAITING ===\n");

        Thread t = new Thread(() -> {
            try {
                System.out.println("   [线程内] 准备睡眠 2秒...");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("   [线程内] 睡醒了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Timed-Thread");

        t.start();

        // 确保线程已经启动并开始 sleep
        TimeUnit.MILLISECONDS.sleep(100);

        System.out.println("线程正在 sleep(2000), 当前状态: " + t.getState());
    }
}
