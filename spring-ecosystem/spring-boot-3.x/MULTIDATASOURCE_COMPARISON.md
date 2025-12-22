# 多数据源模块说明

本项目包含两个多数据源示例模块，分别演示 JdbcTemplate 和 MyBatis 的多数据源配置。

## 📦 **模块概览**

| 模块 | 技术栈 | 说明 |
|------|--------|------|
| `multidatasource` | JdbcTemplate | 演示如何使用 JdbcTemplate 配置多数据源 |
| `multidatasource-mybatis` | MyBatis | 演示如何使用 MyBatis 配置多数据源 |

---

## 🎯 **multidatasource（JdbcTemplate 方式）**

### 核心配置

每个数据源需要配置 **3 个 Bean**：
- `DataSource` - 数据源
- `JdbcTemplate` - JDBC 模板
- `TransactionManager` - 事务管理器

### 配置类

```java
@Configuration
public class MultiDataSourceConfig {
    
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() { ... }
    
    @Primary
    @Bean(name = "primaryJdbcTemplate")
    public JdbcTemplate primaryJdbcTemplate(...) { ... }
    
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(...) { ... }
}
```

### 使用方式

```java
@Repository
public class UserRepository {
    
    private final JdbcTemplate primaryJdbcTemplate;
    private final JdbcTemplate secondaryJdbcTemplate;
    
    public UserRepository(
            @Qualifier("primaryJdbcTemplate") JdbcTemplate primary,
            @Qualifier("secondaryJdbcTemplate") JdbcTemplate secondary) {
        this.primaryJdbcTemplate = primary;
        this.secondaryJdbcTemplate = secondary;
    }
    
    @Transactional(transactionManager = "primaryTransactionManager")
    public void saveUser(User user) {
        primaryJdbcTemplate.update(...);
    }
}
```

---

## 🎯 **multidatasource-mybatis（MyBatis 方式）**

### 核心配置

每个数据源需要配置 **4 个 Bean**：
- `DataSource` - 数据源
- `SqlSessionFactory` - MyBatis 会话工厂
- `SqlSessionTemplate` - MyBatis 会话模板
- `TransactionManager` - 事务管理器

### 配置类

```java
@Configuration
@MapperScan(
    basePackages = "io.github.daihaowxg.mybatis.mapper.primary",
    sqlSessionFactoryRef = "primarySqlSessionFactory"
)
public class PrimaryMyBatisConfig {
    
    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() { ... }
    
    @Primary
    @Bean(name = "primarySqlSessionFactory")
    public SqlSessionFactory primarySqlSessionFactory(...) { ... }
    
    @Primary
    @Bean(name = "primarySqlSessionTemplate")
    public SqlSessionTemplate primarySqlSessionTemplate(...) { ... }
    
    @Primary
    @Bean(name = "primaryTransactionManager")
    public PlatformTransactionManager primaryTransactionManager(...) { ... }
}
```

### 使用方式

```java
// Mapper 接口
@Mapper
public interface PrimaryUserMapper {
    @Select("SELECT * FROM users")
    List<User> findAll();
}

// Service 层
@Service
public class UserService {
    
    private final PrimaryUserMapper primaryUserMapper;
    private final SecondaryUserMapper secondaryUserMapper;
    
    @Transactional(transactionManager = "primaryTransactionManager")
    public void saveUser(User user) {
        primaryUserMapper.insert(user);
    }
}
```

---

## 📊 **配置对比**

| 特性 | JdbcTemplate | MyBatis |
|------|-------------|---------|
| **配置复杂度** | 简单 | 中等 |
| **Bean 数量** | 每个数据源 3 个 | 每个数据源 4 个 |
| **SQL 编写** | 字符串拼接 | 注解或 XML |
| **类型安全** | 较弱 | 较强 |
| **动态 SQL** | 手动拼接 | 支持 |
| **结果映射** | 手动映射 | 自动映射 |
| **适用场景** | 简单 CRUD | 复杂查询 |

---

## 🔑 **关键区别**

### 1. **Mapper 扫描**

**MyBatis 需要 @MapperScan**：
```java
@MapperScan(
    basePackages = "com.example.mapper.primary",
    sqlSessionFactoryRef = "primarySqlSessionFactory"
)
```

**JdbcTemplate 不需要**：直接注入 `JdbcTemplate` 使用

### 2. **包结构要求**

**MyBatis**：
- ✅ 必须将不同数据源的 Mapper 放在不同的包下
- 示例：
  ```
  mapper/primary/UserMapper.java
  mapper/secondary/UserMapper.java
  ```

**JdbcTemplate**：
- ✅ 无包结构要求
- 通过 `@Qualifier` 区分不同的 `JdbcTemplate`

### 3. **SQL 编写方式**

**MyBatis**：
```java
@Select("SELECT * FROM users WHERE id = #{id}")
User findById(@Param("id") Long id);
```

**JdbcTemplate**：
```java
public User findById(Long id) {
    return jdbcTemplate.queryForObject(
        "SELECT * FROM users WHERE id = ?",
        new BeanPropertyRowMapper<>(User.class),
        id
    );
}
```

---

## 🚀 **运行测试**

### JdbcTemplate 模块
```bash
cd multidatasource
mvn test
```

### MyBatis 模块
```bash
cd multidatasource-mybatis
mvn test
```

---

## 📚 **参考文档**

- **JdbcTemplate 方式**：查看 `multidatasource/README.md`
- **MyBatis 方式**：查看 `multidatasource-mybatis/README.md`

---

## 💡 **选择建议**

### 使用 JdbcTemplate 如果：
- ✅ 项目简单，主要是 CRUD 操作
- ✅ 不需要复杂的动态 SQL
- ✅ 团队熟悉原生 SQL

### 使用 MyBatis 如果：
- ✅ 需要复杂的动态 SQL
- ✅ 需要更好的类型安全
- ✅ 需要结果集自动映射
- ✅ 团队熟悉 MyBatis

---

## ⚠️ **注意事项**

1. **两种方式不要混用**：在同一个模块中只使用一种方式
2. **@Transactional 必须指定 transactionManager**
3. **跨数据源操作不保证事务一致性**
4. **配置文件使用 `jdbc-url` 而不是 `url`**
