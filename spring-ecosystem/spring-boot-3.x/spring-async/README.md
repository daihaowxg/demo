# Spring Async 示例

## 📖 项目简介

本模块演示了 **Spring `@Async` 注解**的使用方法，展示如何在 Spring Boot 应用中实现异步方法调用，提升系统性能和用户体验。

## 🎯 核心概念

`@Async` 是 Spring 提供的异步执行注解，可以让方法在独立的线程中执行，不阻塞主流程。

### 关键特性

- **异步执行**：方法在独立线程中执行
- **线程池管理**：可配置自定义线程池
- **返回值支持**：支持 `CompletableFuture` 返回异步结果
- **通用性**：不仅限于事件监听，可用于任何业务方法

## 📁 项目结构

```
spring-async/
├── src/main/java/io/github/daihaowxg/async/
│   ├── Application.java          # Spring Boot 启动类
│   ├── config/                   # 配置包
│   │   └── CustomAsyncConfig.java # 自定义线程池配置
│   └── service/                  # 服务包
│       ├── AsyncService.java     # 异步服务示例
│       └── BusinessService.java  # 业务服务示例
└── src/test/java/io/github/daihaowxg/async/
    └── AsyncServiceTest.java     # 测试类
```

## 💡 使用场景

### 场景 1：无返回值的异步方法

```java
@Service
public class AsyncService {
    @Async
    public void sendEmailAsync(String to, String subject) {
        // 异步发送邮件，不阻塞主流程
    }
}
```

**适用于**：
- 发送邮件、短信
- 记录日志
- 发送通知

### 场景 2：有返回值的异步方法

```java
@Async
public CompletableFuture<String> getUserInfoAsync(Long userId) {
    // 异步查询用户信息
    return CompletableFuture.completedFuture(userInfo);
}
```

**适用于**：
- 数据库查询
- 第三方 API 调用
- 需要获取异步结果的场景

### 场景 3：指定线程池

```java
@Async("taskExecutor")  // 指定使用特定的线程池
public void executeTaskAsync(String taskName) {
    // 在指定线程池中执行
}
```

**适用于**：
- 资源隔离
- 不同优先级的任务
- 需要精确控制线程池的场景

### 场景 4：批量异步处理

```java
public void processBatchOrders(Long[] orderIds) {
    for (Long orderId : orderIds) {
        asyncService.processOrderAsync(orderId);  // 并发处理
    }
}
```

**适用于**：
- 批量数据处理
- 并发任务执行
- 提高吞吐量的场景

## 🔧 配置说明

### 默认配置（不推荐生产环境）

```java
@Configuration
@EnableAsync  // 只启用异步
public class AsyncConfig {
}
```

- 使用 `SimpleAsyncTaskExecutor`
- 每次创建新线程，不复用
- 高并发时性能较差

### 自定义线程池（推荐）

```java
@Bean(name = "taskExecutor")
public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);           // 核心线程数
    executor.setMaxPoolSize(10);           // 最大线程数
    executor.setQueueCapacity(100);        // 队列容量
    executor.setThreadNamePrefix("async-"); // 线程名前缀
    executor.initialize();
    return executor;
}
```

### 线程池参数说明

| 参数 | 说明 | 推荐值 |
|------|------|--------|
| **corePoolSize** | 核心线程数 | CPU 核心数 * 2 |
| **maxPoolSize** | 最大线程数 | CPU 核心数 * 4 |
| **queueCapacity** | 队列容量 | 100-1000 |
| **keepAliveSeconds** | 线程空闲时间 | 60s |
| **threadNamePrefix** | 线程名前缀 | 业务相关名称 |

## 🚀 运行示例

### 运行测试

```bash
mvn test
```

### 预期输出

```
========== 场景 1：无返回值的异步方法 ==========
>> 主线程耗时: 2ms (立即返回)
>> 邮件正在后台发送中...
【异步发送邮件】线程: async-event-1, 收件人: user@example.com
【邮件发送成功】收件人: user@example.com
```

## 📊 线程执行流程

```
主线程：                          异步线程池：
┌──────────────────────┐         ┌─────────────────────────┐
│ 调用异步方法         │         │                         │
│   ├─ 提交任务        │────────>│ async-event-1 线程      │
│   └─ 立即返回 ✓     │         │  └─ 执行异步方法        │
└──────────────────────┘         └─────────────────────────┘
     耗时: ~2ms                        耗时: 2000ms
                                       (不阻塞主线程)
```

## ⚠️ 注意事项

1. **方法必须是 public**：`@Async` 只能用于 public 方法
2. **不能在同一个类中调用**：必须通过 Spring 代理调用
3. **异常处理**：异步方法的异常不会传播到调用方
4. **事务问题**：异步方法在独立事务中执行
5. **线程池配置**：生产环境务必配置自定义线程池

## 🎯 最佳实践

1. **合理配置线程池**：根据业务特点调整参数
2. **设置线程名前缀**：方便日志追踪和问题排查
3. **异常处理**：在异步方法内部处理异常
4. **监控线程池**：监控线程池使用情况，及时调整
5. **避免过度使用**：不是所有方法都适合异步

## 🛠️ 技术栈

- Spring Boot 2.7.18
- Lombok
- JUnit 5

## 📖 扩展阅读

- [Spring 官方文档 - Task Execution and Scheduling](https://docs.spring.io/spring-framework/reference/integration/scheduling.html)
- [ThreadPoolExecutor 详解](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ThreadPoolExecutor.html)
