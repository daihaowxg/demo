# Spring Event 示例

## 📖 项目简介

本模块演示了 **Spring 事件机制**（Spring Event）的基本用法，展示了如何在 Spring Boot 应用中实现事件的发布和监听，从而实现业务逻辑的解耦。

## 🎯 核心概念

Spring 事件机制是基于**观察者模式**的实现，允许应用中的不同组件通过事件进行通信，而无需直接依赖彼此。

### 主要组件

- **ApplicationEvent**: 自定义事件类，继承自 `ApplicationEvent`
- **ApplicationEventPublisher**: 事件发布器，用于发布事件
- **@EventListener**: 事件监听器注解，用于标记事件处理方法

## 🤔 为什么使用事件机制？

### 核心问题：既然默认是同步执行，为什么不直接调用？

事件机制的价值**不在于同步还是异步**，而在于**架构层面的解耦和可维护性**。

### 1. 降低耦合度

**❌ 直接调用方式**（高耦合）：
```java
@Service
public class UserService {
    // 需要依赖所有后续服务
    private final EmailService emailService;
    private final SmsService smsService;
    private final PointsService pointsService;
    private final StatisticsService statisticsService;
    
    public void register(String username) {
        saveUser(username);
        
        // 紧密耦合：UserService 必须知道所有后续操作
        emailService.sendWelcomeEmail(username);
        smsService.sendWelcomeSms(username);
        pointsService.grantNewUserPoints(username);
        statisticsService.recordRegistration(username);
    }
}
```

**✅ 事件机制方式**（低耦合）：
```java
@Service
public class UserService {
    // 只依赖事件发布器
    private final ApplicationEventPublisher eventPublisher;
    
    public void register(String username) {
        saveUser(username);
        
        // 解耦：只需发布事件，不需要知道谁在监听
        eventPublisher.publishEvent(new UserRegisterEvent(this, username));
    }
}
```

**优势**：
- ✅ `UserService` 不需要依赖多个服务类
- ✅ 添加新功能时无需修改 `UserService`
- ✅ 符合**开闭原则**（对扩展开放，对修改关闭）

### 2. 灵活扩展

使用事件机制，可以在**不修改发布者代码**的情况下动态增减监听器：

```java
// 新增监听器，UserService 完全不需要改动
@Component
@Profile("production")  // 只在生产环境生效
public class UserAnalyticsListener {
    @EventListener
    public void handleUserRegister(UserRegisterEvent event) {
        // 发送数据到分析平台
    }
}

// 条件监听：只处理 VIP 用户
@Component
public class VipUserListener {
    @EventListener(condition = "#event.username.startsWith('vip_')")
    public void handleVipUser(UserRegisterEvent event) {
        // VIP 用户特殊处理
    }
}
```

### 3. 单一职责原则

每个组件只关注自己的职责：

```java
// UserService：只负责用户注册核心逻辑
@Service
public class UserService {
    public void register(String username) {
        validateUsername(username);
        saveToDatabase(username);
        publishEvent(new UserRegisterEvent(this, username));
    }
}

// EmailListener：只负责邮件相关逻辑
@Component
public class EmailListener {
    @EventListener
    public void sendEmail(UserRegisterEvent event) {
        // 发送欢迎邮件
    }
}

// PointsListener：只负责积分相关逻辑
@Component
public class PointsListener {
    @EventListener
    public void grantPoints(UserRegisterEvent event) {
        // 赠送新人积分
    }
}
```

### 4. 便于测试

```java
// 测试 UserService 时，不需要 mock 所有依赖的服务
@Test
void testRegister() {
    UserService userService = new UserService(eventPublisher);
    userService.register("test");
    
    // 只需验证事件是否发布
    verify(eventPublisher).publishEvent(any(UserRegisterEvent.class));
}
```

### 5. 轻松支持异步

虽然默认是同步的，但事件机制为异步处理提供了便利：

```java
// 只需添加 @Async 注解，即可实现异步
@Async
@EventListener
public void handleUserRegisterEvent(UserRegisterEvent event) {
    // 异步发送邮件，不阻塞主流程
    emailService.send(event.getUsername());
}
```

如果是直接调用，改成异步需要更多改动。

### 6. 对比总结

