# Spring JDBC & MyBatis Demo

本模块演示了 Java 中三种常见的数据库访问方式：
1. **Standard JDBC** (原生 JDBC)
2. **Spring JDBC** (JdbcTemplate)
3. **MyBatis**

通过对比同一个 `User` 实体类的 CRUD 操作，展示各自的特点、优缺点及适用场景。

## 1. 原生 JDBC (Standard JDBC)

代码位置: [`StandardJdbcRepository`](src/main/java/io/github/daihaowxg/spring/jdbc/repository/StandardJdbcRepository.java)

这是 Java 标准库提供的最底层的数据库访问方式。

### 特点
- **手动管理资源**: 必须通过 `try-with-resources` 或 `finally` 块显式关闭 `Connection`, `Statement`, `ResultSet`。
- **样板代码多**: 获取连接、预编译语句、设置参数、处理结果集都需要大量代码。
- **受检异常**: 必须捕获处理 `SQLException`。

### 优点
- **无依赖**: JDK 自带，不需要引入额外库。
- **极致性能/控制**: 最底层，理论上开销最小，控制最精细。

### 缺点
- **繁琐**: 开发效率低，容易出错（如忘记关闭资源）。
- **可读性差**: 业务逻辑被大量 JDBC API 淹没。

---

## 2. Spring JDBC (JdbcTemplate)

代码位置: [`SpringJdbcRepository`](src/main/java/io/github/daihaowxg/spring/jdbc/repository/SpringJdbcRepository.java)

Spring 对 JDBC 的轻量级封装。

### 特点
- **封装资源管理**: Spring 帮你打开和关闭连接，处理事务。
- **异常转换**: 将受检的 `SQLException` 转换为非受检的 `DataAccessException` 体系，便于处理。
- **保留 SQL**: 依然直接编写 SQL，但 API 更加人性化（如 `queryForObject`, `update` 等）。

### 优点
- **简洁**: 消除 90% 的样板代码。
- **灵活**: 依然可以直接控制 SQL，适合复杂查询。
- **Spring 集成**: 天然集成 Spring 事务管理、依赖注入。

### 缺点
- **SQL 硬编码**: SQL 依然以字符串形式散落在 Java 代码中，维护稍显不便。
- **映射相对手动**: 虽然有 `RowMapper`，但相比 ORM 框架，对象关系映射依然需要手动配置。

---

## 3. MyBatis

代码位置: [`UserMapper`](src/main/java/io/github/daihaowxg/spring/jdbc/mapper/UserMapper.java) & [`UserMapper.xml`](src/main/resources/mapper/UserMapper.xml)

流行的 SQL Mapper 框架（半自动 ORM）。

### 特点
- **SQL 与代码分离**: SQL 通常写在 XML 中（也可以用注解），Java 代码中只有干净的接口。
- **动态 SQL**: 强大的 XML 标签 (`<if>`, `<foreach>`) 支持构建复杂的动态 SQL。
- **ResultMap**: 提供强大的结果集映射功能，解决字段名不一致、复杂的关联查询（一对一、一对多）。

### 优点
- **解耦**: 易于 DBA 审查和优化 SQL。
- **清晰**: Java 接口非常干净。
- **强大**: 动态 SQL 是其杀手锏，非常适合国内复杂的业务报表查询场景。

### 缺点
- **配置**: 需要额外的依赖和配置（XML 路径等）。
- **XML 繁琐**: 简单的 CRUD 也要写 XML，开发效率不如 JPA/Hibernate（但可以使用 MyBatis-Plus 解决）。

---

## 总结对比

| 特性 | Standard JDBC | Spring JDBC | MyBatis |
| :--- | :--- | :--- | :--- |
| **样板代码** | 很多 (Connection, Try-Catch) | 很少 (JdbcTemplate 封装) | 极少 (只写接口和 SQL) |
| **SQL 位置** | Java 字符串 | Java 字符串 | XML 或 注解 |
| **资源管理** | 手动 | 自动 | 自动 |
| **异常处理** | 受检异常 (SQLException) | 运行时异常 (DataAccessException) | 运行时异常 |
| **动态 SQL** | 手动拼接字符串 (极易出错) | 如果用 Java 拼接也比较痛苦 | XML 标签支持，非常强大 |
| **学习曲线** | 低 (基础) | 低 | 中 (需学习 XML 标签和配置) |
| **适用场景** | 极少使用 (除非写框架) | 简单项目，不喜欢 ORM | 复杂 SQL，国内企业级应用主流 |

