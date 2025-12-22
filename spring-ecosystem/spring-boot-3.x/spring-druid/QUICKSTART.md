# Alibaba Druid 数据源示例 - 快速开始

## ✨ 项目简介

这是一个完整的 Alibaba Druid 数据库连接池示例项目，展示了如何在 Spring Boot 3.x 中集成和使用 Druid，包括：

- ✅ Druid 连接池配置
- ✅ SQL 监控和统计
- ✅ Web 监控页面
- ✅ SQL 防火墙
- ✅ 慢 SQL 记录
- ✅ 连接池状态监控

## 🚀 快速运行

### 1. 启动应用

```bash
cd /Users/wxg/my-projects/java-labs/spring-ecosystem/spring-boot-3.x/spring-druid
mvn spring-boot:run
```

### 2. 访问监控页面

打开浏览器访问：**http://localhost:8080/druid**

- 用户名：`admin`
- 密码：`admin123`

### 3. 测试 API

```bash
# 查看所有用户
curl http://localhost:8080/api/users

# 查看单个用户
curl http://localhost:8080/api/users/1

# 搜索用户
curl "http://localhost:8080/api/users/search?username=张"

# 查看连接池统计
curl http://localhost:8080/api/users/druid/stats
```

## 📊 监控页面功能

### 数据源监控
- 连接池配置信息
- 实时连接池状态
- 连接池历史统计图表

### SQL 监控
- SQL 执行次数统计
- SQL 执行时间分析
- 慢 SQL 记录
- SQL 错误统计

### URI 监控
- HTTP 请求统计
- 请求响应时间
- 并发数统计

## 🔧 核心配置

### Druid 连接池配置

```yaml
spring:
  datasource:
    druid:
      initial-size: 5          # 初始化连接数
      min-idle: 5              # 最小空闲连接数
      max-active: 20           # 最大活跃连接数
      test-while-idle: true    # 空闲时检测连接有效性
```

### 监控配置

```yaml
spring:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        login-username: admin
        login-password: admin123
```

### 慢 SQL 配置

```yaml
spring:
  datasource:
    druid:
      connection-properties: druid.stat.slowSqlMillis=5000
```

## 📝 代码示例

### 1. 使用 JdbcTemplate

```java
@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", USER_ROW_MAPPER);
    }
}
```

### 2. 获取连接池统计

```java
@Service
public class UserService {
    private final DataSource dataSource;
    
    public String getDruidStatistics() {
        if (dataSource instanceof DruidDataSource druidDataSource) {
            return "活跃连接数: " + druidDataSource.getActiveCount();
        }
        return "非 Druid 数据源";
    }
}
```

## 🧪 运行测试

```bash
mvn test -pl spring-druid
```

测试包含：
- ✅ 数据源类型验证
- ✅ Druid 配置验证
- ✅ CRUD 操作测试
- ✅ 批量操作测试
- ✅ 连接池统计测试

## 📚 学习要点

1. **连接池配置**：理解各项参数的含义和调优方法
2. **监控页面**：学会使用 Druid 监控页面分析 SQL 性能
3. **SQL 防火墙**：了解如何防止 SQL 注入攻击
4. **慢 SQL 优化**：通过慢 SQL 记录发现性能问题

## 🎯 最佳实践

### 生产环境配置建议

```yaml
spring:
  datasource:
    druid:
      initial-size: 10
      min-idle: 10
      max-active: 50
      max-wait: 60000
      
      # 连接泄漏检测
      remove-abandoned: true
      remove-abandoned-timeout: 180
      log-abandoned: true
      
      # 监控页面安全
      stat-view-servlet:
        allow: 192.168.1.0/24    # 仅允许内网访问
        reset-enable: false       # 禁止重置统计数据
```

## 📖 相关文档

- [Druid 官方文档](https://github.com/alibaba/druid/wiki)
- [完整 README](README.md)

---

**作者**: daihaowxg  
**创建时间**: 2025-12-22
