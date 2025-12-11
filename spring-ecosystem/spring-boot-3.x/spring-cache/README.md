# Spring Cache 学习示例

这是一个完整的 Spring Cache 学习示例项目，涵盖从基础到进阶的各种缓存使用场景。

## 📚 什么是 Spring Cache？

Spring Cache 是 Spring 框架提供的**缓存抽象层**，它：
- 提供统一的缓存注解（`@Cacheable`、`@CachePut`、`@CacheEvict` 等）
- 支持多种缓存实现（Caffeine、Redis、EhCache 等）
- 通过 AOP 实现缓存逻辑，对业务代码无侵入
- 使用简单，只需添加注解即可实现缓存功能

## 🎯 核心注解

### @Cacheable
- **作用**：缓存方法的返回值
- **行为**：查询缓存，如果有则直接返回；**如果无缓存**，则执行方法并将结果存入缓存
- **适用场景**：查询操作

### @CachePut
- **作用**：更新缓存
- **行为**：无论缓存是否存在，都执行方法并更新缓存
- **适用场景**：新增、更新操作

### @CacheEvict
- **作用**：清除缓存
- **行为**：执行方法后删除指定的缓存
- **适用场景**：删除操作

### @Caching
- **作用**：组合多个缓存操作
- **适用场景**：需要同时执行多个缓存操作

### @CacheConfig
- **作用**：类级别的缓存配置
- **适用场景**：为整个类设置默认的缓存名称等

## 🚀 快速开始

### 1. 启动应用

```bash
cd /Users/wxg/my-projects/java-labs/spring-ecosystem/spring-boot-3.x/spring-cache
mvn spring-boot:run
```

### 2. 测试基础缓存功能

#### 步骤1：查询一个不存在的用户（验证缓存未命中 + 数据库查询）
```bash
curl http://localhost:8080/users/999
```
此时因为数据库和缓存都没有，返回空且控制台打印：`从数据库查询用户，ID: 999`

#### 步骤2：创建用户（验证 @CachePut 写入缓存）
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"张三","email":"zhangsan@example.com","age":25}'
```
> **注意**：由于使用了 `@CachePut`，创建成功后，该用户数据已经**同步写入了缓存**。

#### 步骤3：查询该用户（验证直接命中缓存）
```bash
curl http://localhost:8080/users/1
```
观察控制台日志，**不会**看到 `从数据库查询用户` 的日志，说明直接从缓存获取了刚才创建时写入的数据。

#### 步骤4：更新用户
```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"张三（已更新）","email":"zhangsan@example.com","age":26}'
```

#### 步骤5：再次查询（应该返回更新后的数据）
```bash
curl http://localhost:8080/users/1
```
验证数据已更新。
#### 删除用户
```bash
curl -X DELETE http://localhost:8080/users/1
```

### 3. 测试列表缓存

```bash
# 查询电子产品（第一次 - 慢）
curl http://localhost:8080/products/category/电子产品

# 再次查询（第二次 - 快，从缓存获取）
curl http://localhost:8080/products/category/电子产品
```

### 4. 测试条件缓存

```bash
# 查询价格区间的商品
curl "http://localhost:8080/products/price-range?minPrice=100&maxPrice=2000"

# 组合条件查询
# 中文可能会有问题
# curl "http://localhost:8080/products/combo?category=电子产品&maxPrice=3000"
curl "http://localhost:8080/products/combo?category=%E7%94%B5%E5%AD%90%E4%BA%A7%E5%93%81&maxPrice=3000"
```

### 5. 测试进阶特性

```bash
# 测试缓存同步（防止缓存击穿）
curl http://localhost:8080/advanced/expensive/test-key

# 测试自定义 KeyGenerator
curl "http://localhost:8080/advanced/custom-key?param1=hello&param2=123"

# 测试缓存空值（防止缓存穿透）
curl http://localhost:8080/advanced/nullable/-1
```

## 📁 项目结构

```
spring-cache/
├── src/main/java/io/github/daihaowxg/demo/lab16springcache/
│   ├── config/
│   │   ├── CacheConfig.java           # Caffeine 缓存配置
│   │   └── RedisCacheConfig.java      # Redis 缓存配置（可选）
│   ├── controller/
│   │   ├── UserController.java        # 用户 API
│   │   ├── ProductController.java     # 商品 API
│   │   └── AdvancedCacheController.java  # 进阶缓存 API
│   ├── entity/
│   │   ├── User.java                  # 用户实体
│   │   └── Product.java               # 商品实体
│   ├── service/
│   │   ├── UserService.java           # 用户服务（基础注解）
│   │   ├── ProductService.java        # 商品服务（条件缓存）
│   │   └── AdvancedCacheService.java  # 进阶服务（高级特性）
│   └── Lab16SpringCacheApplication.java  # 启动类
├── src/main/resources/
│   └── application.yml                # 配置文件
├── docs/
│   ├── EXAMPLES.md                    # 详细示例说明
│   └── BEST_PRACTICES.md              # 最佳实践
└── README.md                          # 本文件
```

## 🔧 缓存配置

### Caffeine（默认）
- 高性能本地缓存
- 适用于单机应用
- 配置在 `CacheConfig.java`

### Redis（可选）
- 分布式缓存
- 适用于多实例部署
- 需要启动 Redis 服务
- 配置在 `RedisCacheConfig.java`

切换到 Redis：
1. 启动 Redis 服务
2. 修改 `application.yml` 中的 `spring.cache.type` 为 `redis`

## 📖 学习路径

1. **基础入门**：查看 `UserService.java`，理解 `@Cacheable`、`@CachePut`、`@CacheEvict` 的基本用法
2. **条件缓存**：查看 `ProductService.java`，学习 `condition`、`unless` 和自定义 Key
3. **进阶特性**：查看 `AdvancedCacheService.java`，了解缓存同步、自定义 KeyGenerator
4. **实战应用**：查看 `docs/BEST_PRACTICES.md`，学习缓存穿透/雪崩/击穿的解决方案

## 📚 更多文档

- [详细示例说明](docs/EXAMPLES.md) - 每个注解的详细用法和测试方法
- [最佳实践](docs/BEST_PRACTICES.md) - 缓存使用的最佳实践和常见问题

## 💡 关键概念

### 缓存 Key
- 默认使用方法参数作为 key
- 支持 SpEL 表达式自定义 key
- 可以使用自定义 KeyGenerator

### 缓存过期
- Caffeine：支持基于时间和大小的淘汰策略
- Redis：支持 TTL（Time To Live）

### 缓存同步
- `sync = true`：防止缓存击穿
- 高并发场景下只有一个线程执行方法，其他线程等待

## ⚠️ 注意事项

1. **方法必须是 public**：缓存注解基于 AOP，只对 public 方法有效
2. **避免自调用**：同一个类内部方法调用不会触发缓存（AOP 限制）
3. **序列化**：使用 Redis 时，实体类必须实现 `Serializable` 接口
4. **缓存一致性**：更新数据时要同步更新或清除缓存

## 🎓 学习建议

1. 先运行示例，观察日志输出，理解缓存的命中和未命中
2. 修改代码，尝试不同的配置参数
3. 使用 JMeter 或 ab 工具进行并发测试，观察缓存同步的效果
4. 阅读源码，深入理解 Spring Cache 的实现原理

Happy Learning! 🚀
