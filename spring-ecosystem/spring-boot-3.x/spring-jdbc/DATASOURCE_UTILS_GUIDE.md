# DataSourceUtils 使用示例

## 概述

`DataSourceUtils` 是 Spring JDBC 提供的工具类，用于在事务环境中正确获取和释放数据库连接。它是 Spring 事务管理的核心组件之一。

## 为什么需要 DataSourceUtils？

### 问题背景

在 Spring 事务环境中，如果直接使用 `dataSource.getConnection()`，会遇到以下问题：

```java
@Transactional
public void badExample() {
    Connection con1 = dataSource.getConnection(); // ❌ 创建新连接，不参与事务
    Connection con2 = dataSource.getConnection(); // ❌ 又创建一个新连接
    // con1 和 con2 是不同的连接，无法保证事务一致性
}
```

### 正确做法

使用 `DataSourceUtils.getConnection()`：

```java
@Transactional
public void goodExample() {
    Connection con1 = DataSourceUtils.getConnection(dataSource); // ✅ 获取事务连接
    Connection con2 = DataSourceUtils.getConnection(dataSource); // ✅ 返回同一个连接
    // con1 和 con2 是同一个连接，确保事务一致性
}
```

## 核心特性

### 1. 事务感知（Transaction-Aware）

在 `@Transactional` 方法中，`DataSourceUtils.getConnection()` 会：
- 检查当前线程是否有活动事务
- 如果有事务，返回事务中的连接
- 如果没有事务，从连接池获取新连接

### 2. 连接复用（Connection Reuse）

在同一个事务中，多次调用 `getConnection()` 返回同一个连接对象：

```java
@Transactional
public void demo() {
    Connection con1 = DataSourceUtils.getConnection(dataSource);
    Connection con2 = DataSourceUtils.getConnection(dataSource);
    System.out.println(con1 == con2); // true - 同一个连接
}
```

### 3. 正确释放（Proper Release）

使用 `DataSourceUtils.releaseConnection()` 而不是 `Connection.close()`：

```java
Connection con = DataSourceUtils.getConnection(dataSource);
try {
    // 使用连接
} finally {
    // ✅ 正确：在事务中不会真正关闭连接
    DataSourceUtils.releaseConnection(con, dataSource);
    
    // ❌ 错误：会在事务中提前关闭连接，导致后续操作失败
    // con.close();
}
```

## 使用示例

本项目提供了 6 个完整的使用示例：

### 示例 1：基本用法

```java
public Optional<User> findById(Long id) {
    Connection con = DataSourceUtils.getConnection(dataSource);
    try {
        // 执行查询
        // ...
        return Optional.of(user);
    } finally {
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
```

### 示例 2：事务中的连接复用

```java
@Transactional
public void transferUserData(Long fromId, Long toId) {
    Connection con1 = DataSourceUtils.getConnection(dataSource);
    // 使用 con1 查询
    DataSourceUtils.releaseConnection(con1, dataSource);
    
    Connection con2 = DataSourceUtils.getConnection(dataSource);
    // 使用 con2 更新
    DataSourceUtils.releaseConnection(con2, dataSource);
    
    // con1 == con2，它们是同一个连接
}
```

### 示例 3：事务回滚

```java
@Transactional
public void saveUserWithRollback(User user) {
    Connection con = DataSourceUtils.getConnection(dataSource);
    try {
        // 插入数据
        // ...
        
        if (user.getName().contains("error")) {
            throw new RuntimeException("触发回滚");
        }
    } finally {
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
```

### 示例 4：批量操作

```java
@Transactional
public void batchInsert(List<User> users) {
    Connection con = DataSourceUtils.getConnection(dataSource);
    try {
        PreparedStatement ps = con.prepareStatement(sql);
        for (User user : users) {
            ps.setString(1, user.getName());
            ps.addBatch();
        }
        ps.executeBatch();
    } finally {
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
```

### 示例 5：非事务环境

```java
// 没有 @Transactional 注解
public List<User> findAll() {
    Connection con = DataSourceUtils.getConnection(dataSource);
    try {
        // 执行查询
        // ...
    } finally {
        // 在非事务环境中，这会真正关闭连接
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
```

### 示例 6：检查连接状态

```java
@Transactional
public void demonstrateConnectionInfo() {
    Connection con = DataSourceUtils.getConnection(dataSource);
    try {
        boolean isTransactional = DataSourceUtils.isConnectionTransactional(con, dataSource);
        System.out.println("Is transactional? " + isTransactional); // true
    } finally {
        DataSourceUtils.releaseConnection(con, dataSource);
    }
}
```

## 运行测试

```bash
# 进入项目目录
cd spring-ecosystem/spring-boot-3.x/spring-jdbc

# 运行测试
mvn test -Dtest=DataSourceUtilsRepositoryTest
```

## 测试用例说明

| 测试方法 | 说明 |
|---------|------|
| `testFindById` | 测试基本查询操作 |
| `testTransferUserData` | 测试事务中的连接复用 |
| `testSaveUserWithRollback` | 测试事务回滚 |
| `testSaveUserWithoutRollback` | 测试正常保存 |
| `testBatchInsert` | 测试批量插入 |
| `testFindAll` | 测试非事务环境查询 |
| `testDemonstrateConnectionInfo` | 测试连接状态检查 |
| `testTransactionACID` | 测试事务的 ACID 特性 |

## 最佳实践

### ✅ 推荐做法

1. **始终使用 try-finally**
   ```java
   Connection con = DataSourceUtils.getConnection(dataSource);
   try {
       // 使用连接
   } finally {
       DataSourceUtils.releaseConnection(con, dataSource);
   }
   ```

2. **在事务方法中使用**
   ```java
   @Transactional
   public void businessMethod() {
       Connection con = DataSourceUtils.getConnection(dataSource);
       // ...
   }
   ```

3. **使用 isConnectionTransactional 检查**
   ```java
   boolean isTx = DataSourceUtils.isConnectionTransactional(con, dataSource);
   ```

### ❌ 避免的做法

1. **不要直接调用 close()**
   ```java
   Connection con = DataSourceUtils.getConnection(dataSource);
   con.close(); // ❌ 错误！在事务中会导致问题
   ```

2. **不要混用 dataSource.getConnection()**
   ```java
   @Transactional
   public void badExample() {
       Connection con1 = DataSourceUtils.getConnection(dataSource); // ✅
       Connection con2 = dataSource.getConnection(); // ❌ 不在同一个事务中
   }
   ```

3. **不要忘记释放连接**
   ```java
   Connection con = DataSourceUtils.getConnection(dataSource);
   // 使用连接
   // ❌ 忘记调用 releaseConnection，导致连接泄漏
   ```

## 对比总结

| 特性 | dataSource.getConnection() | DataSourceUtils.getConnection() |
|------|---------------------------|--------------------------------|
| 事务感知 | ❌ 否 | ✅ 是 |
| 连接复用 | ❌ 每次创建新连接 | ✅ 事务中复用连接 |
| 释放方式 | con.close() | DataSourceUtils.releaseConnection() |
| 适用场景 | 非 Spring 环境 | Spring 事务环境 |

## 相关资源

- [DataSourceUtilsRepository.java](./src/main/java/io/github/daihaowxg/spring/jdbc/repository/DataSourceUtilsRepository.java) - 完整示例代码
- [DataSourceUtilsRepositoryTest.java](./src/test/java/io/github/daihaowxg/spring/jdbc/repository/DataSourceUtilsRepositoryTest.java) - 测试用例
- [Spring 官方文档](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/DataSourceUtils.html)
