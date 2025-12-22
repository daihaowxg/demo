# 多数据源 JdbcTemplate 使用指南

## 概述

在实际项目中,经常需要同时访问多个数据库。本示例演示了如何在 Spring Boot 中配置和使用多个 JdbcTemplate。

## 核心概念

### 1. 配置要点

每个数据源需要配置三个 Bean:
- **DataSource** - 数据源
- **JdbcTemplate** - JDBC 模板
- **PlatformTransactionManager** - 事务管理器

### 2. 关键注解

| 注解 | 作用 | 使用场景 |
|------|------|---------|
| `@Primary` | 标记主 Bean | 当有多个相同类型的 Bean 时,标记默认使用的 Bean |
| `@Qualifier` | 指定 Bean 名称 | 注入时明确指定要使用哪个 Bean |
| `@Transactional(transactionManager = "xxx")` | 指定事务管理器 | 在多数据源环境中明确指定使用哪个事务管理器 |

## 配置步骤

### 步骤 1: 配置文件 (application.yml)

```yaml
spring:
  datasource:
    # 主数据源配置
    primary:
      jdbc-url: jdbc:h2:mem:primarydb
      username: sa
      password: 
      driver-class-name: org.h2.Driver
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5
    
    # 第二个数据源配置
    secondary:
      jdbc-url: jdbc:h2:mem:secondarydb
      username: sa
      password: 
      driver-class-name: org.h2.Driver
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5
```

**注意:** 
- 使用 `jdbc-url` 而不是 `url` (多数据源配置的要求)
- 每个数据源使用不同的配置前缀

### 步骤 2: 创建配置类

参考 `MultiDataSourceConfig.java`:

```java
@Configuration
public class MultiDataSourceConfig {
    
    // 主数据源 (使用 @Primary 标记)
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Primary
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    // 第二个数据源 (不使用 @Primary)
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }
    
    @Bean(name = "secondaryJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

### 步骤 3: 使用 JdbcTemplate

参考 `MultiDataSourceRepository.java`:

```java
@Repository
public class MultiDataSourceRepository {
    
    private final JdbcTemplate primaryJdbcTemplate;
    private final JdbcTemplate secondaryJdbcTemplate;
    
    // 使用 @Qualifier 注入不同的 JdbcTemplate
    public MultiDataSourceRepository(
            @Qualifier("primaryJdbcTemplate") JdbcTemplate primaryJdbcTemplate,
            @Qualifier("secondaryJdbcTemplate") JdbcTemplate secondaryJdbcTemplate) {
        this.primaryJdbcTemplate = primaryJdbcTemplate;
        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
    }
    
    // 使用主数据源
    public void saveUserToPrimary(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        primaryJdbcTemplate.update(sql, user.getName(), user.getEmail());
    }
    
    // 使用第二个数据源
    public void saveUserToSecondary(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        secondaryJdbcTemplate.update(sql, user.getName(), user.getEmail());
    }
}
```

## 使用场景

### 场景 1: 单数据源操作

```java
// 从主数据源查询
@Transactional(transactionManager = "primaryTransactionManager")
public void saveUserToPrimary(User user) {
    primaryJdbcTemplate.update(
        "INSERT INTO users (name, email) VALUES (?, ?)",
        user.getName(), user.getEmail()
    );
}

// 从第二个数据源查询
@Transactional(transactionManager = "secondaryTransactionManager")
public void saveUserToSecondary(User user) {
    secondaryJdbcTemplate.update(
        "INSERT INTO users (name, email) VALUES (?, ?)",
        user.getName(), user.getEmail()
    );
}
```

### 场景 2: 跨数据源操作

```java
// 注意: 没有 @Transactional,因为涉及多个数据源
public void syncUserBetweenDataSources(Long id) {
    // 从主数据源查询
    Optional<User> user = findUserFromPrimary(id);
    
    // 保存到第二个数据源
    if (user.isPresent()) {
        saveUserToSecondary(user.get());
    }
}
```

**重要提示:** 
- 跨数据源操作默认**不保证事务一致性**
- 如果需要分布式事务,需要使用 JTA 或其他分布式事务解决方案

### 场景 3: 动态选择数据源

```java
public Optional<User> findUserDynamically(Long id, boolean usePrimarySource) {
    JdbcTemplate jdbcTemplate = usePrimarySource 
        ? primaryJdbcTemplate 
        : secondaryJdbcTemplate;
    
    return jdbcTemplate.query(
        "SELECT * FROM users WHERE id = ?",
        rs -> rs.next() ? Optional.of(mapUser(rs)) : Optional.empty(),
        id
    );
}
```

## 常见问题

### Q1: 为什么要使用 @Primary?

**A:** 当有多个相同类型的 Bean 时,Spring 不知道应该注入哪一个。使用 `@Primary` 标记默认的 Bean,这样在没有明确指定 `@Qualifier` 时,会使用主 Bean。

### Q2: @Transactional 必须指定 transactionManager 吗?

**A:** 在多数据源环境中,**强烈建议**明确指定 `transactionManager`,否则会使用默认的事务管理器(标记了 `@Primary` 的那个)。

```java
// ✅ 推荐: 明确指定事务管理器
@Transactional(transactionManager = "secondaryTransactionManager")
public void saveUser(User user) { ... }

// ⚠️ 不推荐: 会使用 primaryTransactionManager (因为它是 @Primary)
@Transactional
public void saveUser(User user) { ... }
```

### Q3: 如何实现分布式事务?

**A:** 多数据源的分布式事务需要额外的配置:

1. **使用 JTA (Java Transaction API)**
   - 需要 JTA 事务管理器(如 Atomikos, Bitronix)
   - 配置复杂,性能开销大

2. **使用消息队列实现最终一致性**
   - 推荐用于微服务架构
   - 通过消息队列保证最终一致性

3. **使用 Seata 等分布式事务框架**
   - 适合微服务场景
   - 提供 AT、TCC、SAGA 等多种模式

### Q4: 配置文件中为什么用 jdbc-url 而不是 url?

**A:** 在多数据源配置中,Spring Boot 要求使用 `jdbc-url` 而不是 `url`,这是为了避免与单数据源配置冲突。

```yaml
# ❌ 错误
spring.datasource.primary.url=jdbc:h2:mem:primarydb

# ✅ 正确
spring.datasource.primary.jdbc-url=jdbc:h2:mem:primarydb
```

## 对比: 单数据源 vs 多数据源

| 特性 | 单数据源 | 多数据源 |
|------|---------|---------|
| **配置复杂度** | 简单 | 复杂 |
| **Bean 数量** | 3 个 (DataSource, JdbcTemplate, TransactionManager) | 每个数据源 3 个 |
| **@Primary 注解** | 不需要 | 需要标记主数据源 |
| **@Qualifier 注解** | 不需要 | 需要明确指定 Bean |
| **@Transactional** | 不需要指定 transactionManager | 建议明确指定 |
| **分布式事务** | 不涉及 | 需要额外配置 |

## 最佳实践

1. ✅ **使用 @Primary 标记主数据源**,简化大部分场景的使用
2. ✅ **明确指定 transactionManager**,避免事务管理器混淆
3. ✅ **使用 @Qualifier 注入**,代码更清晰
4. ✅ **为每个数据源使用独立的配置前缀**
5. ⚠️ **谨慎处理跨数据源操作**,考虑数据一致性问题
6. ⚠️ **避免在同一个事务中操作多个数据源**,除非使用分布式事务

## 参考文件

- 配置类: `MultiDataSourceConfig.java`
- 使用示例: `MultiDataSourceRepository.java`
- 单数据源示例: `DataSourceUtilsRepository.java`
