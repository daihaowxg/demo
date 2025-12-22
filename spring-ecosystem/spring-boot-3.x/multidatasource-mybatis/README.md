# MyBatis 多数据源配置指南

## 📚 **概述**

本文档详细说明如何在 Spring Boot 中配置 MyBatis 多数据源。

## 🎯 **核心概念**

### MyBatis 多数据源需要配置的组件

每个数据源需要配置 **4 个核心 Bean**：

| Bean | 作用 | 说明 |
|------|------|------|
| `DataSource` | 数据源 | 提供数据库连接 |
| `SqlSessionFactory` | MyBatis 会话工厂 | 创建 SqlSession，MyBatis 的核心 |
| `SqlSessionTemplate` | MyBatis 会话模板 | 线程安全的 SqlSession |
| `TransactionManager` | 事务管理器 | 管理数据库事务 |

### 🔍 **为什么需要这 4 个 Bean？**

#### 1️⃣ **DataSource（数据源）**

**作用**：提供数据库连接

```java
@Bean(name = "primaryDataSource")
@ConfigurationProperties(prefix = "spring.datasource.primary")
public DataSource primaryDataSource() {
    return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .build();
}
```

**为什么需要**：
- ✅ 这是一切的基础，没有数据源就无法连接数据库
- ✅ 通过 `@ConfigurationProperties` 自动读取配置文件中的连接信息
- ✅ 每个数据库需要独立的 `DataSource`，因为它们的连接信息不同

**配置文件示例**：
```yaml
spring:
  datasource:
    primary:
      jdbc-url: jdbc:mysql://localhost:3306/db1
      username: root
      password: password
```

---

#### 2️⃣ **SqlSessionFactory（MyBatis 会话工厂）**

**作用**：MyBatis 的核心，用于创建 `SqlSession`

```java
@Bean(name = "primarySqlSessionFactory")
public SqlSessionFactory primarySqlSessionFactory(
        @Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);  // ← 关联到对应的 DataSource
    
    // 配置 MyBatis 行为
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true);  // 下划线转驼峰
    bean.setConfiguration(configuration);
    
    return bean.getObject();
}
```

**为什么需要**：
- ✅ `SqlSessionFactory` 是 MyBatis 的核心，所有 SQL 操作都需要通过它创建的 `SqlSession` 执行
- ✅ 它需要绑定到特定的 `DataSource`，这样才知道连接哪个数据库
- ✅ 每个数据源需要独立的 `SqlSessionFactory`，因为它们管理的是不同的数据库

**工作流程**：
```
SqlSessionFactory → 创建 SqlSession → 执行 SQL → 返回结果
```

**关键配置**：
- `setDataSource()` - 指定使用哪个数据源
- `setConfiguration()` - 配置 MyBatis 行为（如驼峰转换）
- `setMapperLocations()` - 指定 Mapper XML 文件位置（如果使用 XML）

---

#### 3️⃣ **SqlSessionTemplate（MyBatis 会话模板）**

**作用**：线程安全的 `SqlSession` 实现

```java
@Bean(name = "primarySqlSessionTemplate")
public SqlSessionTemplate primarySqlSessionTemplate(
        @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);  // ← 关联到对应的 SqlSessionFactory
}
```

**为什么需要**：
- ✅ **线程安全**：原生的 `SqlSession` 不是线程安全的，`SqlSessionTemplate` 解决了这个问题
- ✅ **自动管理**：自动处理 `SqlSession` 的打开和关闭，避免资源泄漏
- ✅ **Spring 集成**：与 Spring 的事务管理无缝集成

**对比原生 SqlSession**：

```java
// ❌ 原生 SqlSession（不线程安全，需要手动管理）
SqlSession sqlSession = sqlSessionFactory.openSession();
try {
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    mapper.insert(user);
    sqlSession.commit();
} finally {
    sqlSession.close();  // 必须手动关闭
}

// ✅ SqlSessionTemplate（线程安全，自动管理）
UserMapper mapper = sqlSessionTemplate.getMapper(UserMapper.class);
mapper.insert(user);  // 自动提交和关闭
```

**注意**：虽然配置了 `SqlSessionTemplate`，但在实际使用中，我们通常直接注入 Mapper 接口，Spring 会自动使用 `SqlSessionTemplate`。

