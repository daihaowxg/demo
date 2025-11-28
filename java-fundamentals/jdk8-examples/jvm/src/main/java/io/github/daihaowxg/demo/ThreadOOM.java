package io.github.daihaowxg.demo;

/**
 * 线程数过多：无限创建新线程，耗尽系统资源
 */
public class ThreadOOM {

    /**
     * java -Xmx32m -Xms32m -Xss512k ThreadOOM
     * -Xmx32m -Xms32m 限制堆内存，JVM 资源更容易耗尽。
     * -Xss512k 降低线程栈大小，加快 OOM 触发。
     * -XX:+PrintGCDetails 让你看到 GC 发生情况。
     * -XX:+PrintGCDateStamps 帮助分析 OOM 发生前的 GC 行为。
     */
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