package io.github.daihaowxg.demo.reentrantreadwritelock;

/**
 * 多个线程同时读取和写入数据
 * @author wxg
 * @since 2025/3/19
 */
public class MultiThreadExample {
    private static final ReentrantReadWriteLockExample example = new ReentrantReadWriteLockExample();

    public static void main(String[] args) {
        // 创建多个读线程
        Runnable reader = () -> {
            for (int i = 0; i < 3; i++) {
                example.read();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // 创建一个写线程
        Runnable writer = () -> {
            for (int i = 0; i < 3; i++) {
                example.write(i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        // 启动线程
        Thread t1 = new Thread(reader, "Reader-1");
        Thread t2 = new Thread(reader, "Reader-2");
        Thread t3 = new Thread(writer, "Writer");

        t1.start();
        t2.start();
        t3.start();
    }
}