#### 🤔 **Spring 自动使用的是我们创建的 SqlSessionTemplate 吗？**

**答案：不完全是！** 这里有一个重要的细节需要理解：

##### **实际工作机制**

1. **我们配置的 SqlSessionTemplate**：
   ```java
   @Bean(name = "primarySqlSessionTemplate")
   public SqlSessionTemplate primarySqlSessionTemplate(...) {
       return new SqlSessionTemplate(sqlSessionFactory);
   }
   ```
   - 这个 Bean **可以**被直接注入使用
   - 但在 Mapper 接口的场景下，它**不是必需的**

2. **Mapper 接口的创建过程**：
   ```java
   @MapperScan(
       basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
       sqlSessionFactoryRef = "primarySqlSessionFactory"  // ← 关键！
   )
   ```
   - `@MapperScan` 只需要 `sqlSessionFactoryRef`
   - **不需要** `sqlSessionTemplateRef`
   - MyBatis-Spring 会**自动创建**一个内部的 `SqlSessionTemplate`

3. **内部创建的 SqlSessionTemplate**：
   ```java
   // MyBatis-Spring 内部会这样做：
   SqlSessionTemplate internalTemplate = new SqlSessionTemplate(sqlSessionFactory);
   // 然后用这个 template 创建 Mapper 代理对象
   ```

##### **验证：SqlSessionTemplate 是否必需？**

让我们做个实验：

**场景 1：不配置 SqlSessionTemplate**
```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
    sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryMyBatisConfig {
    
    @Bean
    public DataSource primaryDataSource() { ... }
    
    @Bean
    public SqlSessionFactory primarySqlSessionFactory(...) { ... }
    
    // ❌ 不配置 SqlSessionTemplate
    // @Bean
    // public SqlSessionTemplate primarySqlSessionTemplate(...) { ... }
}
```
**结果**：✅ **Mapper 接口仍然可以正常工作！**

**场景 2：配置了 SqlSessionTemplate**
```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
    sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryMyBatisConfig {
    
    @Bean
    public DataSource primaryDataSource() { ... }
    
    @Bean
    public SqlSessionFactory primarySqlSessionFactory(...) { ... }
    
    // ✅ 配置 SqlSessionTemplate
    @Bean
    public SqlSessionTemplate primarySqlSessionTemplate(...) { ... }
}
```
**结果**：✅ **Mapper 接口也能正常工作，但不会使用我们配置的这个 Bean**

##### **那么 SqlSessionTemplate 什么时候有用？**

**使用场景 1：直接注入 SqlSessionTemplate**
```java
@Service
public class UserService {
    
    // 直接注入我们配置的 SqlSessionTemplate
    @Autowired
    @Qualifier("primarySqlSessionTemplate")
    private SqlSessionTemplate sqlSessionTemplate;
    
    public void saveUser(User user) {
        // 手动使用 SqlSessionTemplate
        UserMapper mapper = sqlSessionTemplate.getMapper(UserMapper.class);
        mapper.insert(user);
    }
}
```

**使用场景 2：通过 @MapperScan 的 sqlSessionTemplateRef 指定**
```java
@MapperScan(
    basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
    sqlSessionTemplateRef = "primarySqlSessionTemplate"  // ← 使用我们配置的
)
public class PrimaryMyBatisConfig {
    
    @Bean
    public SqlSessionTemplate primarySqlSessionTemplate(...) {
        // 可以在这里自定义 ExecutorType
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }
}
```

##### **最佳实践建议**

**推荐配置方式 1：只配置 SqlSessionFactory**（简化版）
```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
    sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryMyBatisConfig {
    
    @Bean
    public DataSource primaryDataSource() { ... }
    
    @Bean
    public SqlSessionFactory primarySqlSessionFactory(...) { ... }
    
    @Bean
    public PlatformTransactionManager primaryTransactionManager(...) { ... }
    
    // 不需要配置 SqlSessionTemplate，MyBatis-Spring 会自动创建
}
```
**优点**：
- ✅ 配置简单
- ✅ 减少 Bean 数量
- ✅ 满足大多数场景

