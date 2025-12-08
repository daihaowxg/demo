package io.github.daihaowxg.concurrency.basics;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程创建方式示例
 * 
 * @author wxg
 * @since 2025/12/08
 */
public class ThreadCreationExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("=== 线程创建方式示例 ===\n");
        
        // 方式1: 继承 Thread 类
        createThreadByExtending();
        
        // 方式2: 实现 Runnable 接口
        createThreadByRunnable();
        
        // 方式3: 实现 Callable 接口
        createThreadByCallable();
        
        // 方式4: 使用 Lambda 表达式
        createThreadByLambda();
    }
    
    /**
     * 方式1: 继承 Thread 类
     */
    private static void createThreadByExtending() {
        Thread thread = new MyThread("Thread-Extend");
        thread.start();
        System.out.println("方式1: 继承 Thread 类 - 线程已启动\n");
    }
    
    /**
     * 方式2: 实现 Runnable 接口
     */
    private static void createThreadByRunnable() {
        Runnable runnable = new MyRunnable("Thread-Runnable");
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("方式2: 实现 Runnable 接口 - 线程已启动\n");
    }
    
    /**
     * 方式3: 实现 Callable 接口
     */
    private static void createThreadByCallable() throws ExecutionException, InterruptedException {
        Callable<String> callable = new MyCallable("Thread-Callable");
        FutureTask<String> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask);
        thread.start();
        
        // 获取返回值（会阻塞直到任务完成）
        String result = futureTask.get();
        System.out.println("方式3: 实现 Callable 接口 - 返回值: " + result + "\n");
    }
    
    /**
     * 方式4: 使用 Lambda 表达式（推荐）
     */
    private static void createThreadByLambda() {
        Thread thread = new Thread(() -> {
            System.out.println("方式4: Lambda 表达式 - 线程 " + Thread.currentThread().getName() + " 正在执行");
        }, "Thread-Lambda");
        thread.start();
        System.out.println("方式4: 使用 Lambda 表达式 - 线程已启动\n");
    }
    
    /**
     * 继承 Thread 类的方式
     */
    static class MyThread extends Thread {
        public MyThread(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            System.out.println("MyThread - 线程 " + getName() + " 正在执行");
        }
    }
    
    /**
     * 实现 Runnable 接口的方式
     */
    static class MyRunnable implements Runnable {
        private final String name;
        
        public MyRunnable(String name) {
            this.name = name;
        }
        
        @Override
        public void run() {
            System.out.println("MyRunnable - 线程 " + name + " 正在执行");
        }
    }
    
    /**
     * 实现 Callable 接口的方式
     */
    static class MyCallable implements Callable<String> {
        private final String name;
        
        public MyCallable(String name) {
            this.name = name;
        }
        
        @Override
        public String call() throws Exception {
            System.out.println("MyCallable - 线程 " + name + " 正在执行");
            Thread.sleep(100); // 模拟耗时操作
            return "任务完成: " + name;
        }
    }
}
