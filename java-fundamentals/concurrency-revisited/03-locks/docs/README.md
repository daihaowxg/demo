# Lock 框架

## 核心概念

### 1. Lock 接口

Lock 接口提供了比 synchronized 更灵活的锁操作。

### 2. ReentrantLock

可重入锁，支持公平锁和非公平锁。

### 3. ReadWriteLock

读写锁，允许多个读线程同时访问，但写线程独占访问。

### 4. StampedLock

JDK 8 引入的新锁，支持乐观读。

### 5. Condition

条件队列，用于线程间的协调。

## 学习要点

- [ ] 理解 Lock 与 synchronized 的区别
- [ ] 掌握 ReentrantLock 的使用
- [ ] 理解公平锁与非公平锁
- [ ] 掌握 ReadWriteLock 的使用场景
- [ ] 学习 StampedLock 的乐观读机制
- [ ] 掌握 Condition 的使用

## 代码示例

参考 `src/main/java/io/github/daihaowxg/concurrency/locks/` 目录下的示例代码。