**推荐配置方式 2：配置 SqlSessionTemplate**（完整版）
```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
    sqlSessionTemplateRef = "primarySqlSessionTemplate"  // 明确指定
)
public class PrimaryMyBatisConfig {
    
    @Bean
    public DataSource primaryDataSource() { ... }
    
    @Bean
    public SqlSessionFactory primarySqlSessionFactory(...) { ... }
    
    @Bean
    public SqlSessionTemplate primarySqlSessionTemplate(...) {
        // 可以自定义 ExecutorType
        return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
    }
    
    @Bean
    public PlatformTransactionManager primaryTransactionManager(...) { ... }
}
```
**优点**：
- ✅ 可以自定义 `ExecutorType`（如批量执行）
- ✅ 可以直接注入使用
- ✅ 配置更明确

##### **总结对比**

| 配置方式 | Bean 数量 | Mapper 工作方式 | 适用场景 |
|---------|----------|----------------|---------|
| **只配置 SqlSessionFactory** | 3 个 | MyBatis-Spring 自动创建内部 SqlSessionTemplate | 大多数场景 |
| **配置 SqlSessionTemplate + sqlSessionFactoryRef** | 4 个 | MyBatis-Spring 自动创建内部 SqlSessionTemplate（不使用我们的） | 需要直接注入 SqlSessionTemplate |
| **配置 SqlSessionTemplate + sqlSessionTemplateRef** | 4 个 | 使用我们配置的 SqlSessionTemplate | 需要自定义 ExecutorType |

##### **修正后的理解**

```java
// 我们的配置
@Bean(name = "primarySqlSessionTemplate")
public SqlSessionTemplate primarySqlSessionTemplate(...) {
    return new SqlSessionTemplate(sqlSessionFactory);
}

// 如果使用 sqlSessionFactoryRef
@MapperScan(
    basePackages = "...",
    sqlSessionFactoryRef = "primarySqlSessionFactory"  // ← 只指定 Factory
)
// → MyBatis-Spring 会创建一个新的内部 SqlSessionTemplate
// → 我们配置的 primarySqlSessionTemplate 不会被 Mapper 使用
// → 但可以被 @Autowired 直接注入使用

// 如果使用 sqlSessionTemplateRef
@MapperScan(
    basePackages = "...",
    sqlSessionTemplateRef = "primarySqlSessionTemplate"  // ← 直接指定 Template
)
// → MyBatis-Spring 会使用我们配置的 primarySqlSessionTemplate
// → 这才是真正使用我们创建的 Bean
```

##### **实际项目建议**

对于多数据源场景：

**简化配置**（推荐新手）：
```java
// 每个数据源只需要 3 个 Bean
- DataSource
- SqlSessionFactory
- TransactionManager
```

**完整配置**（推荐生产）：
```java
// 每个数据源配置 4 个 Bean，并使用 sqlSessionTemplateRef
- DataSource
- SqlSessionFactory
- SqlSessionTemplate
- TransactionManager

@MapperScan(
    basePackages = "...",
    sqlSessionTemplateRef = "primarySqlSessionTemplate"  // 明确指定
)
```

---

#### 4️⃣ **TransactionManager（事务管理器）**

**作用**：管理数据库事务

```java
@Bean(name = "primaryTransactionManager")
public PlatformTransactionManager primaryTransactionManager(
        @Qualifier("primaryDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);  // ← 关联到对应的 DataSource
}
```

**为什么需要**：
- ✅ **事务控制**：支持 `@Transactional` 注解，实现声明式事务
- ✅ **数据一致性**：确保一组操作要么全部成功，要么全部回滚
- ✅ **多数据源隔离**：每个数据源的事务独立管理，互不影响

**使用示例**：

```java
@Service
public class UserService {
    
    // 使用主数据源的事务管理器
    @Transactional(transactionManager = "primaryTransactionManager")
    public void saveUser(User user) {
        primaryUserMapper.insert(user);
        // 如果这里抛出异常，上面的插入会回滚
    }
    
    // 使用第二个数据源的事务管理器
    @Transactional(transactionManager = "secondaryTransactionManager")
    public void saveUserToSecondary(User user) {
        secondaryUserMapper.insert(user);
    }
}
```

**为什么必须指定 transactionManager**：
```java
// ❌ 错误：会使用默认的（@Primary 的）事务管理器
@Transactional
public void saveUser(User user) {
    secondaryUserMapper.insert(user);  // 可能用错事务管理器！
}

// ✅ 正确：明确指定
@Transactional(transactionManager = "secondaryTransactionManager")
public void saveUser(User user) {
    secondaryUserMapper.insert(user);
}
```

