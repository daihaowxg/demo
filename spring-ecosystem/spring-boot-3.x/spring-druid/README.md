# Spring Boot + Alibaba Druid 示例

本示例演示了如何在 Spring Boot 3.x 项目中集成和使用 Alibaba Druid 数据库连接池。

## 📚 功能特性

### 1. Druid 连接池配置
- ✅ 连接池基本参数配置（初始化大小、最小/最大连接数等）
- ✅ 连接有效性检测配置
- ✅ 连接池性能优化配置

### 2. SQL 监控统计
- ✅ SQL 执行统计
- ✅ 慢 SQL 记录（默认 5 秒）
- ✅ SQL 合并统计
- ✅ Web 应用统计

### 3. 监控页面
- ✅ 内置监控页面（访问 `/druid`）
- ✅ 数据源信息展示
- ✅ SQL 监控
- ✅ URI 监控
- ✅ Session 监控
- ✅ Spring 监控

### 4. 安全防护
- ✅ SQL 防火墙（Wall Filter）
- ✅ 防止 SQL 注入
- ✅ 监控页面登录认证

### 5. 编程式访问
- ✅ 通过 API 获取连接池统计信息
- ✅ 运行时监控连接池状态

## 🚀 快速开始

### 1. 启动应用

```bash
cd spring-druid
mvn spring-boot:run
```

### 2. 访问监控页面

打开浏览器访问：http://localhost:8080/druid

- **用户名**: `admin`
- **密码**: `admin123`

### 3. 测试 API 接口

```bash
# 获取所有用户
curl http://localhost:8080/api/users

# 获取单个用户
curl http://localhost:8080/api/users/1

# 搜索用户
curl http://localhost:8080/api/users/search?username=张

# 获取连接池统计信息
curl http://localhost:8080/api/users/druid/stats

# 创建用户
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"username":"新用户","email":"new@example.com","age":25}'
```

### 4. 运行测试

```bash
mvn test
```

## 📖 核心配置说明

### application.yml 配置

```yaml
spring:
  datasource:
    druid:
      # 连接池配置
      initial-size: 5              # 初始化连接数
      min-idle: 5                  # 最小空闲连接数
      max-active: 20               # 最大活跃连接数
      max-wait: 60000              # 获取连接最大等待时间（毫秒）
      
      # 连接检测配置
      test-while-idle: true        # 空闲时检测连接有效性
      validation-query: SELECT 1   # 验证查询 SQL
      
      # 监控配置
      filters: stat,wall,slf4j     # 启用监控、防火墙、日志
      
      # 慢 SQL 配置
      connection-properties: druid.stat.slowSqlMillis=5000
      
      # 监控页面配置
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        login-username: admin
        login-password: admin123
```

## 🔍 监控页面功能

### 1. 数据源页面
- 查看连接池配置信息
- 实时连接池状态（活跃连接、空闲连接等）
- 连接池历史统计

### 2. SQL 监控页面
- SQL 执行次数统计
- SQL 执行时间统计（最大、最小、平均）
- 慢 SQL 记录
- SQL 执行错误统计

### 3. URI 监控页面
- HTTP 请求统计
- 请求响应时间
- 并发数统计

### 4. Spring 监控页面
- Spring Bean 方法调用统计
- 方法执行时间统计

## 📊 连接池统计信息

通过编程方式获取连接池统计：

```java
@Autowired
private DataSource dataSource;

public void printStats() {
    if (dataSource instanceof DruidDataSource druidDataSource) {
        System.out.println("活跃连接数: " + druidDataSource.getActiveCount());
        System.out.println("空闲连接数: " + druidDataSource.getPoolingCount());
        System.out.println("等待线程数: " + druidDataSource.getWaitThreadCount());
        System.out.println("创建连接总数: " + druidDataSource.getCreateCount());
        // ... 更多统计信息
    }
}
```

## 🛡️ 安全配置

