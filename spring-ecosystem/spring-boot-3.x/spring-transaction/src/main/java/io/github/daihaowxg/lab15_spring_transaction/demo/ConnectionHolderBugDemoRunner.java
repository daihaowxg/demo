package io.github.daihaowxg.lab15_spring_transaction.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * ConnectionHolder Bug 演示运行器
 * 
 * 这个类实现了 CommandLineRunner 接口
 * 会在 Spring Boot 应用启动完成后自动执行
 * 
 * 演示内容：
 * 1. 三种常见的 bug 场景
 * 2. 正确的使用方式
 * 3. Spring 内部机制的详细展示
 * 
 * @author daihaowxg
 */
@Component
public class ConnectionHolderBugDemoRunner implements CommandLineRunner {

    @Autowired
    private ConnectionHolderBugDemoService demoService;

    @Autowired
    private ConnectionHolderInternalDemo internalDemo;

    /**
     * 应用启动后自动执行的方法
     * 
     * 执行顺序：
     * 1. Bug 场景演示 - 展示常见错误
     * 2. 正确用法演示 - 展示最佳实践
     * 3. 内部机制演示 - 深入理解 Spring 事务管理
     * 
     * @param args 命令行参数
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("Connection Holder Bug 演示");
        System.out.println("========================================\n");

        // Bug 场景 1: 无事务上下文
        // 演示在没有 @Transactional 注解时访问连接的问题
        System.out.println("--- Bug 场景 1: 在没有事务的情况下访问连接 ---");
        demoService.bugCase1_NoTransaction();
        System.out.println();

        // Bug 场景 2: 不同线程访问
        // 演示 ThreadLocal 特性导致的跨线程访问问题
        System.out.println("--- Bug 场景 2: 在不同线程中访问连接 ---");
        demoService.bugCase2_DifferentThread();
        System.out.println();

        // Bug 场景 3: 事务完成后访问
        // 演示事务生命周期结束后访问连接的问题
        System.out.println("--- Bug 场景 3: 在事务完成后使用连接 ---");
        demoService.bugCase3_StoreConnection();
        Thread.sleep(100); // 确保事务已经完成
        demoService.bugCase3_UseStoredConnection();
        System.out.println();

        // 正确用法
        // 演示如何在事务上下文中正确访问连接
        System.out.println("--- 正确用法: 在事务中访问连接 ---");
        demoService.correctUsage();
        System.out.println();

        // 内部机制演示 1: Null ConnectionHolder
        // 深入展示 TransactionSynchronizationManager 在无事务时的状态
        System.out.println("--- 内部机制 1: 事务外的 Null ConnectionHolder ---");
        internalDemo.demonstrateNullConnectionHolder();
        System.out.println();

        // 内部机制演示 2: Valid ConnectionHolder
        // 展示事务中可以获取的所有上下文信息
        System.out.println("--- 内部机制 2: 事务中的 Valid ConnectionHolder ---");
        internalDemo.demonstrateValidConnectionHolder();
        System.out.println();

        // 内部机制演示 3: 丢失的事务上下文
        // 演示事务上下文的生命周期和 ThreadLocal 的清理
        System.out.println("--- 内部机制 3: 丢失的事务上下文 ---");
        Runnable runnable = internalDemo.createRunnableWithTransactionContext();
        Thread.sleep(100); // 确保事务已经完成
        internalDemo.executeRunnable(runnable);
        System.out.println();

        System.out.println("========================================");
        System.out.println("演示完成");
        System.out.println("========================================\n");
    }
}
