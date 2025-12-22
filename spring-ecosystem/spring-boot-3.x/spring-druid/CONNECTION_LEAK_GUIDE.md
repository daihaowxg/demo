# 连接泄漏检测日志查看指南

## 📍 日志输出位置

### 1. **控制台输出**

当你运行应用时（`mvn spring-boot:run` 或 IDE 启动），连接泄漏日志会直接输出到控制台。

### 2. **日志文件**

日志文件位置：`logs/spring-druid.log`

```bash
# 查看日志文件
tail -f logs/spring-druid.log

# 搜索连接泄漏相关日志
grep "abandon" logs/spring-druid.log
```

## 🔍 连接泄漏日志示例

当发生连接泄漏时，你会看到类似以下的日志：

```
2025-12-22 14:00:00.123  WARN 12345 --- [Druid-ConnectionPool-Destroy-123] com.alibaba.druid.pool.DruidDataSource   : abandon connection, owner thread: Thread[http-nio-8080-exec-1,5,main], connected at : 1703232000123, open stackTrace
java.lang.Exception: open stackTrace
    at com.alibaba.druid.pool.DruidDataSource.getConnectionDirect(DruidDataSource.java:1234)
    at com.alibaba.druid.pool.DruidDataSource.getConnection(DruidDataSource.java:1000)
    at com.zaxxer.hikari.pool.HikariPool.getConnection(HikariPool.java:123)
    at org.springframework.jdbc.datasource.DataSourceUtils.doGetConnection(DataSourceUtils.java:123)
    at org.springframework.jdbc.core.JdbcTemplate.execute(JdbcTemplate.java:123)
    at io.github.daihaowxg.druid.repository.UserRepository.findAll(UserRepository.java:47)
    at io.github.daihaowxg.druid.service.UserService.getAllUsers(UserService.java:35)
    at io.github.daihaowxg.druid.controller.UserController.getAllUsers(UserController.java:28)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    ...
```

## 📊 日志信息解读

### 关键信息

1. **日志级别**：`WARN`
2. **日志来源**：`com.alibaba.druid.pool.DruidDataSource`
3. **关键字**：`abandon connection`

### 堆栈信息含义

堆栈信息显示了**连接被获取时的调用链**，帮助你定位：

- 哪个类获取了连接：`UserRepository.findAll`
- 哪个方法调用了它：`UserService.getAllUsers`
- 最终是哪个接口触发的：`UserController.getAllUsers`

### 时间信息

- `connected at : 1703232000123`：连接被获取的时间戳
- 如果当前时间 - connected at > 180秒，连接会被强制回收

## 🧪 如何测试连接泄漏检测

### 方法 1：创建测试端点

创建一个故意不关闭连接的测试方法：

```java
@GetMapping("/test/leak")
public String testConnectionLeak() throws SQLException {
    DataSource ds = ((DruidDataSource) dataSource).getRawDataSource();
    Connection conn = ds.getConnection();
    // 故意不关闭连接
    // conn.close(); 
    return "Connection leaked!";
}
```

### 方法 2：使用 JMeter 或 curl 压测

```bash
# 连续请求多次，快速消耗连接
for i in {1..25}; do
  curl http://localhost:8080/test/leak &
done

# 等待 180 秒后查看日志
sleep 180
tail -f logs/spring-druid.log
```

### 方法 3：调整超时时间测试

为了快速测试，可以临时将超时时间改小：

```yaml
spring:
  datasource:
    druid:
      remove-abandoned-timeout: 10  # 改为 10 秒，方便测试
```

## 📝 实时监控日志

### 使用 tail 命令

```bash
# 实时查看日志文件
tail -f logs/spring-druid.log

# 只看 WARN 和 ERROR 级别
tail -f logs/spring-druid.log | grep -E "WARN|ERROR"

# 只看连接泄漏相关
tail -f logs/spring-druid.log | grep "abandon"
```

### 使用 IDE 查看

如果在 IDE 中运行：
1. **IntelliJ IDEA**：在 Run 窗口直接查看控制台输出
2. **Eclipse**：在 Console 窗口查看
3. **VS Code**：在终端窗口查看

## 🔧 日志级别配置

确保 Druid 的日志级别至少是 `WARN`：

```yaml
logging:
  level:
    com.alibaba.druid: warn  # 至少要 warn 级别才能看到连接泄漏日志
```

当前配置是 `debug`，所以会输出所有级别的日志。

## 📈 在 Druid 监控页面查看

除了日志，你还可以在 Druid 监控页面查看连接池状态：

1. 访问：http://localhost:8080/druid
2. 登录（admin/admin123）
3. 查看 **数据源** 页面
4. 关注以下指标：
   - **活跃连接数**：如果持续增长不下降，可能有泄漏
   - **等待线程数**：如果很高，说明连接池可能被耗尽
   - **错误统计**：查看是否有异常

## ⚠️ 常见问题

### Q1: 为什么看不到连接泄漏日志？

**可能原因**：
- 日志级别太高（改为 `warn` 或 `debug`）
- `log-abandoned` 配置为 `false`
- 没有真正发生连接泄漏
- 超时时间太长，还没到触发时间

### Q2: 如何判断是真的泄漏还是正常的长查询？

**判断方法**：
1. 查看堆栈信息，确认是哪个方法
2. 评估该方法的正常执行时间
3. 如果是正常的长查询，调大 `remove-abandoned-timeout`
4. 如果是泄漏，修复代码确保连接被正确关闭

### Q3: 生产环境应该如何配置？

**建议配置**：
```yaml
spring:
  datasource:
    druid:
      remove-abandoned: true
      remove-abandoned-timeout: 300  # 5分钟，根据业务调整
      log-abandoned: true
```

## 📚 相关文档

- [Druid 连接泄漏检测文档](https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE)
- [Spring Boot 日志配置](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)

---

**提示**：连接泄漏检测是一个强大的调试工具，但在生产环境中要谨慎配置超时时间，避免误杀正常的长时间查询。