---

### 🔗 **4 个 Bean 之间的关系**

```
┌─────────────────────────────────────────────────────────┐
│                    数据源配置流程                          │
└─────────────────────────────────────────────────────────┘

1. DataSource (数据源)
   ↓ 提供数据库连接
   
2. SqlSessionFactory (MyBatis 核心)
   ↓ 使用 DataSource 创建 SqlSession
   
3. SqlSessionTemplate (线程安全包装)
   ↓ 包装 SqlSessionFactory，提供线程安全的操作
   
4. TransactionManager (事务管理)
   ↓ 使用 DataSource 管理事务
   
最终 → Mapper 接口可以正常工作
```

**依赖关系**：
- `SqlSessionFactory` 依赖 `DataSource`
- `SqlSessionTemplate` 依赖 `SqlSessionFactory`
- `TransactionManager` 依赖 `DataSource`
- `Mapper` 接口依赖 `SqlSessionFactory`（通过 `@MapperScan`）

---

### 📊 **与 JdbcTemplate 对比**

| 组件 | JdbcTemplate 多数据源 | MyBatis 多数据源 | 原因 |
|------|---------------------|-----------------|------|
| DataSource | ✅ 需要 | ✅ 需要 | 都需要数据库连接 |
| JdbcTemplate | ✅ 需要 | ❌ 不需要 | MyBatis 不使用 JdbcTemplate |
| SqlSessionFactory | ❌ 不需要 | ✅ 需要 | MyBatis 的核心组件 |
| SqlSessionTemplate | ❌ 不需要 | ✅ 需要 | MyBatis 的线程安全包装 |
| TransactionManager | ✅ 需要 | ✅ 需要 | 都需要事务管理 |

**总结**：
- JdbcTemplate：3 个 Bean（DataSource + JdbcTemplate + TransactionManager）
- MyBatis：4 个 Bean（DataSource + SqlSessionFactory + SqlSessionTemplate + TransactionManager）

---

### 💡 **实际使用时的简化**

虽然配置了 4 个 Bean，但在实际使用中非常简单：

```java
@Service
@RequiredArgsConstructor
public class UserService {
    
    // 直接注入 Mapper，Spring 会自动使用对应的 SqlSessionFactory
    private final PrimaryUserMapper primaryUserMapper;
    private final SecondaryUserMapper secondaryUserMapper;
    
    @Transactional(transactionManager = "primaryTransactionManager")
    public void saveUser(User user) {
        primaryUserMapper.insert(user);  // 就这么简单！
    }
}
```

**Spring 自动完成的工作**：
1. 根据 `@MapperScan` 找到 Mapper 接口
2. 使用对应的 `SqlSessionFactory` 创建 Mapper 实现
3. 通过 `SqlSessionTemplate` 执行 SQL
4. 使用 `TransactionManager` 管理事务

---

### ⚠️ **常见错误**

#### 错误 1：忘记配置 SqlSessionFactory
```
Error: No qualifying bean of type 'SqlSessionFactory'
```
**原因**：没有为数据源配置 `SqlSessionFactory`

#### 错误 2：Mapper 扫描配置错误
```
Error: Mapper interface not found
```
**原因**：`@MapperScan` 的 `sqlSessionFactoryRef` 没有正确指定

#### 错误 3：事务管理器使用错误
```
数据没有保存，但也没有报错
```
**原因**：`@Transactional` 使用了错误的 `transactionManager`

---

## 📝 **配置步骤**

### 步骤 1: 添加依赖（pom.xml）

```xml
<dependencies>
    <!-- MyBatis Spring Boot Starter -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>3.0.3</version>
    </dependency>
    
    <!-- 数据库驱动（示例使用 H2） -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

---

### 步骤 2: 配置文件（application.yml）

```yaml
spring:
  datasource:
    # 主数据源
    primary:
      jdbc-url: jdbc:h2:mem:primarydb
      username: sa
      password: 
      driver-class-name: org.h2.Driver
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5
    
    # 第二个数据源
    secondary:
      jdbc-url: jdbc:h2:mem:secondarydb
      username: sa
      password: 
      driver-class-name: org.h2.Driver
      hikari:
        maximum-pool-size: 10
        minimum-idle: 5

  # SQL 初始化配置
  sql:
    init:
      mode: always
      schema-locations:
        - classpath:schema-primary.sql
        - classpath:schema-secondary.sql

