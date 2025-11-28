package io.github.daihaowxg.junit;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * 基于 JUnit 4 的单元测试类
 */
public class CalculatorTest {

    private Calculator calculator;

    @BeforeClass
    public static void setupClass() {
        System.out.println("--- 所有测试开始前执行 ---");
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("--- 所有测试结束后执行 ---");
    }

    @Before
    public void setup() {
        System.out.println("...单个测试开始，创建 Calculator 实例...");
        // 每个测试方法前都创建一个新的实例，保证测试之间的独立性
        calculator = new Calculator();
    }

    @After
    public void tearDown() {
        System.out.println("...单个测试结束，清理资源...");
        calculator = null;
    }

    @Test
    public void testAdd() {
        System.out.println("正在测试 add 方法");
        int result = calculator.add(5, 3);
        assertEquals(8, result); // 验证 5 + 3 是否等于 8
    }

    @Test
    public void testSubtract() {
        System.out.println("正在测试 subtract 方法");
        int result = calculator.subtract(10, 4);
        assertEquals(6, result); // 验证 10 - 4 是否等于 6
    }

    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        System.out.println("正在测试除零异常");
        calculator.divide(5, 0); // 期望此处抛出 ArithmeticException
    }

    @Test
    public void testDivideSuccess() {
        System.out.println("正在测试正常除法");
        int result = calculator.divide(10, 2);
        assertEquals(5, result);
    }

    @Ignore("这个测试暂时被忽略")
    @Test
    public void testSomethingWIP() {
        // 这是一个正在开发中或暂时失效的功能
        fail("这个测试不应该被执行");
    }
}