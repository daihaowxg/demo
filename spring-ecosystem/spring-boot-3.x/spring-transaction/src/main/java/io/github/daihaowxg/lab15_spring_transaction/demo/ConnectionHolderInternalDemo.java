package io.github.daihaowxg.lab15_spring_transaction.demo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * ConnectionHolder 内部机制演示
 * 
 * 演示 Spring 事务管理的内部实现机制
 * 重点展示 TransactionSynchronizationManager 的工作原理
 * 
 * 核心概念：
 * - TransactionSynchronizationManager: Spring 事务同步管理器
 * - ThreadLocal: 线程本地存储，用于存储事务上下文
 * - ConnectionHolder: 连接持有者，包装数据库连接
 * 
 * @author daihaowxg
 */
@Service
public class ConnectionHolderInternalDemo {

    /**
     * Bug 演示: 在事务外访问 ConnectionHolder
     * 
     * 这是 "connection holder is null" 错误的根本原因
     * 
     * TransactionSynchronizationManager 工作原理：
     * 1. 使用 ThreadLocal 存储事务资源（包括 ConnectionHolder）
     * 2. 只有在 @Transactional 方法中，ThreadLocal 才会有值
     * 3. 在事务外访问时，ThreadLocal 返回 null
     * 
     * 技术细节：
     * - TransactionSynchronizationManager.resources 是一个 ThreadLocal<Map<Object, Object>>
     * - Map 的 key 是 DataSource，value 是 ConnectionHolder
     * - 没有事务时，ThreadLocal 中的 Map 为空或不存在
     */
    public void demonstrateNullConnectionHolder() {
        System.out.println("尝试在事务外获取 ConnectionHolder...");
        
        // 检查是否有活动的事务
        // 这会检查 ThreadLocal 中是否有事务标记
        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("是否有活动的事务: " + isTransactionActive);
        
        // 尝试获取 ConnectionHolder - 这将返回 null
        // 因为 ThreadLocal 中没有事务资源
        Object connectionHolder = TransactionSynchronizationManager.getResource(null);
        System.out.println("ConnectionHolder (应该是 null): " + connectionHolder);
        
        if (connectionHolder == null) {
            System.err.println("错误: ConnectionHolder 是 null！这就是 bug 的根源。");
        }
    }

    /**
     * 正确演示: 在事务中访问 ConnectionHolder
     * 
     * 在 @Transactional 方法中，Spring 会：
     * 1. 通过 AOP 拦截方法调用
     * 2. 创建事务并开始事务
     * 3. 将事务信息存储到 ThreadLocal 中
     * 4. 执行方法体
     * 5. 提交或回滚事务
     * 6. 清理 ThreadLocal 中的事务信息
     * 
     * TransactionSynchronizationManager 提供的信息：
     * - 事务是否活动
     * - 事务名称
     * - 隔离级别
     * - 是否只读
     * - 同步状态
     */
    @Transactional
    public void demonstrateValidConnectionHolder() {
        System.out.println("在事务中访问 ConnectionHolder...");
        
        // 检查是否有活动的事务
        // 在 @Transactional 方法中，这应该返回 true
        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("是否有活动的事务: " + isTransactionActive);
        
        // 检查事务同步是否激活
        // 同步机制用于在事务的不同阶段执行回调
        boolean isSynchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        System.out.println("事务同步是否激活: " + isSynchronizationActive);
        
        // 获取当前事务名称
        // 默认是 "类名.方法名"
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        System.out.println("事务名称: " + transactionName);
        
        // 获取隔离级别
        // null 表示使用数据库默认隔离级别
        // 其他值: ISOLATION_READ_UNCOMMITTED, READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
        Integer isolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
        System.out.println("隔离级别: " + isolationLevel);
        
        // 检查是否只读事务
        // @Transactional(readOnly = true) 会设置这个标志
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        System.out.println("是否只读事务: " + isReadOnly);
        
        System.out.println("成功: 所有事务上下文信息都可用！");
    }

    /**
     * Bug 演示: 在事务方法完成后访问事务上下文
     * 
     * 问题场景：
     * 1. 在 @Transactional 方法中创建 Runnable
     * 2. Runnable 在方法返回后执行
     * 3. 此时事务已经提交/回滚，事务上下文已清理
     * 
     * ThreadLocal 生命周期：
     * - 方法开始: Spring AOP 设置 ThreadLocal
     * - 方法执行: ThreadLocal 中有事务信息
     * - 方法返回: Spring AOP 清理 ThreadLocal
     * - 方法后: ThreadLocal 中没有事务信息
     * 
     * 注意：这里的 Runnable 捕获了事务状态，但不会捕获 ThreadLocal
     */
    @Transactional
    public Runnable createRunnableWithTransactionContext() {
        System.out.println("在事务中创建 Runnable...");
        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("创建时事务是否活动: " + isTransactionActive);
        
        // 这个 Runnable 在事务外执行时，无法访问事务上下文
        // 因为 ThreadLocal 是线程本地的，且事务已经结束
        return () -> {
            System.out.println("执行 Runnable...");
            boolean isStillActive = TransactionSynchronizationManager.isActualTransactionActive();
            System.out.println("执行时事务是否活动: " + isStillActive);
            
            if (!isStillActive) {
                System.err.println("错误: 事务上下文丢失！ConnectionHolder 会是 null。");
            }
        };
    }

    /**
     * 在事务上下文外执行 Runnable
     * 
     * 这个方法没有 @Transactional 注解
     * 因此执行 Runnable 时没有事务上下文
     * 
     * 这演示了为什么不能在事务外使用事务内创建的资源引用
     */
    public void executeRunnable(Runnable runnable) {
        System.out.println("在事务上下文外执行 Runnable...");
        runnable.run();
    }
}