### 适用场景
- 极少使用 (除非写框架) | 简单项目，不喜欢 ORM | 复杂 SQL，国内企业级应用主流 |

## 4. 事务管理 (Transaction Management)

在 Spring Boot 中，无论是 **Spring JDBC** 还是 **MyBatis**，事务管理通常都是统一由 Spring 的声明式事务 (`@Transactional`) 来接管的。

### 原理简述
- **Spring JDBC (`JdbcTemplate`)**: `JdbcTemplate` 会在每次执行 SQL 前通过 `DataSourceUtils` 获取当前线程绑定的数据库连接（如果当前处于事务中）。
- **MyBatis (`mybatis-spring`)**: MyBatis-Spring 集成库提供了一个 `SpringManagedTransaction` 类。当扫描到 Spring 管理的事务时，它会将 MyBatis 的 `SqlSession` 与 Spring 的当前事务连接同步。

### 结论
这意味着你可以在同一个 `@Transactional` 方法中混合使用 `JdbcTemplate` 和 `MyBatis Mapper`，它们会共享同一个数据库连接，从而保证在同一个事务中提交或回滚。

例如：
```java
@Transactional
public void mixedTransaction() {
    // 这一步使用 JdbcTemplate
    jdbcTemplate.update("INSERT ..."); 
    
    // 这一步使用 MyBatis
    userMapper.save(user);
    
    // 发生异常，两者都会回滚
    throw new RuntimeException("Rollback all");
}
```

### 建议
- 如果你是写一个没有很多依赖的小工具：**Standard JDBC** (或者 Apache Commons DbUtils)。
- 如果你在 Spring 环境下，需要灵活控制 SQL 且不想过度配置：**Spring JDBC**。
- 如果你的业务有大量复杂的查询、需要动态 SQL、或者团队习惯于 SQL 调优：**MyBatis** (这是目前国内最主流的选择)。

## 5. 常见问题 (FAQ)

### 为什么 Connection 和 PreparedStatement 必须关闭？

这两个对象代表了昂贵的底层**物理资源**，必须显式关闭以防止资源泄漏。

#### 1. Connection (数据库连接)
*   **有限的资源**: 数据库服务器允许的同时连接数是有限的（例如 MySQL 默认通常是 151 个）。
*   **网络开销**: `Connection` 在底层封装了一个 TCP Socket 连接。保持连接打开会占用客户端和服务器端的内存及网络端口。
*   **连接池耗尽**: 在现代应用中（如 Spring Boot），我们通常使用**连接池** (HikariCP)。调用 `connection.close()` 并不是真正切断 TCP 连接，而是将连接**归还**给连接池。如果你不关闭它，连接池会认为该连接仍被占用。一旦所有连接都被借出且不归还，新的请求就会因为无法获取连接而阻塞（Connection Pool Starvation），导致整个应用瘫痪。

#### 2. PreparedStatement (预编译语句)
*   **数据库游标 (Cursors)**: `Statement` 或 `PreparedStatement` 通常对应数据库端的“游标”或“句柄”。数据库对每个连接允许打开的游标数量也是有限制的（例如 Oracle 的 `OPEN_CURSORS` 参数）。
*   **内存泄漏**: 每个打开的 Statement 都会在数据库端和驱动层占用内存。如果不关闭，即使 Connection 最终被回收，长时间积累的未关闭 Statement 也可能导致数据库端的资源耗尽（如 Oracle 常见的 `ORA-01000: maximum open cursors exceeded` 错误）。

> **最佳实践**: 始终使用 `try-with-resources` (Java 7+) 来自动关闭这些资源。