### 1. SQL 防火墙

Druid 的 Wall Filter 可以防止 SQL 注入攻击：

```yaml
spring:
  datasource:
    druid:
      wall:
        config:
          multi-statement-allow: true   # 是否允许多语句
          delete-allow: true             # 是否允许删除
          drop-table-allow: false        # 是否允许删除表
```

### 2. 监控页面访问控制

```yaml
spring:
  datasource:
    druid:
      stat-view-servlet:
        login-username: admin           # 登录用户名
        login-password: admin123        # 登录密码
        allow: 127.0.0.1               # IP 白名单
        deny: 192.168.1.100            # IP 黑名单
```

## 🎯 最佳实践

### 1. 连接池大小配置

```yaml
# 根据实际业务场景调整
initial-size: 5      # 启动时创建的连接数
min-idle: 5          # 保持的最小空闲连接数
max-active: 20       # 最大连接数（根据数据库和应用服务器资源调整）
```

**建议**：
- `max-active` = (核心线程数 × 2) + 磁盘数量
- 监控实际使用情况后调整

### 2. 连接有效性检测

```yaml
test-while-idle: true                        # 推荐开启
test-on-borrow: false                        # 不推荐，影响性能
test-on-return: false                        # 不推荐，影响性能
time-between-eviction-runs-millis: 60000     # 检测间隔
min-evictable-idle-time-millis: 300000       # 连接最小空闲时间
```

### 3. 慢 SQL 监控

```yaml
# 记录执行时间超过 5 秒的 SQL
connection-properties: druid.stat.slowSqlMillis=5000
```

### 4. 生产环境配置

```yaml
spring:
  datasource:
    druid:
      # 生产环境建议配置
      initial-size: 10
      min-idle: 10
      max-active: 50
      max-wait: 60000
      
      # 开启连接泄漏检测
      remove-abandoned: true
      remove-abandoned-timeout: 180
      log-abandoned: true
      
      # 监控页面访问控制
      stat-view-servlet:
        enabled: true
        allow: 192.168.1.0/24    # 仅允许内网访问
        reset-enable: false       # 禁止重置统计数据
```

## 📝 项目结构

```
spring-druid/
├── src/main/java/io/github/daihaowxg/druid/
│   ├── DruidApplication.java          # 启动类
│   ├── controller/
│   │   └── UserController.java        # REST API 控制器
│   ├── service/
│   │   └── UserService.java           # 业务服务层
│   ├── repository/
│   │   └── UserRepository.java        # 数据访问层
│   └── entity/
│       └── User.java                   # 实体类
├── src/main/resources/
│   ├── application.yml                 # 应用配置
│   ├── schema.sql                      # 数据库表结构
│   └── data.sql                        # 初始化数据
└── src/test/java/
    └── DruidApplicationTests.java      # 测试类
```

## 🔗 相关链接

- [Druid 官方文档](https://github.com/alibaba/druid/wiki)
- [Druid Spring Boot Starter](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)
- [Druid 监控配置](https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatViewServlet%E9%85%8D%E7%BD%AE)

## 📌 注意事项

1. **H2 数据库仅用于演示**：生产环境请使用 MySQL、PostgreSQL 等数据库
2. **监控页面安全**：生产环境务必配置强密码和 IP 白名单
3. **连接池大小**：根据实际负载调整，避免过大或过小
4. **慢 SQL 阈值**：根据业务需求调整 `slowSqlMillis` 参数
5. **PSCache**：MySQL 建议关闭，Oracle/PostgreSQL 建议开启

## 🎓 学习要点

1. ✅ 理解数据库连接池的作用和原理
2. ✅ 掌握 Druid 的配置方法
3. ✅ 学会使用 Druid 监控页面分析 SQL 性能
4. ✅ 了解 SQL 防火墙的作用
5. ✅ 掌握连接池参数调优方法

---

**作者**: daihaowxg  
**创建时间**: 2025-12-22
