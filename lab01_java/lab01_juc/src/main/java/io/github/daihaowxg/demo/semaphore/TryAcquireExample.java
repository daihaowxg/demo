package io.github.daihaowxg.demo.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @author wxg
 * @since 2025/3/19
 */
public class TryAcquireExample {
    public static void main(String[] args) {
        // 创建一个 Semaphore，允许最多 2 个线程同时访问
        Semaphore semaphore = new Semaphore(2);

        // 创建 5 个线程
        for (int i = 1; i <= 5; i++) {
            new Thread(() -> {
                if (semaphore.tryAcquire()) {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 获取许可证，正在访问资源...");
                        Thread.sleep(2000); // 模拟资源访问
                        System.out.println(Thread.currentThread().getName() + " 释放许可证，资源访问结束。");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        semaphore.release();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " 未能获取许可证，执行其他逻辑...");
                }
            }, "Thread-" + i).start();
        }

        /*
         * Thread-1 和 Thread-2 获取到许可证，并在访问资源后释放许可证。
         * Thread-3、Thread-4、Thread-5 未能获取许可证，执行其他逻辑。
         */

        // Thread-1 获取许可证，正在访问资源...
        // Thread-3 未能获取许可证，执行其他逻辑...
        // Thread-2 获取许可证，正在访问资源...
        // Thread-4 未能获取许可证，执行其他逻辑...
        // Thread-5 未能获取许可证，执行其他逻辑...
        // Thread-1 释放许可证，资源访问结束。
        // Thread-2 释放许可证，资源访问结束。
    }
}