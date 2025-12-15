# Connection Holder is Null - Bug Demo

这个项目演示了 Spring 事务管理中常见的 "connection holder is null" 错误。

## 什么是 Connection Holder？

`ConnectionHolder` 是 Spring 框架中用于管理数据库连接的内部类。它将数据库连接绑定到当前线程的事务上下文中。当我们在事务方法中访问数据库时，Spring 会通过 `TransactionSynchronizationManager` 来获取当前线程绑定的 `ConnectionHolder`。

## Bug 产生的原因

"connection holder is null" 错误通常在以下情况下发生：

### 1. 在事务上下文之外访问连接
当没有 `@Transactional` 注解时，Spring 不会创建事务上下文，因此 `ConnectionHolder` 为 null。

### 2. 在不同线程中访问连接
事务上下文是线程本地的（ThreadLocal），在新线程中无法访问主线程的事务上下文。

### 3. 在事务完成后访问连接
事务方法执行完毕后，Spring 会清理事务上下文，此时 `ConnectionHolder` 已被移除。

## 项目结构

```
demo/
├── ConnectionHolderBugDemoService.java      # 演示三种常见的 bug 场景
├── ConnectionHolderInternalDemo.java        # 演示 Spring 内部机制
└── ConnectionHolderBugDemoRunner.java       # 自动运行所有演示
```

## 演示的场景

### Bug Case 1: 无事务上下文
```java
// ❌ 错误：没有 @Transactional 注解
public void bugCase1_NoTransaction() {
    Connection connection = DataSourceUtils.getConnection(dataSource);
    // 这里会失败，因为没有活动的事务
}
```

### Bug Case 2: 不同线程访问
```java
// ❌ 错误：在新线程中访问事务连接
@Transactional
public void bugCase2_DifferentThread() {
    new Thread(() -> {
        // 这里会失败，因为事务上下文在另一个线程
        Connection connection = DataSourceUtils.getConnection(dataSource);
    }).start();
}
```

### Bug Case 3: 事务完成后访问
```java
// ❌ 错误：存储连接引用并在事务外使用
@Transactional
public void bugCase3_StoreConnection() {
    storedConnection = DataSourceUtils.getConnection(dataSource);
}

public void bugCase3_UseStoredConnection() {
    // 这里可能失败，因为事务上下文已经不存在
    DataSourceUtils.isConnectionTransactional(storedConnection, dataSource);
}
```

### 正确用法
```java
// ✅ 正确：在事务上下文中访问连接
@Transactional
public void correctUsage() {
    Connection connection = DataSourceUtils.getConnection(dataSource);
    // 可以安全地使用连接
}
```

## 运行演示

### 方式 1: 使用 Maven
```bash
cd spring-ecosystem/spring-boot-3.x/spring-transaction
mvn clean spring-boot:run
```

### 方式 2: 使用 IDE
直接运行 `Lab15SpringTransactionApplication` 类

## 预期输出

程序会自动运行所有演示场景，你会看到：

1. **Bug Case 1**: 显示在没有事务时访问连接的错误
2. **Bug Case 2**: 显示在不同线程中访问连接的错误
3. **Bug Case 3**: 显示在事务完成后使用连接的问题
4. **Correct Usage**: 显示正确的使用方式

## 关键知识点

### TransactionSynchronizationManager
Spring 使用 `TransactionSynchronizationManager` 来管理事务上下文：

```java
// 检查是否有活动的事务
boolean isActive = TransactionSynchronizationManager.isActualTransactionActive();

// 获取当前事务名称
String name = TransactionSynchronizationManager.getCurrentTransactionName();

// 检查是否只读
boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
```

### DataSourceUtils
`DataSourceUtils` 提供了事务感知的连接管理：

```java
// 获取事务绑定的连接
Connection conn = DataSourceUtils.getConnection(dataSource);

// 检查连接是否是事务性的
boolean isTx = DataSourceUtils.isConnectionTransactional(conn, dataSource);

// 释放连接（如果不是事务性的会关闭）
DataSourceUtils.releaseConnection(conn, dataSource);
```

## 解决方案

1. **确保在事务上下文中操作**
   - 使用 `@Transactional` 注解
   - 确保事务管理器正确配置

2. **避免跨线程访问**
   - 不要在新线程中访问事务资源
   - 如需异步处理，考虑使用 Spring 的 `@Async` 并配置事务传播

3. **不要存储连接引用**
   - 让 Spring 管理连接的生命周期
   - 在需要时通过 `DataSourceUtils` 获取

## 相关配置

项目使用 H2 内存数据库，配置在 `application.properties` 中：

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true
```

## 技术栈

- Spring Boot 3.5.6
- Spring Transaction
- Spring JDBC
- H2 Database
- Java 17

## 学习目标

通过这个演示，你将理解：
- Spring 事务管理的内部机制
- `ConnectionHolder` 的作用和生命周期
- 常见的事务错误及其原因
- 如何正确使用 Spring 事务
