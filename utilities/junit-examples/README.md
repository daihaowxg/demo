# JUnit Examples

本模块通过对比的方式展示了 JUnit 4 和 JUnit 5 (Jupiter) 的核心特性差异。

## 目录结构

```
io.github.daihaowxg.junit
├── junit4/                 # JUnit 4 示例
│   ├── LifecycleTest.java  # 生命周期 (@BeforeClass, @Before, etc.)
│   ├── AssertionTest.java  # 断言与异常 (@Test(expected=...), assertEquals)
│   └── AssumptionTest.java # 假设 (Assume)
│
└── junit5/                 # JUnit 5 示例
    ├── LifecycleTest.java  # 生命周期 (@BeforeAll, @BeforeEach, etc.)
    ├── AssertionTest.java  # 断言与异常 (assertThrows, assertAll)
    └── AssumptionTest.java # 假设 (assumeTrue, assumingThat)
```

## 特性对比速查

| 特性 | JUnit 4 | JUnit 5 | 说明 |
|------|---------|---------|------|
| **生命周期** | `@BeforeClass` | `@BeforeAll` | 所有测试前执行 (static) |
| | `@AfterClass` | `@AfterAll` | 所有测试后执行 (static) |
| | `@Before` | `@BeforeEach` | 每个测试前执行 |
| | `@After` | `@AfterEach` | 每个测试后执行 |
| | `@Ignore` | `@Disabled` | 禁用测试 |
| **断言** | `Assert.assertEquals` | `Assertions.assertEquals` | 基础断言 |
| | N/A | `assertAll` | 组合断言 (报告所有失败) |
| **异常测试** | `@Test(expected=Ex.class)` | `assertThrows(Ex.class, () -> ...)` | JUnit 5 方式更灵活精确 |
| **假设** | `Assume.assumeTrue` | `Assumptions.assumeTrue` | 假设失败则跳过测试 |
| **显示名称** | N/A | `@DisplayName` | 自定义测试名称 |

## 运行测试

使用 Maven 运行所有测试：

```bash
mvn test
```
