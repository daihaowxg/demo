# 🎯 OpenRewrite 实验建议

## 实验 1: 代码格式化 ⭐ (难度: ★☆☆☆☆)

**目标**: 体验 OpenRewrite 的自动格式化能力

### 步骤

1. **查看原始代码**
   ```bash
   cat src/main/java/io/github/daihaowxg/openrewrite/legacy/UnformattedCode.java
   ```
   注意观察：
   - 不一致的空格和缩进
   - 混乱的换行
   - 不规范的括号位置

2. **预览格式化效果**
   ```bash
   mvn rewrite:dryRun -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat
   ```

3. **应用格式化**
   ```bash
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat
   ```

4. **对比差异**
   ```bash
   git diff src/main/java/io/github/daihaowxg/openrewrite/legacy/UnformattedCode.java
   ```

### 预期结果
- 统一的缩进（4 空格）
- 规范的括号位置
- 一致的空行使用
- 整洁的代码布局

---

## 实验 2: 静态分析清理 ⭐⭐ (难度: ★★☆☆☆)

**目标**: 学习常见的代码优化模式

### 步骤

1. **运行静态分析**
   ```bash
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.cleanup.CommonStaticAnalysis
   ```

2. **查看优化结果**
   重点关注 `UnformattedCode.java` 中的这些方法：
   - `isAdult()` - 布尔表达式简化
   - `hasHobby()` - 冗余比较移除
   - `getHobbyCount()` - 条件简化

### 预期优化

**优化前**:
```java
public boolean isAdult() {
    if (age >= 18) {
        return true;
    } else {
        return false;
    }
}
```

**优化后**:
```java
public boolean isAdult() {
    return age >= 18;
}
```

---

## 实验 3: 废弃 API 替换 ⭐⭐⭐ (难度: ★★★☆☆)

**目标**: 了解如何自动更新废弃的 API

### 步骤

1. **查看废弃 API 使用**
   ```bash
   cat src/main/java/io/github/daihaowxg/openrewrite/legacy/DeprecatedApiUsage.java
   ```

2. **运行 API 更新**
   ```bash
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.migrate.JavaVersion17
   ```

### 预期变更

**变更 1: URL 编码**
```java
// 之前
return URLEncoder.encode(url, "UTF-8");

// 之后
return URLEncoder.encode(url, StandardCharsets.UTF_8);
```

**变更 2: 包装类构造器**
```java
// 之前
return new Integer(value);

// 之后
return Integer.valueOf(value);
```

---

## 实验 4: JUnit 4 → JUnit 5 迁移 ⭐⭐⭐⭐ (难度: ★★★★☆)

**目标**: 体验框架升级的自动化

### 步骤

1. **查看 JUnit 4 测试**
   ```bash
   cat src/test/java/io/github/daihaowxg/openrewrite/legacy/JUnit4Test.java
   ```

2. **运行迁移**
   ```bash
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.testing.junit5.JUnit4to5Migration
   ```

3. **查看迁移结果**
   ```bash
   cat src/test/java/io/github/daihaowxg/openrewrite/legacy/JUnit4Test.java
   ```

### 预期变更

| JUnit 4 | JUnit 5 |
|---------|---------|
| `@Before` | `@BeforeEach` |
| `@After` | `@AfterEach` |
| `@BeforeClass` | `@BeforeAll` |
| `@AfterClass` | `@AfterAll` |
| `@Ignore` | `@Disabled` |
| `Assert.assertEquals()` | `Assertions.assertEquals()` |
| `@Test(expected = ...)` | `assertThrows()` |
| `@Test(timeout = ...)` | `@Timeout` |

---

## 实验 5: 自定义 Recipe 组合 ⭐⭐⭐⭐⭐ (难度: ★★★★★)

**目标**: 创建符合团队规范的重构规则

### 步骤

1. **查看配置文件**
   ```bash
   cat rewrite.yml
   ```

2. **运行自定义 Recipe**
   ```bash
   mvn rewrite:run -Drewrite.activeRecipes=io.github.daihaowxg.openrewrite.CodeCleanup
   ```

3. **创建自己的 Recipe**
   编辑 `rewrite.yml`，添加：
   ```yaml
   ---
   type: specs.openrewrite.org/v1beta/recipe
   name: io.github.daihaowxg.openrewrite.MyCustomRecipe
   displayName: 我的自定义规则
   recipeList:
     - org.openrewrite.java.format.AutoFormat
     - org.openrewrite.java.cleanup.CommonStaticAnalysis
     # 添加更多 recipes...
   ```

4. **运行自定义 Recipe**
   ```bash
   mvn rewrite:run -Drewrite.activeRecipes=io.github.daihaowxg.openrewrite.MyCustomRecipe
   ```

---

## 实验 6: 探索更多 Recipes ⭐⭐⭐ (难度: ★★★☆☆)

**目标**: 发现和使用其他有用的 recipes

### 步骤

1. **列出所有可用 Recipes**
   ```bash
   mvn rewrite:discover | less
   ```

2. **搜索特定 Recipes**
   ```bash
   # 搜索 Spring 相关
   mvn rewrite:discover | grep -i spring
   
   # 搜索 Java 迁移相关
   mvn rewrite:discover | grep -i migrate
   
   # 搜索测试相关
   mvn rewrite:discover | grep -i test
   ```

3. **尝试有趣的 Recipes**
   ```bash
   # 使用 var 关键字
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.cleanup.UseVarKeyword
   
   # 使用 Text Blocks (Java 15+)
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.migrate.lang.UseTextBlocks
   
   # instanceof 模式匹配 (Java 16+)
   mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.migrate.lang.UseInstanceofPatternMatching
   ```

---

## 💡 实验技巧

### 1. 始终先 Dry Run
```bash
mvn rewrite:dryRun -Drewrite.activeRecipes=<recipe-name>
```

### 2. 查看详细变更
```bash
cat target/rewrite/rewrite.patch
```

### 3. 重置代码
```bash
git checkout .
```

### 4. 组合多个 Recipes
```bash
mvn rewrite:run -Drewrite.activeRecipes=recipe1,recipe2,recipe3
```

### 5. 排除特定文件
在 `pom.xml` 中配置：
```xml
<configuration>
    <exclusions>
        <exclusion>**/SpecificFile.java</exclusion>
    </exclusions>
</configuration>
```

---

## 📊 学习进度追踪

- [ ] 实验 1: 代码格式化
- [ ] 实验 2: 静态分析清理
- [ ] 实验 3: 废弃 API 替换
- [ ] 实验 4: JUnit 4 → 5 迁移
- [ ] 实验 5: 自定义 Recipe 组合
- [ ] 实验 6: 探索更多 Recipes

完成所有实验后，你将掌握 OpenRewrite 的核心用法！🎉