| 特性 | 直接调用 | 事件机制 |
|------|---------|---------|
| **耦合度** | 高（需要依赖所有服务） | 低（只依赖事件发布器） |
| **扩展性** | 差（添加功能需修改代码） | 好（添加监听器即可） |
| **测试性** | 需要 mock 多个依赖 | 只需验证事件发布 |
| **职责分离** | 发布者需要知道所有后续操作 | 发布者只负责发布事件 |
| **灵活性** | 固定的调用链 | 可动态增减监听器 |
| **异步支持** | 需要手动处理 | 添加注解即可 |
| **符合设计原则** | 违反开闭原则 | 符合开闭原则、单一职责 |

### 💡 结论

事件机制的核心价值在于：
1. **架构层面的解耦** - 降低组件间的依赖关系
2. **更好的可维护性** - 修改和扩展更容易
3. **符合设计原则** - 单一职责、开闭原则
4. **为未来提供便利** - 轻松支持异步、条件处理等

即使是同步执行，这些架构优势依然存在，这才是事件机制的真正价值！

## 📁 项目结构

```
spring-event/
├── src/main/java/io/github/daihaowxg/event/
│   ├── Application.java              # Spring Boot 启动类
│   ├── UserRegisterEvent.java        # 用户注册事件
│   ├── UserService.java              # 用户服务（事件发布者）
│   ├── config/                       # 配置包
│   │   └── AsyncConfig.java          # 异步配置
│   ├── traditional/                  # 传统方式（不使用事件机制）
│   │   ├── TraditionalUserService.java  # 传统用户服务（高耦合示例）
│   │   ├── EmailService.java         # 邮件服务
│   │   ├── PointsService.java        # 积分服务
│   │   └── StatisticsService.java    # 统计服务
│   ├── sync/                         # 同步事件监听
│   │   ├── EmailListener.java        # 邮件监听器（同步）
│   │   ├── PointsListener.java       # 积分监听器（同步）
│   │   ├── StatisticsListener.java   # 统计监听器（同步）
│   │   └── VipUserListener.java      # VIP 用户监听器（条件监听）
│   └── async/                        # 异步事件监听
│       └── AsyncEmailListener.java   # 异步邮件监听器
└── src/test/java/io/github/daihaowxg/event/
    └── SpringEventTest.java          # 测试类
```

### 包说明

- **根目录**: 核心组件（事件定义、事件发布者）
- **traditional**: 传统直接调用方式（高耦合），用于对比展示事件机制的优势
- **sync**: 同步事件监听方式（低耦合，同步执行），适合快速操作
- **async**: 异步事件监听方式（低耦合，异步执行），适合耗时操作

## 💡 实现示例

### 1. 定义事件类

```java
@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private final String username;

    public UserRegisterEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
```

### 2. 发布事件

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final ApplicationEventPublisher eventPublisher;

    public void register(String username) {
        // 执行注册逻辑
        System.out.println(">> 用户 " + username + " 注册成功");
        
        // 发布事件
        eventPublisher.publishEvent(new UserRegisterEvent(this, username));
    }
}
```

### 3. 监听事件

**邮件监听器**：
```java
@Component
public class EmailListener {
    @Order(1)
    @EventListener
    public void sendWelcomeEmail(UserRegisterEvent event) {
        // 发送欢迎邮件
        System.out.println(">> 发送欢迎邮件给: " + event.getUsername());
    }
}
```

**积分监听器**：
```java
@Component
public class PointsListener {
    @Order(2)
    @EventListener
    public void grantNewUserPoints(UserRegisterEvent event) {
        // 赠送新人积分
        System.out.println(">> 赠送 100 积分给新用户: " + event.getUsername());
    }
}
```

**统计监听器**：
```java
@Component
public class StatisticsListener {
    @Order(3)
    @EventListener
    public void recordRegistration(UserRegisterEvent event) {
        // 记录统计
        System.out.println(">> 记录用户注册统计: " + event.getUsername());
    }
}
```

**VIP 用户监听器（条件监听）**：
```java
@Component
public class VipUserListener {
    // 只处理用户名以 vip_ 开头的用户
    @EventListener(condition = "#event.username.startsWith('vip_')")
    public void handleVipUser(UserRegisterEvent event) {
        System.out.println(">> VIP 用户注册，赠送专属礼包: " + event.getUsername());
    }
}
```

## 🚀 运行示例

### 运行测试

```bash
mvn test
```

### 预期输出

**普通用户注册**：
```
>> 用户 daihaowxg 注册成功
>> 发送欢迎邮件给: daihaowxg
>> 赠送 100 积分给新用户: daihaowxg
>> 记录用户注册统计: daihaowxg
```

**VIP 用户注册**：
```
>> 用户 vip_alice 注册成功
>> 发送欢迎邮件给: vip_alice
>> 赠送 100 积分给新用户: vip_alice
>> 记录用户注册统计: vip_alice
>> VIP 用户注册，赠送专属礼包: vip_alice
```

## 🔄 对比示例

### 对比 1：传统方式 vs 事件机制

#### ❌ 传统方式（高耦合）

```java
@Service
@RequiredArgsConstructor
public class TraditionalUserService {
    // 需要依赖所有后续服务 - 高耦合！
    private final EmailService emailService;
    private final PointsService pointsService;
    private final StatisticsService statisticsService;
    
