# Java 并发编程重新学习

## 📚 学习目标

本模块用于系统性地重新学习 Java 并发编程知识，包含完整的代码示例和学习笔记。

## 🗂️ 模块结构

```
concurrency-revisited/
├── 01-thread-basics/              # 线程基础
│   ├── docs/                      # 学习笔记
│   └── src/                       # 代码示例
│
├── 02-synchronization/            # 线程安全与同步
│   ├── docs/                      # synchronized、volatile、happens-before
│   └── src/                       # 同步机制示例
│
├── 03-locks/                      # Lock 框架
│   ├── docs/                      # ReentrantLock、ReadWriteLock、StampedLock
│   └── src/                       # 锁的使用示例
│
├── 04-concurrent-utilities/       # 并发工具类
│   ├── docs/                      # CountDownLatch、CyclicBarrier、Semaphore、Phaser
│   └── src/                       # 工具类使用示例
│
├── 05-thread-pool/                # 线程池
│   ├── docs/                      # ThreadPoolExecutor、ScheduledExecutorService
│   └── src/                       # 线程池配置与使用
│
├── 06-concurrent-collections/     # 并发集合
│   ├── docs/                      # ConcurrentHashMap、CopyOnWriteArrayList 等
│   └── src/                       # 并发集合使用示例
│
├── 07-atomic/                     # 原子类
│   ├── docs/                      # AtomicInteger、AtomicReference、LongAdder
│   └── src/                       # 原子操作示例
│
└── 08-async-programming/          # Future 与异步编程
    ├── docs/                      # Future、CompletableFuture、虚拟线程
    └── src/                       # 异步编程模式
```

## 📖 学习路径

### 第一阶段：基础概念
- [ ] 线程的创建与生命周期
- [ ] 线程的状态转换
- [ ] 线程中断机制
- [ ] 守护线程与用户线程

### 第二阶段：线程安全
- [ ] 什么是线程安全
- [ ] synchronized 关键字
- [ ] volatile 关键字
- [ ] happens-before 原则
- [ ] 死锁问题与解决

### 第三阶段：Lock 框架
- [ ] Lock 接口与 ReentrantLock
- [ ] 读写锁 ReadWriteLock
- [ ] StampedLock 乐观读
- [ ] Condition 条件队列

### 第四阶段：并发工具类
- [ ] CountDownLatch 倒计时门闩
- [ ] CyclicBarrier 循环栅栏
- [ ] Semaphore 信号量
- [ ] Phaser 分阶段同步

### 第五阶段：线程池
- [ ] Executor 框架
- [ ] ThreadPoolExecutor 核心参数
- [ ] 常见线程池类型
- [ ] 拒绝策略
- [ ] ScheduledExecutorService

### 第六阶段：并发集合
- [ ] ConcurrentHashMap 原理
- [ ] CopyOnWriteArrayList
- [ ] BlockingQueue 家族
- [ ] ConcurrentSkipListMap

### 第七阶段：原子类
- [ ] CAS 原理
- [ ] AtomicInteger 等基本类型
- [ ] AtomicReference 引用类型
- [ ] LongAdder 与 LongAccumulator
- [ ] FieldUpdater

### 第八阶段：异步编程
- [ ] Future 接口
- [ ] CompletableFuture 详解
- [ ] 异步编程模式
- [ ] JDK 21 虚拟线程（Virtual Threads）

## 🎯 与旧模块的区别

**旧模块** (`jdk8-examples/java-concurrency`):
- 基于 JDK 8
- 主要是代码示例
- 按照 API 分类组织

**新模块** (`concurrency-revisited`):
- 基于 JDK 17+
- 包含完整的学习笔记和文档
- 按照学习路径组织
- 更系统化的知识体系
- 包含原理分析和最佳实践

## 📝 文档规范

每个子模块的 `docs/` 目录下应包含：
- `README.md` - 该主题的核心概念总结
- `notes.md` - 详细学习笔记
- `best-practices.md` - 最佳实践
- `common-pitfalls.md` - 常见陷阱

## 🚀 快速开始

```bash
# 构建整个模块
cd java-fundamentals/concurrency-revisited
mvn clean install

# 运行某个示例
cd 01-thread-basics
mvn exec:java -Dexec.mainClass="io.github.daihaowxg.concurrency.basics.ThreadCreationExample"
```

## 📚 参考资料

- 《Java 并发编程实战》
- 《Java 并发编程的艺术》
- JDK 官方文档
- JEP 444: Virtual Threads
