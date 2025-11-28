# OpenRewrite 快速入门指南

## 🎯 5 分钟快速体验

### 步骤 1: 进入项目目录

```bash
cd /Users/wxg/my-projects/java-labs/utilities/openrewrite-demo
```

### 步骤 2: 查看可用的 Recipes

```bash
mvn rewrite:discover
```

这会列出所有可用的 recipes。输出会很长，你可以搜索感兴趣的关键词。

### 步骤 3: 第一次重构 - 代码格式化

先看看当前代码的样子：

```bash
cat src/main/java/io/github/daihaowxg/openrewrite/legacy/UnformattedCode.java
```

运行 dry-run 预览变更：

```bash
mvn rewrite:dryRun -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat
```

如果满意，应用变更：

```bash
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat
```

再次查看代码，对比差异：

```bash
cat src/main/java/io/github/daihaowxg/openrewrite/legacy/UnformattedCode.java
```

### 步骤 4: 静态分析清理

运行通用静态分析清理：

```bash
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.staticanalysis.CommonStaticAnalysis
```

这会自动优化：
- 简化布尔表达式
- 优化字符串操作
- 移除冗余代码
- 等等...

### 步骤 5: 使用自定义 Recipe 组合

运行我们在 `rewrite.yml` 中定义的自定义 recipe：

```bash
mvn rewrite:run -Drewrite.activeRecipes=io.github.daihaowxg.openrewrite.CodeCleanup
```

### 步骤 6: JUnit 4 → JUnit 5 迁移

查看测试文件：

```bash
cat src/test/java/io/github/daihaowxg/openrewrite/legacy/JUnit4Test.java
```

运行迁移：

```bash
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.testing.junit5.JUnit4to5Migration
```

查看迁移后的结果：

```bash
cat src/test/java/io/github/daihaowxg/openrewrite/legacy/JUnit4Test.java
```

注意观察：
- `@Before` → `@BeforeEach`
- `@After` → `@AfterEach`
- `@BeforeClass` → `@BeforeAll`
- `@AfterClass` → `@AfterAll`
- `@Ignore` → `@Disabled`
- `import org.junit.*` → `import org.junit.jupiter.api.*`
- `Assert.*` → `Assertions.*`

### 步骤 7: 查看变更报告

```bash
ls -la target/rewrite/
cat target/rewrite/rewrite.patch
```

## 🔄 重置代码

如果你想重置代码到初始状态，使用 Git：

如果你想重置代码到初始状态，有以下几种方法：

### 方法 1: 使用 Git（推荐）

最简单的方法是直接丢弃工作区的修改：

```bash
# 丢弃所有修改
git restore .

# 或者（旧版 Git）
git checkout .
```

**💡 提示**：建议在运行 `rewrite:run` 之前，确保你的 Git 工作区是干净的（已提交所有更改），这样你可以随时轻松回滚。

### 方法 2: 使用 Patch 文件回退

如果你运行了 `rewrite:dryRun`，它会在 `target/rewrite/rewrite.patch` 生成一个补丁文件。你可以用它来反向应用修改：

```bash
# 反向应用补丁（撤销修改）
git apply -R target/rewrite/rewrite.patch
```

这在你不想使用 Git 版本控制，或者只想撤销 OpenRewrite 产生的特定修改时很有用。

## 📊 推荐的实验顺序

1. **代码格式化** - 最直观，立即看到效果
2. **静态分析清理** - 学习常见的代码优化模式
3. **废弃 API 替换** - 了解如何自动更新 API
4. **JUnit 迁移** - 体验框架升级的自动化
5. **自定义 Recipe** - 组合多个 recipes 满足特定需求

## 💡 常用命令速查

```bash
# 查看所有可用 recipes
mvn rewrite:discover

# Dry run（预览，不修改文件）
mvn rewrite:dryRun -Drewrite.activeRecipes=<recipe-name>

# 应用重构
mvn rewrite:run -Drewrite.activeRecipes=<recipe-name>

# 运行多个 recipes
mvn rewrite:run -Drewrite.activeRecipes=recipe1,recipe2,recipe3

# 查看帮助
mvn rewrite:help
```

## 🎓 下一步

完成快速入门后，查看 `README.md` 了解更多高级用法！
