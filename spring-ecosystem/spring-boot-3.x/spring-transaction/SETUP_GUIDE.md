# 运行环境配置说明

## 问题诊断

当前系统检测到的 Java 版本：
```
java version "1.8.0_391"
```

**问题**：项目需要 Java 17，但当前系统使用的是 Java 8。

## 解决方案

### 方案 1: 安装 Java 17 (推荐)

#### 使用 Homebrew 安装
```bash
# 安装 Homebrew（如果还没有安装）
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 安装 Java 17
brew install openjdk@17

# 设置 JAVA_HOME
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc

# 重新加载配置
source ~/.zshrc
```

#### 手动下载安装
1. 访问 [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) 或 [Adoptium](https://adoptium.net/)
2. 下载 macOS ARM64 版本
3. 安装 DMG 文件
4. 设置环境变量：
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 17)
   export PATH="$JAVA_HOME/bin:$PATH"
   ```

### 方案 2: 临时使用 Java 17 (如果已安装但未激活)

检查系统是否已安装 Java 17：
```bash
/usr/libexec/java_home -V
```

如果列表中有 Java 17，临时设置：
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
./mvnw clean spring-boot:run
```

### 方案 3: 修改项目使用 Java 8 (不推荐)

如果无法安装 Java 17，可以降级项目配置（但可能会有兼容性问题）：

修改 `pom.xml`：
```xml
<properties>
    <java.version>8</java.version>
</properties>
```

修改 Spring Boot 版本为 2.x：
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>
    <relativePath/>
</parent>
```

## 验证安装

安装完成后，验证 Java 版本：
```bash
java -version
# 应该显示: openjdk version "17.x.x" 或 java version "17.x.x"

echo $JAVA_HOME
# 应该显示 Java 17 的路径
```

## 运行项目

配置好 Java 17 后，运行项目：
```bash
cd /Users/lizhi/Desktop/wxg/java-labs/spring-ecosystem/spring-boot-3.x/spring-transaction
./mvnw clean spring-boot:run
```

## 预期输出

成功运行后，你会看到：
1. Spring Boot 启动日志
2. 自动执行的 ConnectionHolder Bug 演示
3. 三种 bug 场景的错误信息
4. 正确用法的成功执行
5. Spring 内部机制的详细信息

## 常见问题

### Q: Maven wrapper 没有执行权限
```bash
chmod +x mvnw
```

### Q: 端口被占用
修改 `application.properties` 添加：
```properties
server.port=8081
```

### Q: 想要查看更详细的日志
已经配置了 DEBUG 级别的日志，查看控制台输出即可。

## 联系方式

如果遇到其他问题，请检查：
1. Java 版本是否正确（必须是 17 或更高）
2. Maven 是否可以正常运行
3. 网络连接是否正常（Maven 需要下载依赖）
