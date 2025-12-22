# 第2章：有意义的命名 - 示例说明

本文档详细说明了 `NamingExamples.java` 中的各个示例。

## 📋 十六大命名原则

### 1️⃣ 原则1：名副其实

**核心思想**：名称应该告诉你它为什么存在、做什么事、如何使用。

**示例对比**：
```java
// ❌ 不好
private int d; // 需要注释才能理解

// ✅ 好
private int elapsedTimeInDays; // 名称本身就说明了一切
```

**关键要点**：
- 如果名称需要注释来补充，那就不算名副其实
- 好的名称能让代码自文档化

---

### 2️⃣ 原则2：避免误导

**核心思想**：避免使用容易引起误解的名称。

**示例对比**：
```java
// ❌ 不好：accountList 不是真正的 List
private String accountList; // 实际上是逗号分隔的字符串

// ✅ 好
private String accountGroup;
private String accountsString;
```

**常见误导**：
- 使用 `List`、`Map` 等容器名称，但实际类型不是
- 使用容易混淆的字符（l vs 1, O vs 0）
- 使用过于相似的名称

---

### 3️⃣ 原则3：做有意义的区分

**核心思想**：不要使用 a1, a2 这样的名称，也不要使用意义含混的废话。

**示例对比**：
```java
// ❌ 不好：数字序列
public void copyChars(char[] a1, char[] a2)

// ✅ 好：有意义的名称
public void copyChars(char[] source, char[] destination)
```

**废话词汇**：
- `Info`、`Data`、`a`、`an`、`the` 等都是意义含混的废话
- `Product` vs `ProductInfo` vs `ProductData` - 无法区分
- 应该用 `ProductSpecification`、`ProductInventory` 等明确的名称

---

### 4️⃣ 原则4：使用读得出来的名称

**核心思想**：避免使用缩写和自造词。

**示例对比**：
```java
// ❌ 不好：无法读出来
private Date genymdhms; // generation year month day hour minute second

// ✅ 好：可以读出来
private Date generationTimestamp;
```

**好处**：
- 便于团队交流
- 便于代码审查
- 便于新人理解

---

### 5️⃣ 原则5：使用可搜索的名称

**核心思想**：单字母名称和数字常量很难搜索。

**示例对比**：
```java
// ❌ 不好：魔法数字
for (int j = 0; j < 34; j++) {
    s += (t[j] * 4) / 5;
}

// ✅ 好：使用常量
private static final int NUMBER_OF_TASKS = 34;
private static final int WORK_DAYS_PER_WEEK = 5;

for (int taskIndex = 0; taskIndex < NUMBER_OF_TASKS; taskIndex++) {
    // ...
}
```

**例外**：
- 单字母名称可用于小作用域的本地变量
- 循环计数器 `i`、`j`、`k` 是可以接受的

---

### 6️⃣ 原则6：避免编码

**核心思想**：不要在名称中加入类型信息（匈牙利命名法、成员前缀等）。

**示例对比**：
```java
// ❌ 不好：类型编码
String strName;
int iCount;
private String m_description;

// ✅ 好：不需要编码
private String name;
private int count;
private String description;
```

**接口和实现**：
```java
// ❌ 不好
interface IShapeFactory {}

// ✅ 好
interface ShapeFactory {}
class ShapeFactoryImpl implements ShapeFactory {}
```

---

### 7️⃣ 原则7：避免思维映射

**核心思想**：不应当让读者在脑中把你的名称翻译为他们熟知的名称。

**示例对比**：
```java
// ❌ 不好：需要思维映射
for (String u : urls) {
    String r = getResponse(u); // r 是什么？
}

// ✅ 好：直接明了
for (String url : urls) {
    String response = getResponse(url);
}
```

**专业程序员的智慧**：
- 明确是王道
- 专业程序员善用其能，编写他人能理解的代码

---

### 8️⃣ 原则8：类名应该是名词或名词短语

**好的类名**：
- `Customer`、`WikiPage`、`Account`、`AddressParser`

**应避免的类名**：
- `Manager`、`Processor`、`Data`、`Info` - 太模糊