# MyBatis 配置
mybatis:
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl  # 日志实现
```

---

### 步骤 3: 创建主数据源配置类

```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.multidatasource.mapper.primary",
    sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryMyBatisConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(
            @Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        
        // MyBatis 配置
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Slf4jImpl.class);
        bean.setConfiguration(configuration);
        
        return bean.getObject();
    }

    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(
            @Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

**关键点**：
- ✅ `@MapperScan` 指定 Mapper 接口所在的包
- ✅ `sqlSessionFactoryRef` 关联到对应的 SqlSessionFactory
- ✅ 使用 `@Primary` 标记主数据源

---

### 步骤 4: 创建第二个数据源配置类

```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.multidatasource.mapper.secondary",
    sqlSessionFactoryRef = "secondarySqlSessionFactory"
)
public class SecondaryMyBatisConfig {

    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(
            @Qualifier("secondaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        
        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Slf4jImpl.class);
        bean.setConfiguration(configuration);
        
        return bean.getObject();
    }

    @Bean(name = "secondarySqlSessionTemplate")
    public SqlSessionTemplate secondarySqlSessionTemplate(
            @Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(
            @Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

**关键点**：
- ✅ Mapper 包路径与主数据源不同
- ✅ 不使用 `@Primary`，需要通过 `@Qualifier` 指定

---

### 步骤 5: 创建 Mapper 接口

#### 主数据源 Mapper

```java
package io.github.daihaowxg.multidatasource.mapper.primary;

@Mapper
public interface PrimaryUserMapper {

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    Optional<User> findById(@Param("id") Long id);

    @Insert("INSERT INTO users (name, email) VALUES (#{name}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
    int update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
```

#### 第二个数据源 Mapper

```java
package io.github.daihaowxg.multidatasource.mapper.secondary;

@Mapper
public interface SecondaryUserMapper {
    // 与 PrimaryUserMapper 相同的方法
}
```

**关键点**：
- ✅ 两个 Mapper 在不同的包下
- ✅ 会被对应配置类的 `@MapperScan` 扫描到
- ✅ 自动关联到对应的 SqlSessionFactory

---

### 步骤 6: 使用 Mapper

```java
@Service
@RequiredArgsConstructor
public class MyBatisMultiDataSourceService {

    private final PrimaryUserMapper primaryUserMapper;
    private final SecondaryUserMapper secondaryUserMapper;

    // 操作主数据源
    @Transactional(transactionManager = "primaryTransactionManager")
    public User saveUserToPrimary(User user) {
        primaryUserMapper.insert(user);
        return user;
    }

    // 操作第二个数据源
    @Transactional(transactionManager = "secondaryTransactionManager")
    public User saveUserToSecondary(User user) {
        secondaryUserMapper.insert(user);
        return user;
    }

    // 跨数据源操作（不在同一个事务中）
    public void syncUser(Long id) {
        Optional<User> user = primaryUserMapper.findById(id);
        if (user.isPresent()) {
            User u = user.get();
            u.setId(null);  // 重置 ID
            secondaryUserMapper.insert(u);
        }
    }
}
```

---

## 🔑 **关键配置说明**

### 1. **@MapperScan 注解**

```java
@MapperScan(
    basePackages = "com.example.mapper.primary",  // Mapper 接口所在包
    sqlSessionFactoryRef = "primarySqlSessionFactory"  // 关联的 SqlSessionFactory
)
```

**作用**：
- 扫描指定包下的 Mapper 接口
- 将 Mapper 关联到指定的 SqlSessionFactory
- 不同数据源的 Mapper 必须在不同的包下

### 2. **SqlSessionFactory 配置**

```java
@Bean(name = "primarySqlSessionFactory")
public SqlSessionFactory primarySqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    
    // 可选：设置 MyBatis 配置
    Configuration configuration = new Configuration();
    configuration.setMapUnderscoreToCamelCase(true);  // 下划线转驼峰
    configuration.setLogImpl(Slf4jImpl.class);  // 日志实现
    bean.setConfiguration(configuration);
    
    // 可选：设置 Mapper XML 位置
    // bean.setMapperLocations(new PathMatchingResourcePatternResolver()
    //     .getResources("classpath:mapper/primary/*.xml"));
    
    return bean.getObject();
}
```

### 3. **SqlSessionTemplate**

```java
@Bean(name = "primarySqlSessionTemplate")
public SqlSessionTemplate primarySqlSessionTemplate(
        SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
}
```

**作用**：
- 提供线程安全的 SqlSession
- 可以直接注入使用（不常用，通常使用 Mapper 接口）

---

## 📊 **配置对比表**

| 组件 | JdbcTemplate 多数据源 | MyBatis 多数据源 |
|------|---------------------|-----------------|
| DataSource | ✅ 需要 | ✅ 需要 |
| JdbcTemplate | ✅ 需要 | ❌ 不需要 |
| SqlSessionFactory | ❌ 不需要 | ✅ 需要 |
| SqlSessionTemplate | ❌ 不需要 | ✅ 需要（可选） |
| TransactionManager | ✅ 需要 | ✅ 需要 |
| @MapperScan | ❌ 不需要 | ✅ 需要 |
| Mapper 接口 | ❌ 不需要 | ✅ 需要 |

---

## ⚠️ **常见问题**

### Q1: Mapper 接口必须在不同的包下吗？

**A:** 是的！因为 `@MapperScan` 通过包路径区分不同数据源的 Mapper。

```
✅ 正确的包结构：
com.example.mapper.primary.UserMapper    → 主数据源
com.example.mapper.secondary.UserMapper  → 第二个数据源

❌ 错误的包结构：
com.example.mapper.UserMapper           → 无法区分数据源
com.example.mapper.PrimaryUserMapper    → 无法区分数据源
```

### Q2: 可以使用 XML 方式配置 SQL 吗？

**A:** 可以！在 SqlSessionFactory 中配置 Mapper XML 位置：

```java
@Bean(name = "primarySqlSessionFactory")
public SqlSessionFactory primarySqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    
    // 设置 Mapper XML 位置
    bean.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath:mapper/primary/*.xml"));
    
    return bean.getObject();
}
```

### Q3: @Transactional 必须指定 transactionManager 吗？

**A:** 在多数据源环境中，**强烈建议**明确指定！

```java
// ✅ 推荐：明确指定
@Transactional(transactionManager = "primaryTransactionManager")
public void saveUser(User user) { ... }

// ⚠️ 不推荐：会使用默认的（@Primary 的）
@Transactional
public void saveUser(User user) { ... }
```

### Q4: 如何实现跨数据源的分布式事务？

**A:** MyBatis 多数据源的分布式事务需要额外配置：

1. **使用 JTA**（如 Atomikos）
2. **使用 Seata** 分布式事务框架
3. **使用消息队列** 实现最终一致性

---

## 🎯 **最佳实践**

1. ✅ **使用 @Primary 标记主数据源**
2. ✅ **Mapper 接口放在不同的包下**
3. ✅ **明确指定 transactionManager**
4. ✅ **使用 @Qualifier 注入 Mapper**（虽然通常不需要，因为包路径已区分）
5. ⚠️ **谨慎处理跨数据源操作**
6. ⚠️ **避免在同一个事务中操作多个数据源**

---

## 📁 **项目结构**

```
multidatasource/
├── src/main/java/
│   └── io/github/daihaowxg/multidatasource/
│       ├── config/
│       │   ├── PrimaryMyBatisConfig.java      # 主数据源配置
│       │   └── SecondaryMyBatisConfig.java    # 第二个数据源配置
│       ├── mapper/
│       │   ├── primary/
│       │   │   └── PrimaryUserMapper.java     # 主数据源 Mapper
│       │   └── secondary/
│       │       └── SecondaryUserMapper.java   # 第二个数据源 Mapper
│       ├── entity/
│       │   └── User.java
│       └── service/
│           └── MyBatisMultiDataSourceService.java
└── src/main/resources/
    ├── application.yml
    ├── schema-primary.sql
    └── schema-secondary.sql
```

---

## 🚀 **运行测试**

```bash
# 运行所有测试
mvn test

# 运行 MyBatis 多数据源测试
mvn test -Dtest=MyBatisMultiDataSourceTest
```

---

## 📚 **参考资料**

- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [MyBatis Spring Boot Starter](https://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
- [Spring Boot 多数据源配置](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-access.configure-custom-datasource)
