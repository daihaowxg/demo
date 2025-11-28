package io.github.daihaowxg.demo.semaphore;

import java.util.concurrent.Semaphore;

/**
 * 最多只有 3 个线程可以同时访问资源，其他线程需要等待许可证释放。
 * @author wxg
 * @since 2025/3/19
 */
public class SemaphoreExample {
    public static void main(String[] args) {
        // 创建一个 Semaphore，允许最多 3 个线程同时访问
        Semaphore semaphore = new Semaphore(3);

        // 创建 10 个线程，模拟资源访问
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                try {
                    // 获取许可证（阻塞等待）
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " 获取许可证，正在访问资源...");

                    // 模拟资源访问
                    Thread.sleep(2000);

                    System.out.println(Thread.currentThread().getName() + " 释放许可证，资源访问结束。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // 释放许可证
                    semaphore.release();
                }
            }, "Thread-" + i).start();
        }

        /*
         * 线程 1、2、3获取到许可证，开始执行相关操作
         * 线程 1、2 相关操作执行完成，释放许可证
         * 线程 4、6 获得了 1、2 释放的许可证，开始执行相关操作
         * 线程 3 释放了许可证，线程 5 获得许可证
         * 线程 4 释放了许可证，线程 7 获得许可证
         * 线程 6、5 释放了许可证，线程 8、9 获得许可证
         * 线程 7、8 释放了许可证，线程 10 获得许可证
         * 线程 9 释放了许可证
         * 线程 10 释放了许可证
         */
        // Thread-1 获取许可证，正在访问资源...
        // Thread-2 获取许可证，正在访问资源...
        // Thread-3 获取许可证，正在访问资源...
        // Thread-1 释放许可证，资源访问结束。
        // Thread-2 释放许可证，资源访问结束。
        // Thread-4 获取许可证，正在访问资源...
        // Thread-6 获取许可证，正在访问资源...
        // Thread-3 释放许可证，资源访问结束。
        // Thread-5 获取许可证，正在访问资源...
        // Thread-4 释放许可证，资源访问结束。
        // Thread-7 获取许可证，正在访问资源...
        // Thread-6 释放许可证，资源访问结束。
        // Thread-5 释放许可证，资源访问结束。
        // Thread-8 获取许可证，正在访问资源...
        // Thread-9 获取许可证，正在访问资源...
        // Thread-7 释放许可证，资源访问结束。
        // Thread-8 释放许可证，资源访问结束。
        // Thread-10 获取许可证，正在访问资源...
        // Thread-9 释放许可证，资源访问结束。
        // Thread-10 释放许可证，资源访问结束。
    }
}