**更好的替代**：
- `OrderProcessor`、`AccountManager`、`CustomerData`

---

### 9️⃣ 原则9：方法名应该是动词或动词短语

**好的方法名**：
```java
public void save()
public void delete()
public void processPayment()
public void sendEmail()
```

**访问器、修改器、断言**：
```java
public String getName()
public void setName(String name)
public boolean isDeleted()
```

**静态工厂方法**：
```java
// ✅ 好：名称说明参数
public static Rule9_MethodNames fromName(String name)
public static Rule9_MethodNames fromId(int id)

// ❌ 不好：参数含义不明确
new Rule9_MethodNames("John"); // 这是什么？
---

### 🔟 原则10：别抖机灵

**核心思想**：不要使用俏皮话、俚语、文化相关的笑话作为名称。

**示例对比**：
```java
// ❌ 不好：使用俏皮话
public void holyHandGrenade() {}  // Monty Python 的梗
public void whack() {}            // 俚语
public void eatMyShorts() {}      // 辛普森一家的台词

// ✅ 好：使用清晰、专业的名称
public void deleteItems() {}
public void kill() {}   // 编程领域的标准术语
public void abort() {}  // 标准技术术语
```

**关键要点**：
- 宁可明确，毋为好玩
- 说清楚你的意思，在代码中不要展示你的幽默天赋

---

### 1️⃣1️⃣ 原则11：每个概念对应一个词

**核心思想**：给每个抽象概念选一个词，并且一以贯之。

**示例对比**：
```java
// ❌ 不好：同一概念使用不同的词
class UserController   { void fetchUser() {} }    // fetch
class OrderController  { void retrieveOrder() {} } // retrieve
class ProductController { void getProduct() {} }   // get

// ✅ 好：统一使用同一个词
class UserController    { void getUser() {} }
class OrderController   { void getOrder() {} }
class ProductController { void getProduct() {} }
```

**关键要点**：
- 统一使用 `get`、`fetch` 或 `retrieve` 中的一个
- 统一使用 `Controller`、`Manager` 或 `Driver` 中的一个

---

### 1️⃣2️⃣ 原则12：别用双关语

**核心思想**：避免将同一个词用于不同的目的。

**示例对比**：
```java
// ❌ 不好：add 有双关含义
public int add(int a, int b) { return a + b; }  // 数学加法
public void add(int item) { list.add(item); }   // 添加到集合

// ✅ 好：使用不同的词表示不同的概念
public int sum(int a, int b) { return a + b; }  // 数学加法
public void append(int item) { list.add(item); } // 添加到集合
```

**关键要点**：
- 一词一义
- 如果语义不同，就应该用不同的词

---

### 1️⃣3️⃣ 原则13：使用解决方案领域名称

**核心思想**：使用计算机科学术语、算法名、模式名、数学术语等。

**示例**：
```java
// ✅ 好：使用设计模式名称
interface AccountVisitor {}  // Visitor 模式
class AccountFactory {}      // Factory 模式
class JobQueue {}            // Queue 数据结构

// ✅ 好：使用算法名称
void quickSort(int[] array) {}
void binarySearch(int[] array, int target) {}

// ✅ 好：使用计算机科学术语
class TreeNode {}   // 树节点
class Adapter {}    // 适配器模式
class Decorator {}  // 装饰器模式
```

**关键要点**：
- 读你代码的人也是程序员
- 他们懂计算机科学术语

---

### 1️⃣4️⃣ 原则14：使用源自所涉问题领域的名称

**核心思想**：如果没有合适的技术术语，就使用业务领域的名称。

**示例**：
```java
// ✅ 好：银行系统
class Account {
    void deposit(double amount) {}
    void withdraw(double amount) {}
    void transfer(Account target, double amount) {}
}

// ✅ 好：电商系统
class ShoppingCart {
    void addItem(CartItem item) {}
    void checkout() {}
}

