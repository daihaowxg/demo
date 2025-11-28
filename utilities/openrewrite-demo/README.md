# OpenRewrite Demo

## 📖 项目简介

这是一个 **OpenRewrite** 自动化代码重构工具的实验项目。OpenRewrite 是一个强大的代码转换引擎，可以自动化执行大规模的代码重构、框架升级、依赖迁移等任务。

## 🎯 学习目标

通过本项目，你将学习到：

1. **OpenRewrite 基础概念**
   - Recipes（配方）的使用
   - LST（Lossless Semantic Tree）无损语义树
   - Visitors（访问器）模式

2. **常见使用场景**
   - Java 版本升级（Java 8 → 11 → 17）
   - Spring Boot 版本迁移（2.x → 3.x）
   - JUnit 4 → JUnit 5 迁移
   - 代码规范统一
   - 静态分析和代码清理
   - 依赖版本升级

3. **自定义 Recipe 开发**
   - 编写自定义重构规则
   - 满足特定业务需求

## ⚠️ 重要：Maven 仓库配置

**在开始之前，请先阅读 [`MAVEN_SETUP.md`](./MAVEN_SETUP.md)**

如果你使用了阿里云 Maven 镜像，可能会遇到 OpenRewrite 依赖下载失败的问题。请按照 `MAVEN_SETUP.md` 中的说明配置 Maven 仓库。

**快速解决方案**：使用临时 settings.xml

```bash
# 创建临时配置（不使用镜像）
cat > /tmp/maven-settings.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
</settings>
EOF

# 使用临时配置运行命令
mvn -s /tmp/maven-settings.xml rewrite:discover
```

## 🚀 快速开始

### 1. 查看可用的 Recipes

```bash
mvn rewrite:discover
```

这会列出所有可用的 recipes 及其描述。

### 2. 运行 Dry Run（预览模式）

在实际修改代码之前，先预览会发生什么变化：

```bash
# 运行所有激活的 recipes（dry-run 模式）
mvn rewrite:dryRun

# 运行特定的 recipe
mvn rewrite:dryRun -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat
```

### 3. 应用重构

确认预览结果后，执行实际的代码修改：

```bash
# 应用所有激活的 recipes
mvn rewrite:run

# 应用特定的 recipe
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.cleanup.CommonStaticAnalysis
```

### 4. 查看变更报告

OpenRewrite 会在 `target/rewrite/` 目录下生成详细的变更报告。

## 📋 常用 Recipes 示例

### 代码格式化和清理

```bash
# 自动格式化代码
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat

# 通用静态分析清理
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.cleanup.CommonStaticAnalysis

# 移除未使用的导入
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.format.RemoveUnusedImports
```

### Java 版本迁移

```bash
# 迁移到 Java 11
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.migrate.JavaVersion11

# 迁移到 Java 17
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.migrate.JavaVersion17

# 迁移到 Java 21
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.migrate.JavaVersion21
```

### Spring Boot 升级

```bash
# 升级到 Spring Boot 3.0
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_0

# 升级到 Spring Boot 3.2
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.spring.boot3.UpgradeSpringBoot_3_2
```

### 测试框架迁移

```bash
# JUnit 4 → JUnit 5 (Jupiter)
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.testing.junit5.JUnit4to5Migration

# AssertJ 最佳实践
mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.testing.assertj.Assertj
```

## 📁 项目结构

```
openrewrite-demo/
├── pom.xml                                    # Maven 配置
├── README.md                                  # 本文件
├── rewrite.yml                                # OpenRewrite 配置文件
└── src/
    ├── main/
    │   └── java/io/github/daihaowxg/openrewrite/
    │       ├── legacy/                        # 待重构的"遗留代码"
    │       │   ├── OldStyleService.java       # 使用旧 API 的代码
    │       │   ├── UnformattedCode.java       # 格式混乱的代码
    │       │   └── DeprecatedApiUsage.java    # 使用废弃 API 的代码
    │       │
    │       └── examples/                      # 各种场景示例
    │           ├── SpringBootExample.java     # Spring Boot 相关
    │           └── CollectionExample.java     # 集合操作优化
    │
    └── test/
        └── java/io/github/daihaowxg/openrewrite/
            └── legacy/
                └── JUnit4Test.java            # JUnit 4 测试（用于迁移演示）
```

## 🔧 配置文件说明

### rewrite.yml

这是 OpenRewrite 的主配置文件，你可以在这里：

- 定义要激活的 recipes
- 配置 recipe 参数
- 创建自定义 recipe 组合
- 排除特定文件或目录

示例配置：

```yaml
---
type: specs.openrewrite.org/v1beta/recipe
name: io.github.daihaowxg.openrewrite.MyCustomRecipe
displayName: 我的自定义重构规则
description: 组合多个 recipes 的自定义规则
recipeList:
  - org.openrewrite.java.format.AutoFormat
  - org.openrewrite.java.cleanup.CommonStaticAnalysis
  - org.openrewrite.java.format.RemoveUnusedImports
```

## 💡 实验建议

### 实验 1: 代码格式化
1. 查看 `src/main/java/io/github/daihaowxg/openrewrite/legacy/UnformattedCode.java`
2. 运行 `mvn rewrite:dryRun -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat`
3. 查看预览的变更
4. 运行 `mvn rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat`
5. 对比前后差异

### 实验 2: JUnit 4 → JUnit 5 迁移
1. 查看 `src/test/java/io/github/daihaowxg/openrewrite/legacy/JUnit4Test.java`
2. 运行 JUnit 4 → 5 迁移 recipe
3. 观察注解、断言方法的自动转换

### 实验 3: 静态分析清理
1. 运行 `CommonStaticAnalysis` recipe
2. 查看自动优化的代码（如简化布尔表达式、优化字符串操作等）

### 实验 4: Spring Boot 升级（高级）
1. 创建一个简单的 Spring Boot 2.x 应用
2. 运行 Spring Boot 3.x 升级 recipe
3. 观察依赖、配置、API 的自动迁移

## 📚 学习资源

- **官方文档**: https://docs.openrewrite.org/
- **Recipe 目录**: https://docs.openrewrite.org/recipes
- **GitHub**: https://github.com/openrewrite/rewrite
- **示例项目**: https://github.com/openrewrite/rewrite-examples
- **在线工具**: https://app.moderne.io/ (可视化 Recipe 浏览)

## ⚠️ 注意事项

1. **备份代码**: 在运行 `rewrite:run` 之前，确保代码已提交到版本控制系统
2. **先 Dry Run**: 始终先运行 `dryRun` 预览变更
3. **逐步验证**: 应用重构后，运行测试确保功能正常
4. **理解变更**: 查看变更报告，理解每个修改的原因
5. **版本兼容**: 注意 recipe 版本与项目 Java/Spring Boot 版本的兼容性

## 🎓 进阶学习

完成基础实验后，可以尝试：

1. **编写自定义 Recipe**: 创建符合团队规范的重构规则
2. **CI/CD 集成**: 将 OpenRewrite 集成到持续集成流程
3. **大规模迁移**: 在多模块项目中应用 OpenRewrite
4. **Recipe 组合**: 创建复杂的 recipe 组合满足特定需求

## 🤝 贡献

如果你发现有用的 recipes 或实验场景，欢迎补充到本项目！

---

**Happy Refactoring! 🚀**
