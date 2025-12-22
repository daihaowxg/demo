# Druid removeAbandoned 工作机制详解

## ✅ **验证结论**

通过实际测试验证：

| 配置项 | 单位 | 示例值 | 说明 |
|--------|------|--------|------|
| `remove-abandoned-timeout` | **秒** | 5 | 配置 5 = 5秒后回收 |
| `time-between-eviction-runs-millis` | **毫秒** | 1000 | 检查频率，影响回收时机 |

## 🔍 **核心发现**

通过测试发现，`removeAbandoned` **不是在 `getConnection()` 时触发**，而是由 **后台 `DestroyTask` 线程定期检查**！

### 关键配置

```yaml
spring:
  datasource:
    druid:
      remove-abandoned: true
      remove-abandoned-timeout: 5          # 单位：秒
      log-abandoned: true
      time-between-eviction-runs-millis: 1000  # ⚠️ 关键！控制检查频率
```

### 2. **连接池维护线程触发**（需要配置）

如果配置了 `timeBetweenEvictionRunsMillis`，Druid 会定期运行维护线程：

```yaml
spring:
  datasource:
    druid:
      time-between-eviction-runs-millis: 60000  # 每 60 秒运行一次
      remove-abandoned: true
      remove-abandoned-timeout: 180
```

## ❌ **为什么原测试失败**

原测试代码：
```java
Connection conn = dataSource.getConnection();
// 不关闭连接

// 只是等待，没有触发检查
for (int i = 1; i <= 10; i++) {
    Thread.sleep(1000);
    // 只是读取活跃连接数，不会触发回收
    int activeCount = druidDataSource.getActiveCount();
}
```

**问题**：
- ❌ 只是等待时间，没有触发检查机制
- ❌ 没有获取新连接
- ❌ 没有配置维护线程

## ✅ **正确的测试方法**

修改后的测试代码：
```java
Connection leakedConn = dataSource.getConnection();
// 不关闭连接，模拟泄漏

Thread.sleep(6000); // 等待超过超时时间

// 关键：获取新连接来触发检查
Connection triggerConn = dataSource.getConnection(); // ← 触发回收
int activeCount = druidDataSource.getActiveCount();
triggerConn.close();

// 此时 leakedConn 应该已被回收
```

## 📊 **验证单位的正确方法**

### 方法 1：读取配置值（推荐）

```java
DruidDataSource ds = (DruidDataSource) dataSource;
long timeoutMillis = ds.getRemoveAbandonedTimeoutMillis();

// 配置文件中设置 180
// 返回值是 180000 毫秒 → 证明单位是秒
```

### 方法 2：实际触发测试

```java
// 1. 设置较小的超时时间（如 5 秒）
// 2. 获取连接但不关闭
Connection leaked = dataSource.getConnection();

// 3. 等待超过超时时间
Thread.sleep(6000);

// 4. 获取新连接触发检查
Connection trigger = dataSource.getConnection();

// 5. 观察日志是否有 "abandon connection" 警告
```

## 🎯 **实际应用建议**

### 1. **开发环境**

```yaml
spring:
  datasource:
    druid:
      remove-abandoned: true
      remove-abandoned-timeout: 60  # 1分钟，快速发现问题
      log-abandoned: true
      # 可选：配置维护线程
      time-between-eviction-runs-millis: 30000  # 30秒检查一次
```

### 2. **生产环境**

```yaml
spring:
  datasource:
    druid:
      # 方案 A：不开启（推荐）
      remove-abandoned: false
      
      # 方案 B：谨慎开启
      # remove-abandoned: true
      # remove-abandoned-timeout: 600  # 10分钟
      # log-abandoned: true
```

### 3. **最佳实践**

**不依赖 removeAbandoned**，而是：

1. **使用 try-with-resources**：
   ```java
   try (Connection conn = dataSource.getConnection()) {
       // 使用连接
   } // 自动关闭
   ```

2. **使用 Spring JdbcTemplate**：
   ```java
   @Autowired
   private JdbcTemplate jdbcTemplate;
   
   // JdbcTemplate 会自动管理连接
   jdbcTemplate.query("SELECT * FROM users", ...);
   ```

3. **代码审查**：
   - 确保所有 `getConnection()` 都有对应的 `close()`
   - 使用静态代码分析工具检查

4. **监控连接池**：
   - 定期查看 Druid 监控页面
   - 关注活跃连接数趋势
   - 设置告警阈值

## 📝 **总结**

| 项目 | 说明 |
|------|------|
| **单位** | 秒（配置文件中） |
| **内部存储** | 毫秒 |
| **触发方式** | 被动触发（获取新连接时） |
| **生产环境** | 不建议开启 |
| **开发环境** | 建议开启，帮助发现问题 |
| **最佳实践** | 使用 try-with-resources 或 JdbcTemplate |

## ⚠️ **重要警告**

```
WARN  com.alibaba.druid.pool.DruidDataSource - 
removeAbandoned is true, not use in production.
```

这个警告的原因：
1. **可能误杀**：正常的长时间查询可能被误判为泄漏
2. **性能影响**：每次获取连接都要检查
3. **不是根本解决方案**：应该从代码层面解决连接泄漏

---

**结论**：`removeAbandoned` 是一个调试工具，不是生产环境的解决方案！
