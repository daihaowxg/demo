# Clean Code 代码示例

本模块用于存放阅读《Clean Code》（代码整洁之道）这本书时的代码示例和实践。

## 📚 关于《Clean Code》

《Clean Code》是 Robert C. Martin（Uncle Bob）编写的经典著作，讲述了如何编写可读、可维护、高质量的代码。

## 📂 目录结构

```
clean-code/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── io/github/daihaowxg/cleancode/
│   │           ├── chapter01/    # 第1章：整洁代码
│   │           ├── chapter02/    # 第2章：有意义的命名
│   │           ├── chapter03/    # 第3章：函数
│   │           ├── chapter04/    # 第4章：注释
│   │           ├── chapter05/    # 第5章：格式
│   │           ├── chapter06/    # 第6章：对象和数据结构
│   │           ├── chapter07/    # 第7章：错误处理
│   │           ├── chapter08/    # 第8章：边界
│   │           ├── chapter09/    # 第9章：单元测试
│   │           ├── chapter10/    # 第10章：类
│   │           ├── chapter11/    # 第11章：系统
│   │           ├── chapter12/    # 第12章：迭进
│   │           ├── chapter13/    # 第13章：并发编程
│   │           ├── chapter14/    # 第14章：逐步改进
│   │           ├── chapter15/    # 第15章：JUnit 内幕
│   │           ├── chapter16/    # 第16章：重构 SerialDate
│   │           └── chapter17/    # 第17章：代码味道与启发
│   └── test/
│       └── java/
│           └── io/github/daihaowxg/cleancode/
└── README.md
```

## 🎯 学习目标

### 基础篇（第1-6章）
- 理解整洁代码的核心原则和重要性
- 掌握有意义的命名技巧
- 学习如何编写简洁、单一职责的函数
- 了解何时以及如何使用注释
- 掌握代码格式化的最佳实践
- 理解对象和数据结构的区别

### 进阶篇（第7-10章）
- 掌握错误处理的最佳实践
- 学习如何处理第三方代码和边界
- 掌握单元测试的编写方法和 TDD
- 理解类的设计原则（单一职责、开闭原则等）

### 高级篇（第11-17章）
- 学习系统级别的整洁架构设计
- 理解简单设计的四大规则（迭进）
- 掌握并发编程的最佳实践和常见陷阱
- 通过实际案例学习逐步改进代码的方法
- 深入理解优秀开源项目的代码质量（JUnit）
- 学习大型重构的系统方法
- 识别代码味道并应用相应的重构手法

## 📝 使用说明

每个章节对应一个包，包含该章节的代码示例：
- **Bad Example**：不好的代码示例（重构前）
- **Good Example**：整洁的代码示例（重构后）
- **说明文档**：解释重构的原因和改进点

## 🚀 运行测试

```bash
mvn test
```

## 📖 参考资料

- 《Clean Code: A Handbook of Agile Software Craftsmanship》 - Robert C. Martin
- [Clean Code 中文版](https://book.douban.com/subject/4199741/)
