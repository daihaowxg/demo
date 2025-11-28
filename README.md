# Java Labs

## 项目简介

这是一个 Java 和 Spring Boot 学习实验项目，包含从基础语法到高级框架集成的各类示例。项目按照技术栈和版本进行了分层组织，方便学习和查阅。

## 项目结构

```
java-labs/
├── java-fundamentals/           # Java 基础示例
│   ├── jdk8-examples/           # JDK 8 特性与基础 (集合, 并发, JVM)
│   └── jdk17-examples/          # JDK 17+ 新特性
│
├── spring-ecosystem/            # Spring 生态示例
│   ├── spring-boot-2.x/         # 基于 Spring Boot 2.7.x (JDK 8)
│   │   ├── startup/             # 启动示例
│   │   ├── spring-core/         # Spring 核心特性
│   │   ├── mybatis-integration/ # MyBatis 集成
│   │   ├── dynamic-datasource/  # 动态数据源
│   │   ├── sse/                 # Server-Sent Events
│   │   ├── kafka-integration/   # Kafka 集成
│   │   ├── redis-integration/   # Redis 集成
│   │   └── logging/             # 日志配置
│   │
│   └── spring-boot-3.x/         # 基于 Spring Boot 3.x (JDK 17+)
│       ├── spring-transaction/  # 事务管理
│       └── spring-cache/        # 缓存抽象
│
├── middleware/                  # 中间件集成
│   └── disruptor/               # LMAX Disruptor 高性能队列
│
├── design-patterns/             # 设计模式
│   └── patterns/                # 常用设计模式实现
│
├── algorithms/                  # 数据结构与算法
│   └── solutions/               # 算法题解
│
└── utilities/                   # 工具类与测试
    ├── hutool-examples/         # Hutool 工具库使用
    ├── junit-examples/          # JUnit 5 测试示例
    └── system-design/           # 系统设计案例
```

## 版本策略

项目采用分层版本策略，以适应不同的学习需求：

- **JDK 8**: 主要用于 `java-fundamentals/jdk8-examples` 和 `spring-ecosystem/spring-boot-2.x`。
- **JDK 17+**: 主要用于 `java-fundamentals/jdk17-examples` 和 `spring-ecosystem/spring-boot-3.x`。

## 快速开始

### 环境要求

- JDK 8 和 JDK 17+
- Maven 3.6+

### 构建项目

在根目录下执行：

```bash
mvn clean install -DskipTests
```

### 运行示例

进入具体的模块目录，例如运行 Spring Boot 启动示例：

```bash
cd spring-ecosystem/spring-boot-2.x/startup
mvn spring-boot:run
```
