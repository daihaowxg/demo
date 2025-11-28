# ⚠️ 重要提示：Maven 仓库配置

## 问题说明

OpenRewrite 的依赖包在阿里云 Maven 镜像中可能不完整或不是最新版本。如果你配置了阿里云镜像（通常在 `~/.m2/settings.xml` 中），可能会遇到依赖下载失败的问题。

## 错误信息

```
Could not find artifact org.openrewrite.recipe:rewrite-java:jar:8.37.1 
in aliyunmaven (https://maven.aliyun.com/repository/public)
```

## 解决方案

### 方案 1: 临时禁用镜像（推荐用于学习）

在运行 OpenRewrite 命令时，使用 `-s` 参数指定一个临时的 settings.xml：

1. **创建临时 settings.xml**

```bash
cat > /tmp/maven-settings.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <!-- 使用 Maven Central，不使用镜像 -->
</settings>
EOF
```

2. **使用临时配置运行命令**

```bash
# 查看可用 recipes
mvn -s /tmp/maven-settings.xml rewrite:discover

# Dry run
mvn -s /tmp/maven-settings.xml rewrite:dryRun -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat

# 应用重构
mvn -s /tmp/maven-settings.xml rewrite:run -Drewrite.activeRecipes=org.openrewrite.java.format.AutoFormat
```

### 方案 2: 修改全局 settings.xml（永久方案）

编辑 `~/.m2/settings.xml`，在 `<mirrors>` 部分添加排除规则：

```xml
<mirrors>
    <mirror>
        <id>aliyunmaven</id>
        <mirrorOf>*,!maven-central</mirrorOf>  <!-- 注意这里添加了 !maven-central -->
        <name>阿里云公共仓库</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
</mirrors>
```

这样配置后，`maven-central` 仓库会直接访问 Maven Central，不走阿里云镜像。

### 方案 3: 使用代理或 VPN

如果你有稳定的网络环境，可以：

1. 临时重命名 settings.xml：
   ```bash
   mv ~/.m2/settings.xml ~/.m2/settings.xml.bak
   ```

2. 运行 OpenRewrite 命令

3. 恢复 settings.xml：
   ```bash
   mv ~/.m2/settings.xml.bak ~/.m2/settings.xml
   ```

## 快速测试

使用方案 1 测试是否可以正常工作：

```bash
# 进入项目目录
cd /Users/wxg/my-projects/java-labs/utilities/openrewrite-demo

# 创建临时 settings.xml
cat > /tmp/maven-settings.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
</settings>
EOF

# 测试 OpenRewrite
mvn -s /tmp/maven-settings.xml rewrite:help
```

如果成功，你会看到 OpenRewrite 插件的帮助信息。

## 推荐做法

**对于本项目的学习**，建议使用**方案 1**（临时 settings.xml），原因：
- ✅ 不影响其他项目的 Maven 配置
- ✅ 简单快速，无需修改全局配置
- ✅ 可以随时切换回阿里云镜像

**对于长期使用**，建议使用**方案 2**（修改 mirrorOf），原因：
- ✅ 一次配置，永久生效
- ✅ 保留阿里云镜像的速度优势
- ✅ 同时支持 OpenRewrite 等特殊依赖

## 常见问题

### Q: 为什么阿里云镜像没有 OpenRewrite？
A: OpenRewrite 是一个相对较新的工具，阿里云镜像可能同步不及时，或者某些版本没有被镜像。

### Q: 使用 Maven Central 会很慢吗？
A: 首次下载可能较慢，但 Maven 会缓存到本地仓库（`~/.m2/repository`），后续使用会很快。

### Q: 可以只为这个项目配置吗？
A: 可以！使用方案 1 的临时 settings.xml 就是只针对这个项目的配置。

## 下一步

配置好仓库后，继续阅读 `QUICKSTART.md` 开始你的 OpenRewrite 之旅！
