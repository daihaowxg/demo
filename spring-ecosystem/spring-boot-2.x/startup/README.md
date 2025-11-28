# Lab00: Spring Boot Startup

## 项目简介

这是一个 Spring Boot 基础启动项目,演示了如何创建一个简单的 Spring Boot 应用,并在启动时输出应用访问信息。

## 技术栈

- **Spring Boot**: 2.7.18
- **Java**: 基于 Maven 构建
- **Lombok**: 简化 Java 代码

## 核心依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
```

## 主要功能

### 1. 启动信息展示

项目实现了一个自定义的 `ApplicationRunner`,在应用启动完成后自动输出以下信息:

- **应用名称**: 从 `spring.application.name` 配置读取
- **本地访问地址**: `http://localhost:{port}{context-path}/`
- **外部访问地址**: `http://{ip}:{port}{context-path}/`
- **API 文档地址**: `http://{ip}:{port}{context-path}/swagger-ui.html`

### 2. 核心代码

[DemoApplication.java](file:///Users/wxg/my-projects/java-labs/lab00_startup/src/main/java/io/github/daihaowxg/demo/DemoApplication.java) 包含:

- Spring Boot 主启动类
- ApplicationRunner Bean,用于启动后输出访问信息
- 自动获取本机 IP 地址和配置的端口号

## 运行项目

### 方式一: 使用 Maven 命令

```bash
# 在项目根目录执行
mvn clean install

# 进入 lab00_startup 目录
cd lab00_startup

# 运行应用
mvn spring-boot:run
```

### 方式二: 使用 IDE

1. 在 IDE 中打开项目
2. 找到 `DemoApplication.java`
3. 右键运行 `main` 方法

## 启动效果

应用启动成功后,控制台会输出类似以下信息:

```
----------------------------------------------------------
Application demo is running! Access URLs:
Local: 		http://localhost:8080/
External: 	http://192.168.1.100:8080/
Document: 	http://192.168.1.100:8080/swagger-ui.html
------------------------------------------------------------
```

## 配置说明

可以通过 `application.properties` 或 `application.yml` 配置以下参数:

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `spring.application.name` | 应用名称 | - |
| `server.port` | 服务端口 | 8080 |
| `server.servlet.context-path` | 应用上下文路径 | / |

## 项目结构

```
lab00_startup/
├── pom.xml                                    # Maven 配置文件
└── src/
    └── main/
        └── java/
            └── io/github/daihaowxg/demo/
                └── DemoApplication.java       # 主启动类
```

## 学习要点

1. **Spring Boot 基础启动流程**
   - `@SpringBootApplication` 注解的使用
   - `SpringApplication.run()` 方法

2. **ApplicationRunner 接口**
   - 在应用启动完成后执行自定义逻辑
   - 获取 Environment 配置信息

3. **网络编程基础**
   - 使用 `InetAddress.getLocalHost()` 获取本机 IP
   - 理解本地地址和外部地址的区别

## 扩展建议

- 添加 `application.yml` 配置文件,自定义应用名称和端口
- 集成 Swagger/OpenAPI 文档,使文档链接可用
- 添加健康检查端点 (Spring Boot Actuator)
- 实现简单的 REST API 接口

## 相关实验

本项目是 Java Labs 系列的第一个实验,更多实验请参考:

- [lab01_java](../lab01_java) - Java 基础特性
- [lab08_spring](../lab08_spring) - Spring 框架深入
- [lab14_spring_boot_logging](../lab14_spring_boot_logging) - Spring Boot 日志配置