// ✅ 好：医疗系统
class Patient {
    private String diagnosis;
    private List<Prescription> prescriptions;
}
```

**关键要点**：
- 优秀的程序员和设计师会区分解决方案领域和问题领域的概念
- 与问题领域更为相关的代码，应该采用问题领域的术语

---

### 1️⃣5️⃣ 原则15：添加有意义的语境

**核心思想**：通过类、函数、命名空间等添加语境，让名称更清晰。

**示例对比**：
```java
// ❌ 不好：缺乏语境
private String street;
private String city;
private String state;
// 这些变量是什么的地址？用户？公司？配送地址？

// ✅ 好：通过类名添加语境
class Address {
    private String street;
    private String city;
    private String state;
}

// ✅ 更好：使用专门的类
class Customer {
    private String name;
    private Address shippingAddress;  // 配送地址
    private Address billingAddress;   // 账单地址
}
```

**关键要点**：
- 很多名称本身并不清楚，需要用良好命名的类、函数或命名空间来放置名称
- 给读者提供上下文

---

### 1️⃣6️⃣ 原则16：不要添加没用的语境

**核心思想**：只要短名称足够清楚，就不要添加多余的语境。

**示例对比**：
```java
// ❌ 不好：添加了没用的语境
// 假设我们在开发 "Gas Station Deluxe" 应用
class GSDAccountAddress {}   // GSD 前缀是多余的
class GSDCustomer {}         // 每个类都加 GSD 前缀，毫无必要

// ✅ 好：只在必要时添加语境
class AccountAddress {}
class Customer {}

// ❌ 不好：过度限定
class MailingAddress {
    private String mailingAddressStreet;  // 前缀多余
    private String mailingAddressCity;
}

// ✅ 好：适度的语境
class MailingAddress {
    private String street;  // 类名已经提供了语境
    private String city;
}
```

**关键要点**：
- 短名称通常优于长名称，只要足够清楚
- 不要给类添加没必要的前缀

---

## 🎯 综合示例：重构演进

### 阶段1：糟糕的命名
```java
public List<int[]> getThem() {
    List<int[]> list1 = new ArrayList<>();
    for (int[] x : theList) {
        if (x[0] == 4) {
            list1.add(x);
        }
    }
    return list1;
}
```

### 阶段2：改进的命名
```java
public List<int[]> getFlaggedCells() {
    List<int[]> flaggedCells = new ArrayList<>();
    for (int[] cell : gameBoard) {
        if (isFlagged(cell)) {
            flaggedCells.add(cell);
        }
    }
    return flaggedCells;
}
```

### 阶段3：最佳实践
```java
public List<Cell> getFlaggedCells() {
    List<Cell> flaggedCells = new ArrayList<>();
    for (Cell cell : gameBoard) {
        if (cell.isFlagged()) {
            flaggedCells.add(cell);
        }
    }
    return flaggedCells;
}

public static class Cell {
    private int status;
    public boolean isFlagged() {
        return status == FLAGGED;
    }
}
```

---

## 💡 关键要点总结

### 基础原则（1-9）
1. **名副其实**：名称应该揭示意图
2. **避免误导**：不要使用容易混淆的名称
3. **做有意义的区分**：避免废话和数字序列
4. **使用读得出来的名称**：避免缩写和自造词
5. **使用可搜索的名称**：用常量代替魔法数字
6. **避免编码**：不使用匈牙利命名法和成员前缀
7. **避免思维映射**：使用明确的名称
8. **类名用名词**：类名应该是名词或名词短语
9. **方法名用动词**：方法名应该是动词或动词短语

### 进阶原则（10-16）
10. **别抖机灵**：不使用俏皮话和文化相关的笑话
11. **每个概念对应一个词**：保持一致性
12. **别用双关语**：一词一义
13. **使用解决方案领域名称**：如 `Factory`、`Visitor`、`Queue`
14. **使用问题领域名称**：如 `Account`、`Patient`、`ShoppingCart`
15. **添加有意义的语境**：通过类、函数、命名空间等
16. **不要添加没用的语境**：短名称优于长名称，只要足够清楚

---

## 📚 延伸阅读

- 《Clean Code》第2章：Meaningful Names
- 《代码大全》第11章：变量名的力量
- 《重构》：改善既有代码的设计

---

**记住**：好的命名是编写整洁代码的第一步！
