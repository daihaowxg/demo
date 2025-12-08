package io.github.daihaowxg.concurrency.basics.lifecycle;

/**
 * 演示 NEW -> RUNNABLE -> TERMINATED 状态转换
 * 
 * 流程：
 * 1. 创建线程 -> NEW
 * 2. 启动线程 -> RUNNABLE
 * 3. 线程执行完毕 -> TERMINATED
 */
public class StateNewRunnableTerminated {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 基础状态演示: NEW -> RUNNABLE -> TERMINATED ===\n");

        Thread t = new Thread(() -> {
            System.out.println("   [线程内] 正在执行任务...");
            // 可以在这里模拟一些计算
            long sum = 0;
            for (int i = 0; i < 1000; i++)
                sum += i;
            System.out.println("   [线程内] 计算结果: " + sum);
        }, "Basic-Thread");

        // 1. NEW
        System.out.println("1. 创建后状态: " + t.getState());

        // 2. RUNNABLE
        t.start();
        // 给一点点时间让线程启动并进入运行状态
        // 注意：即使线程正在运行，状态也是 RUNNABLE（JVM 中没有 Running 状态，Running 被包含在 RUNNABLE 中）
        System.out.println("2. start()后状态: " + t.getState());

        t.join(); // 等待线程结束

        // 3. TERMINATED
        System.out.println("3. 结束后状态: " + t.getState());
    }
}
