# 快速开始指南

## 环境要求

- JDK 17 或更高版本
- Maven 3.6+

## 构建项目

在项目根目录执行：

```bash
# 构建整个 concurrency-revisited 模块
mvn clean install -pl java-fundamentals/concurrency-revisited -am -DskipTests

# 或者只构建某个子模块
mvn clean install -pl java-fundamentals/concurrency-revisited/01-thread-basics -am -DskipTests
```

## 运行示例

### 方式 1: 使用 Maven exec 插件

```bash
cd java-fundamentals/concurrency-revisited/01-thread-basics

# 运行线程创建示例
mvn exec:java -Dexec.mainClass="io.github.daihaowxg.concurrency.basics.ThreadCreationExample"

# 运行线程生命周期示例
mvn exec:java -Dexec.mainClass="io.github.daihaowxg.concurrency.basics.ThreadLifecycleExample"
```

### 方式 2: 使用 IDE

1. 在 IDEA 或 Eclipse 中导入项目
2. 找到对应的示例类
3. 右键运行 `main` 方法

### 方式 3: 编译后直接运行

```bash
cd java-fundamentals/concurrency-revisited/01-thread-basics
mvn clean compile

# 运行编译后的类
java -cp target/classes io.github.daihaowxg.concurrency.basics.ThreadCreationExample
```

## 学习建议

1. **按顺序学习**: 建议按照模块编号顺序学习（01 → 08）
2. **理论结合实践**: 先阅读 `docs/README.md`，再运行代码示例
3. **动手实践**: 修改示例代码，观察运行结果
4. **做好笔记**: 在 `docs/notes.md` 中记录学习心得
5. **总结归纳**: 完成每个模块后，总结关键知识点

## 目录结构说明

```
01-thread-basics/
├── pom.xml                      # Maven 配置
├── docs/                        # 文档目录
│   ├── README.md                # 核心概念总结
│   ├── notes.md                 # 详细学习笔记（待补充）
│   ├── best-practices.md        # 最佳实践（待补充）
│   └── common-pitfalls.md       # 常见陷阱（待补充）
└── src/main/java/               # 代码示例
    └── io/github/daihaowxg/concurrency/basics/
        ├── ThreadCreationExample.java
        └── ThreadLifecycleExample.java
```

## 下一步

- 查看 [README.md](README.md) 了解完整的学习路径
- 开始第一个模块：[01-thread-basics](01-thread-basics/docs/README.md)
- 参考旧模块代码：`java-fundamentals/jdk8-examples/java-concurrency`

## 常见问题

### Q: 为什么使用 JDK 17 而不是 JDK 8？

A: JDK 17 是长期支持版本（LTS），包含了许多性能优化和新特性。同时，本模块也会涉及虚拟线程（JDK 21+）等新特性的学习。

### Q: 与旧模块有什么区别？

A: 旧模块主要是代码示例，新模块更注重系统化学习，包含完整的文档、学习路径和最佳实践总结。

### Q: 如何贡献代码？

A: 欢迎添加新的示例代码和完善文档。请确保代码风格一致，并添加必要的注释。
