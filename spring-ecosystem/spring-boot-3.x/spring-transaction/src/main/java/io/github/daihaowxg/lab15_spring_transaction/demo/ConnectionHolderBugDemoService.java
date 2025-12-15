package io.github.daihaowxg.lab15_spring_transaction.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * ConnectionHolder Bug 演示服务
 * 
 * 演示 "connection holder is null" 错误的常见场景
 * 
 * 这个 bug 通常在以下情况下发生：
 * 1. 在事务上下文之外访问连接
 * 2. 事务同步未正确设置
 * 3. 在事务完成后访问连接
 * 
 * @author daihaowxg
 */
@Service
public class ConnectionHolderBugDemoService {

    @Autowired
    private DataSource dataSource;

    /**
     * Bug 场景 1: 在事务上下文之外访问连接
     * 
     * 问题原因：
     * - 方法没有 @Transactional 注解，因此 Spring 不会创建事务上下文
     * - DataSourceUtils.getConnection() 尝试获取事务绑定的连接
     * - 由于没有活动的事务，ConnectionHolder 为 null
     * 
     * 错误表现：
     * - 可能抛出异常或返回非事务性连接
     * - isConnectionTransactional() 返回 false
     */
    public void bugCase1_NoTransaction() {
        try {
            // 尝试获取连接 - 这里会失败或获取到非事务性连接
            // 因为没有 @Transactional 注解，Spring 没有创建事务上下文
            Connection connection = DataSourceUtils.getConnection(dataSource);
            System.out.println("获取到的连接: " + connection);

            // 检查连接是否是事务性的
            // 在没有事务的情况下，这会返回 false
            boolean isConnectionTransactional = DataSourceUtils.isConnectionTransactional(connection, dataSource);
            System.out.println("连接是否是事务性的: " + isConnectionTransactional);

        } catch (Exception e) {
            System.err.println("Bug 场景 1 - 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bug 场景 2: 在不同线程中访问连接
     * 
     * 问题原因：
     * - Spring 的事务上下文是基于 ThreadLocal 实现的
     * - TransactionSynchronizationManager 将事务信息绑定到当前线程
     * - 新创建的线程没有父线程的事务上下文
     * 
     * 技术细节：
     * - 主线程有 @Transactional，可以正常获取连接
     * - 新线程虽然在事务方法内创建，但它有自己的 ThreadLocal 副本
     * - 新线程的 ThreadLocal 中没有事务信息，因此 ConnectionHolder 为 null
     */
    @Transactional
    public void bugCase2_DifferentThread() {
        System.out.println("主线程: " + Thread.currentThread().getName());

        // 在主线程中可以正常工作
        // 因为 @Transactional 注解使 Spring 在主线程中创建了事务上下文
        Connection mainConnection = DataSourceUtils.getConnection(dataSource);
        System.out.println("主线程获取的连接: " + mainConnection);

        // 在新线程中会失败
        // 新线程没有事务上下文，因为 ThreadLocal 不会传递到子线程
        Thread newThread = new Thread(() -> {
            try {
                System.out.println("新线程: " + Thread.currentThread().getName());
                // 这里会失败，因为新线程没有事务上下文
                Connection threadConnection = DataSourceUtils.getConnection(dataSource);
                System.out.println("新线程获取的连接: " + threadConnection);
            } catch (Exception e) {
                System.err.println("Bug 场景 2 - 新线程中的错误: " + e.getMessage());
                e.printStackTrace();
            }
        });

        newThread.start();
        try {
            newThread.join(); // 等待新线程完成
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Bug 场景 3: 在事务完成后访问连接
     * 
     * 问题原因：
     * - 事务方法执行完毕后，Spring 会清理事务上下文
     * - TransactionSynchronizationManager 会移除 ThreadLocal 中的事务信息
     * - 存储的连接引用虽然还在，但事务上下文已经不存在了
     * 
     * 生命周期：
     * 1. @Transactional 方法开始 -> Spring 创建事务上下文
     * 2. 方法执行中 -> 可以访问事务绑定的连接
     * 3. 方法返回 -> Spring 清理事务上下文
     * 4. 方法外 -> 事务上下文不存在，ConnectionHolder 为 null
     */
    private Connection storedConnection; // 存储的连接引用

    @Transactional
    public void bugCase3_StoreConnection() {
        // 在事务中获取并存储连接
        // 此时事务上下文存在，可以正常获取连接
        storedConnection = DataSourceUtils.getConnection(dataSource);
        System.out.println("在事务中存储的连接: " + storedConnection);
        // 注意：方法返回后，事务会提交或回滚，事务上下文会被清理
    }

    public void bugCase3_UseStoredConnection() {
        try {
            if (storedConnection != null) {
                System.out.println("尝试使用存储的连接: " + storedConnection);
                // 这里可能会失败，因为事务上下文已经不存在了
                // 虽然连接对象还在，但它不再与任何事务关联
                boolean isTransactional = DataSourceUtils.isConnectionTransactional(storedConnection, dataSource);
                System.out.println("存储的连接是否是事务性的: " + isTransactional);
            }
        } catch (Exception e) {
            System.err.println("Bug 场景 3 - 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 正确用法: 在事务上下文中访问连接
     * 
     * 最佳实践：
     * 1. 使用 @Transactional 注解标记需要事务的方法
     * 2. 在方法内部使用 DataSourceUtils 获取连接
     * 3. 不要存储连接引用供方法外使用
     * 4. 让 Spring 管理连接的生命周期
     * 
     * Spring 的处理流程：
     * 1. AOP 拦截 @Transactional 方法
     * 2. 创建事务并绑定到 ThreadLocal
     * 3. 方法执行时可以获取事务绑定的连接
     * 4. 方法完成后提交或回滚事务
     * 5. 清理事务上下文和释放连接
     */
    @Transactional
    public void correctUsage() {
        try {
            // 正确：在 @Transactional 方法中获取连接
            // Spring 已经创建了事务上下文，可以安全地获取连接
            Connection connection = DataSourceUtils.getConnection(dataSource);
            System.out.println("正确用法 - 获取的连接: " + connection);

            // 验证连接是事务性的
            boolean isTransactional = DataSourceUtils.isConnectionTransactional(connection, dataSource);
            System.out.println("正确用法 - 连接是否是事务性的: " + isTransactional);

            // 使用连接执行数据库操作
            // 所有操作都在同一个事务中，保证 ACID 特性
            System.out.println("正确用法 - 连接有效，可以安全使用");

            // 不需要手动关闭连接
            // Spring 会在事务结束时自动管理连接的释放

        } catch (Exception e) {
            System.err.println("正确用法 - 意外错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