    public void register(String username) {
        saveUser(username);
        
        // 必须手动调用所有后续操作 - 紧密耦合！
        emailService.sendWelcomeEmail(username);
        pointsService.grantNewUserPoints(username);
        statisticsService.recordRegistration(username);
        
        // 如果要添加新功能，必须在这里添加代码 - 违反开闭原则！
    }
}
```

**问题**：
- ❌ UserService 需要依赖所有后续服务
- ❌ 添加新功能需要修改 UserService 代码
- ❌ 违反开闭原则和单一职责原则
- ❌ 测试时需要 mock 所有依赖

#### ✅ 事件机制方式（低耦合）

```java
@Service
@RequiredArgsConstructor
public class UserService {
    // 只依赖事件发布器 - 低耦合！
    private final ApplicationEventPublisher eventPublisher;
    
    public void register(String username) {
        saveUser(username);
        
        // 只需发布事件，不需要知道谁在监听 - 解耦！
        eventPublisher.publishEvent(new UserRegisterEvent(this, username));
        
        // 添加新功能只需添加新的监听器，无需修改此处代码！
    }
}
```

**优势**：
- ✅ UserService 只依赖事件发布器
- ✅ 添加新功能只需添加监听器，无需修改 UserService
- ✅ 符合开闭原则和单一职责原则
- ✅ 测试时只需验证事件发布

### 对比 2：同步监听 vs 异步监听

#### 同步监听（默认）

```java
@Component
public class EmailListener {
    @EventListener
    public void sendWelcomeEmail(UserRegisterEvent event) {
        // 在主线程中执行，会阻塞用户注册流程
        sendEmail(event.getUsername()); // 假设耗时 2 秒
    }
}
```

**特点**：
- 在主线程中执行
- 会阻塞主流程
- 如果发送邮件失败，会影响用户注册

#### 异步监听（推荐用于耗时操作）

```java
@Component
public class AsyncEmailListener {
    @Async  // 添加 @Async 注解
    @EventListener
    public void sendWelcomeEmailAsync(UserRegisterEvent event) {
        // 在独立线程中执行，不阻塞用户注册流程
        sendEmail(event.getUsername()); // 耗时 2 秒，但不影响主流程
    }
}
```

**优势**：
- ✅ 在独立线程中执行，不阻塞主流程
- ✅ 提升响应速度（主流程几乎不受影响）
- ✅ 即使邮件发送失败，也不影响用户注册
- ✅ 可以并发处理多个事件

**性能对比**：
```
同步方式：主流程耗时 = 注册逻辑 + 发送邮件(2s) + 其他操作 ≈ 2000ms+
异步方式：主流程耗时 = 注册逻辑 + 其他操作 ≈ 20ms（邮件在后台发送）
```

**配置异步**：
```java
@Configuration
@EnableAsync  // 启用异步支持
public class AsyncConfig {
    // 使用 Spring Boot 默认的异步配置
}
```

### 📊 线程执行模型详解

#### 1️⃣ 同步监听（默认）

```
主线程执行流程：
┌─────────────────────────────────────────────────────────────┐
│ UserService.register()                                      │
│   ├─ 注册逻辑                                               │
│   ├─ 发布事件 eventPublisher.publishEvent()                │
│   │   └─ 触发监听器（在同一线程中）                        │
│   │       ├─ EmailListener.sendEmail()      ← 同一线程     │
│   │       ├─ PointsListener.grantPoints()   ← 同一线程     │
│   │       └─ StatisticsListener.record()    ← 同一线程     │
│   └─ 返回                                                   │
└─────────────────────────────────────────────────────────────┘
```

**特点**：
- 发布事件和监听器处理都在**同一个线程**（main 线程）
- 监听器按顺序执行完毕后，`register()` 方法才返回
- 如果某个监听器耗时 2 秒，主流程就会被阻塞 2 秒

#### 2️⃣ 异步监听

```
主线程：                          异步线程池：
┌──────────────────────┐         ┌─────────────────────────┐
│ UserService.register()│         │                         │
│   ├─ 注册逻辑        │         │                         │
│   ├─ 发布事件        │────────>│ task-1 线程             │
│   │                  │         │  └─ AsyncEmailListener  │
│   └─ 立即返回 ✓     │         │     └─ sendEmail() 2s   │
└──────────────────────┘         └─────────────────────────┘
     耗时: ~20ms                        耗时: 2000ms
                                       (不阻塞主线程)
