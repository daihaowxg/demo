# Spring Cache 详细示例说明

本文档详细介绍了本项目中的各个代码示例，帮助你理解 Spring Cache 的不同特性。

## 1. 基础缓存操作 (UserController)

对应服务类：`UserService.java`

### 1.1 查询缓存 (@Cacheable)
**场景**：查询用户信息，如果缓存中有则直接返回，否则查询数据库并存入缓存。

*   **注解**：`@Cacheable(key = "#id")`
*   **测试命令**：
    ```bash
    # 第一次查询（执行模拟数据库操作，耗时约1秒）
    curl http://localhost:8080/users/1
    
    # 第二次查询（直接返回缓存，毫秒级响应）
    curl http://localhost:8080/users/1
    ```

### 1.2 更新缓存 (@CachePut)
**场景**：更新用户信息，同时更新缓存中的数据，保证缓存一致性。

*   **注解**：`@CachePut(key = "#user.id")`
*   **测试命令**：
    ```bash
    # 更新用户 ID 为 1 的数据
    curl -X PUT http://localhost:8080/users/1 \
      -H "Content-Type: application/json" \
      -d '{"id":1,"name":"New Name","email":"new@example.com","age":30}'
    
    # 再次查询（数据已更新且来自缓存）
    curl http://localhost:8080/users/1
    ```

### 1.3 清除缓存 (@CacheEvict)
**场景**：删除用户时，同时删除对应的缓存。

*   **注解**：`@CacheEvict(key = "#id")`
*   **测试命令**：
    ```bash
    # 删除用户
    curl -X DELETE http://localhost:8080/users/1
    
    # 再次查询（重新查询数据库）
    curl http://localhost:8080/users/1
    ```

### 1.4 组合操作 (@Caching)
**场景**：刷新用户数据，先清除旧缓存，再重新加载并放入缓存。

*   **注解**：`@Caching(evict = {...}, put = {...})`
*   **测试命令**：
    ```bash
    curl -X POST http://localhost:8080/users/1/refresh
    ```

---

## 2. 条件与列表缓存 (ProductController)

对应服务类：`ProductService.java`

### 2.1 条件缓存 (condition)
**场景**：只有 ID 大于 0 的商品才缓存。

*   **注解**：`@Cacheable(..., condition = "#id > 0")`
*   **测试命令**：
    ```bash
    # ID > 0，会被缓存（执行模拟数据库操作，耗时约1秒）
    curl http://localhost:8080/products/1
    
    # 再次查询 ID 1（缓存命中，直接返回缓存，毫秒级响应）
    curl http://localhost:8080/products/1
    ```

### 2.2 结果判断 (unless)
**场景**：查询结果为 null 时不缓存（防止缓存无效数据）。

*   **注解**：`@Cacheable(..., unless = "#result == null")`
*   **测试命令**：
    ```bash
    curl http://localhost:8080/products/999/null-check
    ```

### 2.3 自定义动态 Key
**场景**：根据价格区间缓存商品列表。

*   **注解**：`@Cacheable(key = "'price:' + #minPrice + '-' + #maxPrice")`
*   **生成的 Key**：例如 `price:100-500`
*   **测试命令**：
    ```bash
    curl "http://localhost:8080/products/price-range?minPrice=100&maxPrice=500"
    ```

---

## 3. 进阶特性 (AdvancedCacheController)

对应服务类：`AdvancedCacheService.java`

### 3.1 缓存同步 (Sync)
**场景**：高并发下防止缓存击穿（Dog-pile effect）。当缓存失效时，只允许一个线程去加载数据，其他线程等待。

*   **注解**：`@Cacheable(..., sync = true)`
*   **验证方法**：可以使用工具（如 Apache Bench 或 JMeter）模拟并发请求，观察日志中"执行耗时操作"只出现一次。
*   **测试命令**：
    ```bash
    curl http://localhost:8080/advanced/expensive/key1
    ```

### 3.2 缓存空值 (防止穿透)
**场景**：查询不存在的数据时，也缓存一个空对象（或特定标识），防止频繁请求数据库。

*   **测试命令**：
    ```bash
    # 查询 ID -1（不存在），服务层会返回 null 并被缓存
    curl http://localhost:8080/advanced/nullable/-1
    ```

### 3.3 自定义 KeyGenerator
**场景**：当 key 生成逻辑很复杂，或者需要在多个方法间复用 key 生成策略时使用。

*   **实现**：`CacheConfig.java` 中的 `customKeyGenerator`
*   **使用**：`@Cacheable(keyGenerator = "customKeyGenerator")`
*   **测试命令**：
    ```bash
    curl "http://localhost:8080/advanced/custom-key?param1=abc&param2=123"
    ```

---

## 4. 缓存配置说明

### Caffeine 配置 (CacheConfig.java)
我们在 `CacheConfig.java` 中配置了两个具体的缓存实例：
1.  **users**: 30分钟过期，最大500个。
2.  **products**: 5分钟过期，最大2000个。
3.  **默认**: 10分钟过期，最大1000个。

### Redis 配置 (RedisCacheConfig.java)
如果启用了 Redis（在 `application.yml` 中配置 `spring.cache.type=redis`），则会使用 Redis 作为缓存后端。
配置了 JSON 序列化器，使得 Redis 中的数据可读性更好。

## 5. 常用 SpEL 表达式速查

| 表达式 | 描述 | 示例 |
| :--- | :--- | :--- |
| `#root.methodName` | 当前方法名 | `getUser` |
| `#root.method.name` | 当前方法名 | `getUser` |
| `#root.target` | 当前被调用的对象 | `UserService` 实例 |
| `#root.targetClass` | 当前被调用的类 | `UserService.class` |
| `#root.args[0]` | 第一个参数 | `#id` |
| `#root.caches[0].name` | 当前缓存名称 | `users` |
| `#result` | 方法执行结果 | `User` 对象 (仅在 `unless` 或 `@CachePut` 中可用) |
| `#paramName` | 方法参数名 | `#id`, `#user` |