```

**特点**：
- 发布事件在**主线程**
- 异步监听器在**线程池的独立线程**中执行
- 主流程立即返回，不等待异步监听器完成
- 即使监听器耗时 2 秒，主流程也只需 ~20ms

#### 3️⃣ 传统直接调用

```
主线程执行流程：
┌─────────────────────────────────────────────────────────────┐
│ TraditionalUserService.register()                           │
│   ├─ 注册逻辑                                               │
│   ├─ emailService.sendEmail()          ← 同一线程，高耦合  │
│   ├─ pointsService.grantPoints()       ← 同一线程，高耦合  │
│   ├─ statisticsService.record()        ← 同一线程，高耦合  │
│   └─ 返回                                                   │
└─────────────────────────────────────────────────────────────┘
```

**特点**：
- 都在**同一个线程**执行（与同步监听相同）
- **高耦合**：UserService 必须依赖所有服务
- 难以扩展和测试

### 🎯 三种方式对比总结

| 方式 | 线程模型 | 是否阻塞 | 耦合度 | 核心优势 |
|------|---------|---------|--------|---------|
| **传统直接调用** | 同一线程 | ✅ 阻塞 | ❌ **高耦合** | 无 |
| **同步监听** | 同一线程 | ✅ 阻塞 | ✅ **低耦合** | 解耦依赖、易扩展 |
| **异步监听** | 不同线程 | ❌ **不阻塞** | ✅ **低耦合** | 解耦依赖、易扩展、高性能 |

**关键理解**：
- **同步监听 vs 传统调用**：线程模型相同（都是同步执行），**核心区别在于架构层面的解耦**，而非性能
- **异步监听的价值**：既有解耦优势，又有性能优势（不阻塞主流程）
- **选择建议**：
  - 快速操作（如记录日志）→ 同步监听
  - 耗时操作（如发送邮件、调用第三方 API）→ 异步监听

## 🔑 关键特性

### 1. **解耦业务逻辑**
事件发布者无需知道谁在监听事件，监听器也无需知道谁发布了事件，实现了松耦合。

### 2. **异步处理（可选）**
可以通过 `@Async` 注解实现异步事件处理：

```java
@Async
@EventListener
public void handleUserRegisterEvent(UserRegisterEvent event) {
    // 异步处理
}
```

### 3. **事件顺序控制**
可以使用 `@Order` 注解控制多个监听器的执行顺序：

```java
@Order(1)
@EventListener
public void firstListener(UserRegisterEvent event) { }

@Order(2)
@EventListener
public void secondListener(UserRegisterEvent event) { }
```

### 4. **条件监听**
可以通过 SpEL 表达式实现条件监听：

```java
@EventListener(condition = "#event.username == 'admin'")
public void handleAdminRegister(UserRegisterEvent event) {
    // 仅处理管理员注册
}
```

## 📚 使用场景

Spring 事件机制适用于以下场景：

- ✅ **用户注册后发送欢迎邮件**
- ✅ **订单创建后发送通知、更新库存**
- ✅ **日志记录和审计**
- ✅ **缓存更新**
- ✅ **统计和分析**

## 🛠️ 技术栈

- Spring Boot 2.7.18
- Lombok
- JUnit 5

## 📖 扩展阅读

- [Spring 官方文档 - Application Events](https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events)
- [Spring Boot 事件机制详解](https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2)

## 📝 注意事项

1. **默认同步执行**: 事件监听器默认在发布事件的同一线程中同步执行
2. **异常处理**: 监听器中的异常会影响事件发布者，建议做好异常处理
3. **性能考虑**: 如果监听器执行耗时操作，建议使用异步处理
4. **事务边界**: 注意事件发布和监听的事务边界问题

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！
